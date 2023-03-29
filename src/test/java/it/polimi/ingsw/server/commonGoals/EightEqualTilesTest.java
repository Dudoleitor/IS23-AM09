package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalStrategy;
import it.polimi.ingsw.server.CommonGoalsException;
import it.polimi.ingsw.server.CommonGoalsFactory;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.ShelfGenericException;
import it.polimi.ingsw.shared.TileGenericException;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class EightEqualTilesTest{
    @Test
    void testEmptyShelf() throws CommonGoalsException, ShelfGenericException {
        Shelf emptyShelf = new Shelf(6,5);
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(6,2);
        assertTrue(to_test.getID() == 6);
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void trueTest() throws FileNotFoundException, ParseException, IOException, ShelfGenericException, TileGenericException, CommonGoalsException {
        Shelf trueShelf = new Shelf("src/test/resources/CommonGoalTests/TestShelf_1_8equalsTiles.json");
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(6,2);
        assertTrue(to_test.getID() == 6);
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void falseTest() throws FileNotFoundException, ParseException, IOException, ShelfGenericException, TileGenericException, CommonGoalsException {
        Shelf falseShelf = new Shelf("src/test/resources/CommonGoalTests/TestShelf_2_8equalsTiles.json");
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(6,2);
        assertTrue(to_test.getID() == 6);
        assertFalse(to_test.check(falseShelf));
    }

    //TESTING OF ABSTRACT CLASSES METHODS
    @Test
    void showPointsStackTest(){
        ArrayList<Integer> stackState = new ArrayList<>();
        stackState.add(10);
        stackState.add(20);
        stackState.add(30);
        stackState.add(40);
        CommonGoal t = new CommonGoal(CommonGoalStrategy.EightEqualTiles,stackState);
        assertEquals(stackState,t.showPointsStack());
    }
}