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
        List<String> players = new ArrayList<>();
        players.add("fridgeieri");
        players.add("fridgeoggi");
        players.add("fridgedomani");
        players.add("friededopodomani");
        Controller c = new Controller(players);
        List<Player> playerList = null;
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
        //contains the correct players
        assertEquals(c.getPlayers(), playerList);
        //valid commongoals (hardcoded valid values 1-12)
        assertTrue(c.getCommonGoals().stream().allMatch(cg -> cg.getID() >= 1 && cg.getID() <= 12));
        //start from turn 0
        assertEquals(0,c.getTurn());
        //the Board is a correct empty board for 4 players
        assertTrue(c.getBoard().sameBoard(new Board(4)));
        //the first player is actually the first
        assertEquals(players.get(0),c.getCurrentPlayerName());
        //all shelves are empty
        Shelf emptyShelf = new Shelf(Constants.shelfRows,Constants.shelfColumns);
        assertTrue(c.getShelves().stream().allMatch(s -> s.equals(emptyShelf)));
    }
}
