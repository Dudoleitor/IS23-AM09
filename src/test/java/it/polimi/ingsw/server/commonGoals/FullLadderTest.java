package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.server.CommonGoalsFactory;
import it.polimi.ingsw.shared.Shelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FullLadderTest {
    @Test
    void testEmptyShelf(){
        Shelf emptyShelf = new Shelf(6,5);
        AbstractCommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(10,2);
        assertTrue(to_test.getID() == 10);
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void testTrueDescending() {
        Shelf trueShelf = new Shelf("src/test/resources/TestShelf_1_FullLadder.json");
        AbstractCommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(10, 2);
        assertTrue(to_test.getID() == 10);
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testTrueAscending() {
        Shelf trueShelf = new Shelf("src/test/resources/TestShelf_3_FullLadder.json");
        AbstractCommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(10, 2);
        assertTrue(to_test.getID() == 10);
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testFalse(){
        Shelf falseShelf = new Shelf("src/test/resources/TestShelf_2_FullLadder.json");
        AbstractCommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(10,2);
        assertTrue(to_test.getID() == 10);
        assertFalse(to_test.check(falseShelf));
    }
}