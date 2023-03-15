package it.polimi.ingsw.server;
import it.polimi.ingsw.shared.Position;
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

     //TODO Add more tests
}
