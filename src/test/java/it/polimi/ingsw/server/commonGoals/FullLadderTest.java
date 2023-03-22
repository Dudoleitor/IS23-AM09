package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalsFactory;
import it.polimi.ingsw.shared.Shelf;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FullLadderTest {
    @Test
    void testEmptyShelf(){
        Shelf emptyShelf = new Shelf(6,5);
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(10,2);
        assertTrue(to_test.getID() == 10);
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void testTrueDescending()throws FileNotFoundException, ParseException, IOException {
        Shelf trueShelf = new Shelf("src/test/resources/CommonGoalTests/TestShelf_1_FullLadder.json");
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(10, 2);
        assertTrue(to_test.getID() == 10);
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testTrueAscending() throws FileNotFoundException, ParseException, IOException{
        Shelf trueShelf = new Shelf("src/test/resources/CommonGoalTests/TestShelf_3_FullLadder.json");
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(10, 2);
        assertTrue(to_test.getID() == 10);
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testFalse()throws FileNotFoundException, ParseException, IOException{
        Shelf falseShelf = new Shelf("src/test/resources/CommonGoalTests/TestShelf_2_FullLadder.json");
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(10,2);
        assertTrue(to_test.getID() == 10);
        assertFalse(to_test.check(falseShelf));
    }
}