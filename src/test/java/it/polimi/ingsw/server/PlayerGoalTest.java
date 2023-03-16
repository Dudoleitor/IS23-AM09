package it.polimi.ingsw.server;
import it.polimi.ingsw.shared.Tile;
import it.polimi.ingsw.shared.Shelf;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerGoalTest {

    @Test
    void FileNotFoundTest() {
        String jsonPath = "ghost.json";
        Exception e = assertThrows(PlayerGoalLoadingException.class, () -> new PlayerGoal(jsonPath));
        assertEquals("Error while loading json: file not found", e.getMessage());
    }
    @Test
    void EmptyJsonTest(){
        String jsonPath = "src/test/resources/EmptyJsonTest.json";
        Exception e = assertThrows(PlayerGoalLoadingException.class, () -> new PlayerGoal(jsonPath));
        assertEquals("Error while parsing json", e.getMessage());
    }
     @Test
    void RandomFileTest() {
         String jsonPath = "src/test/resources/RandomFileTest.json";
         Exception e = assertThrows(PlayerGoalLoadingException.class, () -> new PlayerGoal(jsonPath));
         assertEquals("Error while parsing json", e.getMessage());
     }

     @Test
    void WrongMainObjects() {
         String jsonPath1 = "src/test/resources/WrongMainObjects1.json";
         Exception e = assertThrows(PlayerGoalLoadingException.class, () -> new PlayerGoal(jsonPath1));
         assertEquals("Error while parsing json: goals not found", e.getMessage());

         String jsonPath2 = "src/test/resources/WrongMainObjects2.json";
         e = assertThrows(PlayerGoalLoadingException.class, () -> new PlayerGoal(jsonPath2));
         assertEquals("Error while parsing json: points not found", e.getMessage());

     }

     @Test
    void WrongTile() {
         String jsonPath = "src/test/resources/WrongTile.json";
         Exception e = assertThrows(PlayerGoalLoadingException.class, () -> new PlayerGoal(jsonPath));
         assertEquals("Error while parsing json: wrong position attributes", e.getMessage());
     }

    @Test
    void WrongPos() {
        String jsonPath = "src/test/resources/WrongPos.json";
        Exception e = assertThrows(PlayerGoalLoadingException.class, () -> new PlayerGoal(jsonPath));
        assertEquals("Error while parsing json: wrong position attributes", e.getMessage());
    }

    @Test
    void MultipleGoals() {  // Testing if the goals is randomly chosen correctly
        String jsonPath = "src/test/resources/MultipleGoals.json";

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
    void TestGoal() {  // Inserting tiles one at a time and checking behaviour
        String jsonPath = "src/test/resources/TestGoal.json";
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
