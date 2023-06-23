package it.polimi.ingsw.server;

import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.*;
import it.polimi.ingsw.shared.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    @Test
    void jsonCongruenceTest() throws JsonBadParsingException {
        List<Client> clients = new ArrayList<>();
        clients.add(new ClientStub("fridgeieri"));
        clients.add(new ClientStub("fridgeoggi"));
        clients.add(new ClientStub("fridgedomani"));
        Controller testCont1 = new Controller(clients);
        testCont1.setTurn_testing_only("fridgeieri");
        Controller testCont2 = new Controller(testCont1.toJson(), clients);
        testCont2.setTurn_testing_only("fridgeieri");

        assertEquals(testCont1.toJson().toJSONString(), testCont2.toJson().toJSONString());
        //null list
        assertThrows(ControllerGenericException.class, () -> new Controller((List<Client>) null));
        //no players
        //TODO could be better
        assertThrows(Exception.class, () ->  new Controller(new ArrayList<Client>()));
    }

    @Test
    void builderTest(){
        //null list
        assertThrows(ControllerGenericException.class, () -> new Controller((List<Client>) null));
        //no players
        //TODO could be better
        assertThrows(Exception.class, () ->  new Controller(new ArrayList<Client>()));
    }

    @Test
    void getThat() throws JsonBadParsingException {
        List<String> players = new ArrayList<>();
        players.add("fridgeieri");
        players.add("fridgeoggi");
        players.add("fridgedomani");
        List<Client> clients = players.stream().map(ClientStub::new).collect(Collectors.toList());
        Controller c = new Controller(clients);
        c.setTurn_testing_only("fridgeieri");
        List<Player> playerList = new ArrayList<>();
        try{
            playerList = (List<Player>) players.stream().map(p -> {
                try {
                    return new Player(p,new Shelf(GameSettings.shelfRows, GameSettings.shelfColumns), new PlayerGoal(JSONFilePath.PlayerGoals));
                } catch (JsonBadParsingException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        }
        catch (Exception e){
            fail();
        }
        assertEquals("fridgeieri",c.getCurrentPlayerName());
        assertTrue(c.getCommonGoals().stream().allMatch(cg -> cg.getID() >= 1 && cg.getID() <= 12));
        assertTrue(c.getPlayers().containsAll(playerList) && c.getPlayers().size()==playerList.size());
        // ASSERTION REMOVED! Controller must fill the board
        //assertTrue(c.getBoard().sameBoard(new Board(3)));
        assertEquals(players.get(0),c.getCurrentPlayerName());

        //all shelves are empty
        Shelf emptyShelf = new Shelf(GameSettings.shelfRows, GameSettings.shelfColumns);

        for(Player p : c.getPlayers()) {
            assertTrue(c.getShelves().get(p.getName()).equals(emptyShelf));
        }
    }
    @Test
    void validMoves() throws InvalidMoveException {
        List<String> players = new ArrayList<>();
        players.add("fridgeieri");
        players.add("fridgeoggi");
        players.add("fridgedomani");
        List<Client> clients = players.stream().map(ClientStub::new).collect(Collectors.toList());
        Controller c = new Controller(clients);
        c.setTurn_testing_only("fridgeieri");
        List<Player> playerList = null;
        try {
            playerList = (List<Player>) players.stream().map(p -> {
                try {
                    return new Player(p, new Shelf(GameSettings.shelfRows, GameSettings.shelfColumns), new PlayerGoal(JSONFilePath.PlayerGoals));
                } catch (JsonBadParsingException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        } catch (Exception e) {
            fail();
        }

        Move move;
        PartialMove pm = new PartialMove();
        pm.addPosition(new Position(0, 0));
        pm.addPosition(new Position(0, 1));
        pm.addPosition(new Position(0, 2));
        move = new Move(pm, 0);
    }

    @Test
    void getCurrentPlayer(){
        List<String> players = new ArrayList<>();
        players.add("fridgeieri");
        players.add("fridgeoggi");
        players.add("fridgedomani");
        List<Client> clients = players.stream().map(ClientStub::new).collect(Collectors.toList());
        Controller c = new Controller(clients);
        c.setTurn_testing_only("fridgeieri");


        for (int i = 0; i < 100; i++) {
            assertEquals(c.getCurrentPlayerName(), players.get(i%3));
            c.nextTurn();
        }

    }

    @Test
    void getCurrentPlayer3Players1Quit(){
        List<String> playernames = new ArrayList<>();
        playernames.add("fridgeieri");
        playernames.add("fridgeoggi");
        playernames.add("fridgedomani");
        List<Client> clients = playernames.stream().map(ClientStub::new).collect(Collectors.toList());
        Controller c = new Controller(clients);
        c.setTurn_testing_only("fridgeieri");

        List<Player> players = c.getPlayers();
        assertEquals("fridgeieri", c.getCurrentPlayerName());
        c.setActivity("fridgeoggi",false);
        c.nextTurn();
        assertEquals("fridgedomani", c.getCurrentPlayerName());

        for (int i = 0; i < 10; i++) {
            assertFalse(c.getCurrentPlayerName().equals(c.getPlayers().get(1)));
            c.nextTurn();
        }

    }

    @Test
    void getCurrentPlayer4Players2Quit(){
        List<String> playerNames = new ArrayList<>();
        playerNames.add("fridgeieri");
        playerNames.add("fridgeoggi");
        playerNames.add("fridgedomani");
        playerNames.add("fridgedopodomani");
        List<Client> clients = playerNames.stream().map(ClientStub::new).collect(Collectors.toList());
        Controller c = new Controller(clients);
        c.setTurn_testing_only("fridgeieri");

        List<Player> players = c.getPlayers();
        c.setActivity(players.get(1).getName(),false);
        c.setActivity(players.get(2).getName(),false);

        for (int i = 0; i < 10; i++) {
            assertFalse(c.getCurrentPlayerName().equals(c.getPlayers().get(1)));
            assertFalse(c.getCurrentPlayerName().equals(c.getPlayers().get(2)));
            c.nextTurn();
        }

    }

    @Test
    void getByCopy(){
        List<String> playerNames = new ArrayList<>();
        playerNames.add("fridgeieri");
        playerNames.add("fridgeoggi");
        playerNames.add("fridgedomani");
        playerNames.add("fridgedopodomani");
        List<Client> clients = playerNames.stream().map(ClientStub::new).collect(Collectors.toList());
        Controller c = new Controller(clients);
        c.setTurn_testing_only("fridgeieri");

        List<Player> players = c.getPlayers();
        //modify a copy of the list
        players.get(0).setActive(false);
        players.get(2).setActive(false);
        assertTrue(c.getPlayers().get(0).isActive());
        assertTrue(c.getPlayers().get(2).isActive());
    }

    @Test
    void turnIncrement(){
        List<String> players = new ArrayList<>();
        players.add("fridgeieri");
        players.add("fridgeoggi");
        players.add("fridgedomani");
        List<Client> clients = players.stream().map(ClientStub::new).collect(Collectors.toList());
        Controller c = new Controller(clients);
        c.setTurn_testing_only("fridgeieri");

        List<Player> playerList = null;
        try {
            playerList = (List<Player>) players.stream().map(p -> {
                try {
                    return new Player(p, new Shelf(GameSettings.shelfRows, GameSettings.shelfColumns), new PlayerGoal(JSONFilePath.PlayerGoals));
                } catch (JsonBadParsingException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        } catch (Exception e) {
            fail();
        }
        for(int i = c.getTurn(); i < 10; i++){
            assertEquals(i,c.getTurn());
            assertEquals(c.getCurrentPlayerName(),c.getPlayers().get(i%3).getName());
            c.nextTurn();
        }
    }

    @Test
    void reconnectTest() throws InvalidMoveException {
        List<String> players = new ArrayList<>();
        players.add("fridgeieri");
        players.add("fridgeoggi");
        players.add("fridgedomani");
        List<Client> clients = players.stream().map(ClientStub::new).collect(Collectors.toList());
        Controller c = new Controller(new ArrayList<>(clients));
        c.setTurn_testing_only("fridgeieri");

        c.clientDisconnected(clients.get(2));
        clients.remove(clients.get(2));

        clients.add(new ClientStub(players.get(2)));
        c.clientReconnected(clients.get(2));

        PartialMove partialMove = new PartialMove();
        partialMove.addPosition(new Position(1,3));
        Move testMove = new Move(partialMove, 1);

        c.moveTiles(players.get(0), testMove);
    }
}
