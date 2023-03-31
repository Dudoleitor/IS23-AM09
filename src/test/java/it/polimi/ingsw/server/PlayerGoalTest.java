package it.polimi.ingsw.server;
import it.polimi.ingsw.shared.BadPositionException;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Tile;
import it.polimi.ingsw.shared.Shelf;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerGoalTest {

    String basePath = "src/test/resources/PlayerGoalTests/";
    @Test
    void FileNotFoundTest() {
        String jsonPath = "ghost.json";
        Exception e = assertThrows(JsonBadParsingException.class, () -> new PlayerGoal(jsonPath));
        assertEquals("Error while loading json: file not found", e.getMessage());
    }
    @Test
    void EmptyJsonTest(){
        String jsonPath = basePath + "EmptyJsonTest.json";
        Exception e = assertThrows(JsonBadParsingException.class, () -> new PlayerGoal(jsonPath));
        assertEquals("Error while parsing json", e.getMessage());
    }
     @Test
    void RandomFileTest() {
         String jsonPath = basePath + "RandomFileTest.json";
         Exception e = assertThrows(JsonBadParsingException.class, () -> new PlayerGoal(jsonPath));
         assertEquals("Error while parsing json", e.getMessage());
     }

     @Test
    void WrongMainObjects() {
         String jsonPath1 = basePath + "WrongMainObjects1.json";
         Exception e = assertThrows(JsonBadParsingException.class, () -> new PlayerGoal(jsonPath1));
         assertEquals("Error while parsing json: goals not found", e.getMessage());

         String jsonPath2 = basePath + "WrongMainObjects2.json";
         e = assertThrows(JsonBadParsingException.class, () -> new PlayerGoal(jsonPath2));
         assertEquals("Error while parsing json: points not found", e.getMessage());

     }

     @Test
    void WrongTile() {
         String jsonPath = basePath + "WrongTile.json";
         Exception e = assertThrows(JsonBadParsingException.class, () -> new PlayerGoal(jsonPath));
         assertEquals("Error while parsing json: wrong position attributes", e.getMessage());
     }

    @Test
    void WrongPos() {
        String jsonPath = basePath + "WrongPos.json";
        Exception e = assertThrows(JsonBadParsingException.class, () -> new PlayerGoal(jsonPath));
        assertEquals("Error while parsing json: wrong position attributes", e.getMessage());
    }

    @Test
    void MultipleGoals() throws JsonBadParsingException {  // Testing if the goals is randomly chosen correctly
        String jsonPath = basePath + "MultipleGoals.json";

        PlayerGoal goal = new PlayerGoal(jsonPath);
        assertTrue(goal.getGoalId() >= 0);
        assertTrue(goal.getGoalId() < 4);

        goal = new PlayerGoal(jsonPath);
        assertTrue(goal.getGoalId() >= 0);
        assertTrue(goal.getGoalId() < 4);

        goal = new PlayerGoal(jsonPath);
        assertTrue(goal.getGoalId() >= 0);
        assertTrue(goal.getGoalId() < 4);
    }

    @Test
    void creationFromJsonObjTest() throws JsonBadParsingException, IOException, ParseException {
        String jsonPath = basePath + "TestGoal.json";
        JSONParser jsonParser = new JSONParser();
        JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(jsonPath));
        PlayerGoal goal = new PlayerGoal(obj, 0);

        assertEquals(0, goal.getGoalId());
    }

    @Test
    void TestGoal() throws JsonBadParsingException, BadPositionException {  // Inserting tiles one at a time and checking behaviour
        String jsonPath = basePath + "TestGoal.json";
        Shelf shelf = new Shelf(3, 3);
        PlayerGoal goal = new PlayerGoal(jsonPath);

        assertEquals(0, goal.getGoalId());

        assertEquals(0, goal.check(shelf));  // Empty shelf

        shelf.insertTile(Tile.Cat, 0);
        assertEquals(1, goal.check(shelf));  // One matches

        shelf.insertTile(Tile.Book, 0);
        assertEquals(3, goal.check(shelf));  // Two matches

        shelf.insertTile(Tile.Trophy, 0);
        assertEquals(5, goal.check(shelf));  // Three matches

        shelf.insertTile(Tile.Plant, 2);
        assertEquals(5, goal.check(shelf));  // Nothing changes
        shelf.insertTile(Tile.Frame, 2);
        assertEquals(5, goal.check(shelf));  // Nothing changes

        shelf.insertTile(Tile.Game, 2);
        assertEquals(8, goal.check(shelf));  // Four matches

        shelf.insertTile(Tile.Plant, 1);  // Wrong tile
        assertEquals(8, goal.check(shelf));  // Nothing changes
    }
}
