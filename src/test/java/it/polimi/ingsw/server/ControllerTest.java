package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Board;
import it.polimi.ingsw.shared.JsonBadParsingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

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

        assertEquals(0,c.getTurn());
        Board emptyBoard = new Board(3);
    }
}
