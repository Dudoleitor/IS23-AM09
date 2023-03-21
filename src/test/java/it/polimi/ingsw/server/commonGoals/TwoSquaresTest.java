package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.server.CommonGoalsFactory;
import it.polimi.ingsw.shared.Shelf;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwoSquaresTest {
    @Test
    void testEmptyShelf(){
        Shelf emptyShelf = new Shelf(6,5);
        AbstractCommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(2,2);
        assertTrue(to_test.getID() == 2);
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void testTrue() {
        Shelf trueShelf = new Shelf("src/test/resources/CommonGoalTests/TestShelf_1_2Squares.json");
        AbstractCommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(2, 2);
        assertTrue(to_test.getID() == 2);
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testFalse(){
        Shelf falseShelf = new Shelf("src/test/resources/CommonGoalTests/TestShelf_2_2Squares.json");
        AbstractCommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(2,2);
        assertTrue(to_test.getID() == 2);
        assertFalse(to_test.check(falseShelf));
    }
    void knownBug(){
        Shelf falseShelf = new Shelf("src/test/resources/TestShelf_3_2Squares.json");
        AbstractCommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(2,2);
        assertTrue(to_test.getID() == 2);
        assertTrue(to_test.check(falseShelf));
    }
}