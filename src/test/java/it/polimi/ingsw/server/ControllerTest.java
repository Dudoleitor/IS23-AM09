package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Constants;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Shelf;
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
        assertEquals(0,c.getTurn());
        assertTrue(c.getCommonGoals().stream().allMatch(cg -> cg.getID() >= 1 && cg.getID() <= 12));
        //TODO as long as CommonGoals are in board c.getBoard() cannot be tested on controller
        assertEquals(c.getPlayers(), playerList);

        assertEquals(players.get(0),c.getCurrentPlayerName());

        //all shelves are empty
        Shelf emptyShelf = new Shelf(Constants.shelfRows,Constants.shelfColumns);
        assertTrue(c.getShelves().stream().allMatch(s -> s.equals(emptyShelf)));
    }
}
