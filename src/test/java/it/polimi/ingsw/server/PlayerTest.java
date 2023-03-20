package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Shelf;

import it.polimi.ingsw.shared.ShelfGenericException;
import it.polimi.ingsw.shared.Tile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void creationTest(){
        String jsonPath = "src/test/resources/TestGoal.json";
        String name = "fridgeieri";
        ServerShelf shelf = new ServerShelf(3, 3);
        PlayerGoal goal = new PlayerGoal(jsonPath);
        Player testpl = new Player(name, shelf, goal);

        assertEquals("fridgeieri", testpl.getName());
        // TODO waiting for methods in ServerShelf
        // assertEquals((Shelf) shelf, testpl.getShelf());
        assertFalse(testpl.hasFinished());
        assertEquals(0, testpl.getCommonGoalPoints());
        assertEquals(0, testpl.checkPersonalGoal());
        assertTrue(testpl.getCheckedCommonGoals().isEmpty());
        testpl.checkPersonalGoal();
    }

    @Test
    void personalGoalTest() {
        String jsonPath = "src/test/resources/TestGoal.json";
        String name = "fridgeieri";
        ServerShelf shelf = new ServerShelf(3, 3);
        PlayerGoal goal = new PlayerGoal(jsonPath);
        Player testpl = new Player(name, shelf, goal);

        testpl.insertTile(Tile.Cat, 0);
        Shelf testShelf = testpl.getShelf();
        assertEquals(Tile.Cat, testShelf.getTile(2,0));
        assertEquals(1, testpl.checkPersonalGoal());
    }

    @Test
    void hasFinishedTest1() {
        String jsonPath = "src/test/resources/TestGoal.json";
        String name = "fridgeieri";
        ServerShelf shelf = new ServerShelf(3, 3);
        PlayerGoal goal = new PlayerGoal(jsonPath);
        Player testpl = new Player(name, shelf, goal);

        testpl.insertTile(Tile.Game, 0);  // Filling first column
        testpl.insertTile(Tile.Game, 0);
        testpl.insertTile(Tile.Game, 0);
        assertFalse(testpl.hasFinished());

        testpl.insertTile(Tile.Plant, 1);  // Filling second column
        testpl.insertTile(Tile.Plant, 1);
        testpl.insertTile(Tile.Plant, 1);
        assertFalse(testpl.hasFinished());

        testpl.insertTile(Tile.Trophy, 2);  //Filling last column
        testpl.insertTile(Tile.Trophy, 2);
        testpl.insertTile(Tile.Trophy, 2);
        assertTrue(testpl.hasFinished());

        assertThrows(ShelfGenericException.class, () -> testpl.insertTile(Tile.Cat, 0));
    }

    @Test
    void hasFinishedTest2() {
        String jsonPath = "src/test/resources/TestGoal.json";
        String name = "fridgeieri";
        ServerShelf shelf = new ServerShelf(3, 3);
        PlayerGoal goal = new PlayerGoal(jsonPath);
        Player testpl = new Player(name, shelf, goal);

        testpl.insertTile(Tile.Game, 0);  // Filling first row
        testpl.insertTile(Tile.Game, 1);
        testpl.insertTile(Tile.Game, 2);
        assertFalse(testpl.hasFinished());

        testpl.insertTile(Tile.Plant, 0);  // Filling second row
        testpl.insertTile(Tile.Plant, 1);
        testpl.insertTile(Tile.Plant, 2);
        assertFalse(testpl.hasFinished());

        testpl.insertTile(Tile.Trophy, 0);  //Filling last row
        testpl.insertTile(Tile.Trophy, 1);
        testpl.insertTile(Tile.Trophy, 2);
        assertTrue(testpl.hasFinished());

        assertThrows(ShelfGenericException.class, () -> testpl.insertTile(Tile.Cat, 0));
    }

    @Test
    void CommonGoalTest() {
        String jsonPath = "src/test/resources/TestGoal.json";
        String name = "fridgeieri";
        ServerShelf shelf = new ServerShelf(3, 3);
        PlayerGoal playerGoal = new PlayerGoal(jsonPath);
        List<AbstractCommonGoal> commonGoalsList = new ArrayList<>();
        CommonGoalTest commonGoal1 = new CommonGoalTest(10);
        CommonGoalTest commonGoal2 = new CommonGoalTest(20);
        commonGoalsList.add(commonGoal1);
        commonGoalsList.add(commonGoal2);
        Player testpl = new Player(name, shelf, playerGoal);

        testpl.checkCommonGoals(commonGoalsList);
        assertEquals(0, testpl.getCommonGoalPoints());

        commonGoal1.setAchieved();
        testpl.checkCommonGoals(commonGoalsList);
        assertEquals(8, testpl.getCommonGoalPoints());
        assertFalse(testpl.getCheckedCommonGoals().isEmpty());
        assertEquals(1, testpl.getCheckedCommonGoals().size());
        assertEquals(10, testpl.getCheckedCommonGoals().get(0));
        assertEquals(4, commonGoal1.givePoints());

        commonGoal2.setAchieved();
        testpl.checkCommonGoals(commonGoalsList);
        assertEquals(16, testpl.getCommonGoalPoints());
        assertEquals(2, testpl.getCheckedCommonGoals().size());
        assertEquals(20, testpl.getCheckedCommonGoals().get(1));
        assertEquals(4, commonGoal2.givePoints());
    }

    @Test
    void AdjacentPointsTest() {
        String jsonPath = "src/test/resources/TestGoal.json";
        String name = "fridgeieri";
        ServerShelf shelf = new ServerShelf(3, 3);
        PlayerGoal playerGoal = new PlayerGoal(jsonPath);
        Player testpl = new Player(name, shelf, playerGoal);
        // TODO waiting for countAdjacentPoints on shelf
    }
}

class CommonGoalTest extends AbstractCommonGoal {
    private boolean achieved = false;
    private final int id;
    public CommonGoalTest(int id) {
        this.id = id;
        points = new Stack<>();
        populatePointsStack(2);
    }
    @Override
    public int getID() {return id;}
        @Override
    public boolean check(Shelf shelf) { return achieved;}
    public void setAchieved() {this.achieved = true;}
}