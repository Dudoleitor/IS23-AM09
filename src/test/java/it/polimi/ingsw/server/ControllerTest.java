package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    @Test
    void jsonCongruenceTest() throws JsonBadParsingException {
        List<String> players = new ArrayList<>();
        players.add("fridgeieri");
        players.add("fridgeoggi");
        players.add("fridgedomani");
        Controller testCont1 = new Controller(players);
        Controller testCont2 = new Controller(testCont1.toJson());

        assertEquals(testCont1.toJson().toJSONString(), testCont2.toJson().toJSONString());
        //null list
        assertThrows(ControllerGenericException.class, () -> new Controller((List<String>) null));
        //no players
        //TODO could be better
        assertThrows(Exception.class, () ->  new Controller(new ArrayList<String>()));
    }

    @Test
    void builderTest(){
        //null list
        assertThrows(ControllerGenericException.class, () -> new Controller((List<String>) null));
        //no players
        //TODO could be better
        assertThrows(Exception.class, () ->  new Controller(new ArrayList<String>()));
    }

    @Test
    void getThat() throws JsonBadParsingException {
        List<String> players = new ArrayList<>();
        players.add("fridgeieri");
        players.add("fridgeoggi");
        players.add("fridgedomani");
        Controller c = new Controller(players);
        List<Player> playerList = new ArrayList<>();
        try{
            playerList = (List<Player>) players.stream().map(p -> {
                try {
                    return new Player(p,new Shelf(Constants.shelfRows,Constants.shelfColumns), new PlayerGoal(Constants.jsonPathForPlayerGoals));
                } catch (JsonBadParsingException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        }
        catch (Exception e){
            fail();
        }
        assertEquals(0,c.getTurn());
        assertTrue(c.getCommonGoals().stream().allMatch(cg -> cg.getID() >= 1 && cg.getID() <= 12));
        assertEquals(c.getPlayers(), playerList);
        assertTrue(c.getBoard().sameBoard(new Board(3)));
        assertEquals(players.get(0),c.getCurrentPlayerName());

        //all shelves are empty
        Shelf emptyShelf = new Shelf(Constants.shelfRows,Constants.shelfColumns);

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
        Controller c = new Controller(players);
        List<Player> playerList = null;
        try {
            playerList = (List<Player>) players.stream().map(p -> {
                try {
                    return new Player(p, new Shelf(Constants.shelfRows, Constants.shelfColumns), new PlayerGoal(Constants.jsonPathForPlayerGoals));
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
        Controller c = new Controller(players);


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
        Controller c = new Controller(playernames);

        List<Player> players = c.getPlayers();
        c.setActivity(players.get(1).getName(),false);

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
        Controller c = new Controller(playerNames);

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
        Controller c = new Controller(playerNames);

        List<Player> players = c.getPlayers();
        //modify a copy of the list
        players.get(0).setActive(false);
        players.get(2).setActive(false);
        assertTrue(c.getActivePlayers().get(0).isActive());
        assertTrue(c.getActivePlayers().get(2).isActive());
    }

    @Test
    void turnIncrement(){
        List<String> players = new ArrayList<>();
        players.add("fridgeieri");
        players.add("fridgeoggi");
        players.add("fridgedomani");
        Controller c = new Controller(players);
        List<Player> playerList = null;
        try {
            playerList = (List<Player>) players.stream().map(p -> {
                try {
                    return new Player(p, new Shelf(Constants.shelfRows, Constants.shelfColumns), new PlayerGoal(Constants.jsonPathForPlayerGoals));
                } catch (JsonBadParsingException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        } catch (Exception e) {
            fail();
        }
        for(int i = 0; i < 10; i++){
            assertEquals(i,c.getTurn());
            assertEquals(c.getCurrentPlayerName(),players.get(i%3));
            c.nextTurn();
        }
    }
}
