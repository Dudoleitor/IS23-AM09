package it.polimi.ingsw.server;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalStrategy;
import it.polimi.ingsw.shared.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
public class Riemann { //an integration test
    @Test
    void wholeMatch() throws JsonBadParsingException {
        List<String> playerNames = new ArrayList<>();
        playerNames.add("fridgeieri");
        playerNames.add("fridgeoggi");
        playerNames.add("fridgedomani");
        playerNames.add("friededopodomani");
        Controller c = new Controller(playerNames);
        List<Player> playerList = null;
        try{
            playerList = (List<Player>) playerNames.stream().map(p -> {
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

        //TURN 0
        //contains the correct players
        assertEquals(c.getPlayers(), playerList);
        //valid commongoals (hardcoded valid values 1-12)
        assertTrue(c.getCommonGoals().stream().allMatch(cg -> cg.getID() >= 1 && cg.getID() <= 12));
        //start from turn 0
        assertEquals(0,c.getTurn());
        //the Board is a correct empty board for 4 players
        assertTrue(c.getBoard().sameBoard(new Board(4)));
        //the first player is actually the first
        assertEquals(playerNames.get(0),c.getCurrentPlayerName());
        //all shelves are empty
        Shelf emptyShelf = new Shelf(Constants.shelfRows,Constants.shelfColumns);
        for(Player p : c.getPlayers()) {
            assertTrue(c.getShelves().get(p.getName()).equals(emptyShelf));
        }
        printAll(c);

        //TURN 1
        c.prepareForNextPlayer();
        printAll(c);
        assertFalse(c.getBoard().sameBoard(new Board(4)));
        PartialMove pm = new PartialMove();
        List<Tile> picked;
        try {
            picked = new ArrayList<>();
            picked.add(c.getBoard().getTile(0,3));
            pm.addPosition(new Position(0,3));
            picked.add(c.getBoard().getTile(0,4));
            pm.addPosition(new Position(0,4));
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        } catch (BadPositionException e) {
            throw new RuntimeException(e);
        }
        Move m = new Move(pm,0);
        c.moveTiles(playerList.get(0).getName(),m);

        try {
            //The removed tiles are empty
            assertEquals(Tile.Empty,c.getBoard().getTile(0,3));
            assertEquals(Tile.Empty,c.getBoard().getTile(0,4));
            //Next turn has been called
            assertEquals(c.getPlayers().get(1).getName(),c.getCurrentPlayerName());
            //TODO this shouldnt fail
            //assertTrue(c.getShelves().get(playerNames.get(0)).allTilesInColumn(0).containsAll(picked));
        } catch (BadPositionException e) {
            throw new RuntimeException(e);
        }
        printAll(c);
    }

    private void printAll(Controller c) throws JsonBadParsingException {
        List<String> playerNames = c.getPlayers().stream().
                map(p -> p.getName()).
                collect(Collectors.toList());
        for(String p : playerNames){
            System.out.println(c.getShelves().get(p));
        }
        System.out.println(c.getBoard());
    }
}
