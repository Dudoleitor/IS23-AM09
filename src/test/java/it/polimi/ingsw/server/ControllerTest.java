package it.polimi.ingsw.server;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    @Test
    void jsonCongruenceTest() throws JsonParsingException {
        List<String> players = new ArrayList<>();
        players.add("fridgeieri");
        players.add("fridgeoggi");
        players.add("fridgedomani");
        Controller testCont1 = new Controller(players);
        Controller testCont2 = new Controller(testCont1.toJson());

        assertEquals(testCont1.toJson().toJSONString(), testCont2.toJson().toJSONString());
    }
}
