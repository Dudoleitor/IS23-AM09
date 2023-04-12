package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.*;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private final String basePath = getClass().getClassLoader().getResource("PlayerGoalTests").getPath() + "/";

    @Test
    void creationTest() throws JsonBadParsingException {
        String jsonPath = basePath + "TestGoal.json";
        String name = "fridgeieri";
        Shelf shelf = new Shelf(3, 3);
        PlayerGoal goal = new PlayerGoal(jsonPath);
        Player testpl = new Player(name, shelf, goal);

        assertEquals("fridgeieri", testpl.getName());
        assertEquals(shelf, testpl.getShelf());
        assertEquals(goal.getGoalId(), testpl.getPersonalGoalId());
        assertFalse(testpl.hasFinished());
        assertEquals(0, testpl.getAdjacentPoints());
        assertEquals(0, testpl.getCommonGoalPoints());
        assertEquals(0, testpl.checkPersonalGoal());
        assertTrue(testpl.getCheckedCommonGoals().isEmpty());
        testpl.checkPersonalGoal();

        JSONObject nullJson = new JSONObject();
        assertThrows(JsonBadParsingException.class,()-> new Player(nullJson));
        JSONObject wrongJson = new JSONObject();
        wrongJson.put("random bullshit",123);
        assertThrows(JsonBadParsingException.class,()-> new Player(wrongJson));
    }

    @Test
    void equalOrNot(){
        String jsonPath = basePath + "TestGoal.json";
        PlayerGoal goal = null;
        try {
            goal = new PlayerGoal(jsonPath);
        } catch (JsonBadParsingException e) {
            fail();
        }
        Player A = new Player("GigaMike",new Shelf(5,6),goal);
        Player alsoA = new Player("gigamike",new Shelf(5,6),goal);
        assertEquals(A,alsoA); //names are not case sensitive

        Player B = new Player("Dudoleitor",new Shelf(5,6),goal);
        assertNotEquals(A,B);

        Player nullP = null;
        assertNotEquals(nullP,A); //null pointer

        assertNotEquals(new ArrayList<>(),A); //other classes
    }

    @Test
    void jsonCongruenceTest() throws JsonBadParsingException {
        String jsonPath = basePath + "TestGoal.json";
        String name = "fridgeieri";
        Shelf shelf = new Shelf(3, 3);
        PlayerGoal goal = new PlayerGoal(jsonPath);
        Player testpl1 = new Player(name, shelf, goal);
        Player testpl2 = new Player(testpl1.toJson());

        assertEquals(testpl1.toJson().toJSONString(), testpl2.toJson().toJSONString());
    }

    @Test
    void personalGoalTest() throws JsonBadParsingException, BadPositionException {
        String jsonPath = basePath + "TestGoal.json";
        String name = "fridgeieri";
        Shelf shelf = new Shelf(3, 3);
        PlayerGoal goal = new PlayerGoal(jsonPath);
        Player testpl = new Player(name, shelf, goal);

        testpl.insertTile(Tile.Cat, 0);
        Shelf testShelf = testpl.getShelf();
        assertEquals(Tile.Cat, testShelf.getTile(2,0));
        assertEquals(1, testpl.checkPersonalGoal());
    }

    @Test
    void hasFinishedTest1() throws JsonBadParsingException, BadPositionException {
        String jsonPath = basePath + "TestGoal.json";
        String name = "fridgeieri";
        Shelf shelf = new Shelf(3, 3);
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

        assertThrows(BadPositionException.class, () -> testpl.insertTile(Tile.Cat, 0));
    }

    @Test
    void hasFinishedTest2() throws JsonBadParsingException, BadPositionException {
        String jsonPath = basePath + "TestGoal.json";
        String name = "fridgeieri";
        Shelf shelf = new Shelf(3, 3);
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

        assertThrows(BadPositionException.class, () -> testpl.insertTile(Tile.Cat, 0));
    }

    @Test
    void CommonGoalTest() throws JsonBadParsingException {
        String jsonPath = basePath + "TestGoal.json";
        String name = "fridgeieri";
        Shelf shelf = new Shelf(3, 3);
        PlayerGoal playerGoal = new PlayerGoal(jsonPath);
        List<CommonGoal> commonGoalsList = new ArrayList<>();

        ArrayList<Integer> stackState = new ArrayList<>();
        stackState.add(4);
        stackState.add(8);
        //the Strategy is ignored in CommonGoalTest, a random one is used because
        //super class CommonGoal requires so
        CommonGoalTest commonGoal1 = new CommonGoalTest(CommonGoalStrategy.Ladders,stackState);
        commonGoal1.setId(10);
        CommonGoalTest commonGoal2 = new CommonGoalTest(CommonGoalStrategy.Ladders,stackState);
        commonGoal2.setId(20);
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
    void AdjacentPointsTest() throws JsonBadParsingException, BadPositionException {
        String jsonPath = basePath + "TestGoal.json";
        String name = "fridgeieri";
        Shelf shelf = new Shelf(4, 3);
        PlayerGoal playerGoal = new PlayerGoal(jsonPath);
        Player testpl = new Player(name, shelf, playerGoal);

        testpl.insertTile(Tile.Cat, 0);
        testpl.insertTile(Tile.Cat, 0);
        testpl.insertTile(Tile.Cat, 1);
        assertEquals(2, testpl.getAdjacentPoints());

        testpl.insertTile(Tile.Cat, 1);
        assertEquals(3, testpl.getAdjacentPoints());

        testpl.insertTile(Tile.Cat, 2);
        assertEquals(5, testpl.getAdjacentPoints());

        testpl.insertTile(Tile.Cat, 1);
        assertEquals(8, testpl.getAdjacentPoints());

        testpl.insertTile(Tile.Cat, 1);
        assertEquals(8, testpl.getAdjacentPoints());
    }
    @Test
    void insertion() throws JsonBadParsingException, BadPositionException {
        String jsonPath = basePath + "TestGoal.json";
        String name = "fridgeieri";
        Shelf shelf = new Shelf(4, 3);
        PlayerGoal playerGoal = new PlayerGoal(jsonPath);
        Player testpl = new Player(name, new Shelf(shelf), playerGoal);

        assertEquals(shelf,testpl.getShelf());
        testpl.insertTile(Tile.Cat,0);

        assertNotEquals(shelf,testpl.getShelf());
        assertEquals(Tile.Cat,testpl.getShelf().getTile(3,0));
    }
}
class CommonGoalTest extends CommonGoal{
    private boolean achieved = false;
    private int id;
    private Stack<Integer> pointsStack;
    public CommonGoalTest(CommonGoalStrategy s, ArrayList<Integer> stackState) throws JsonBadParsingException {
        super(s,2);
        pointsStack = new Stack<>();
        for(Integer i : stackState){
            pointsStack.push(i);
        }
        id = 0;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getID() {return id;}
    public boolean check(Shelf shelf) { return achieved;}
    public void setAchieved() {this.achieved = true;}
}
