package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.server.CommonGoalsFactory;
import it.polimi.ingsw.shared.Shelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EqualsTilesInAllCornersTest {
    @Test
    void testEmptyShelf(){
        Shelf emptyShelf = new Shelf(6,5);
        AbstractCommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(3,2);
        assertTrue(to_test.getID() == 3);
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void testTrue() {
        Shelf trueShelf = new Shelf("src/test/resources/TestShelf_1_EqualCorners.json");
        AbstractCommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(3, 2);
        assertTrue(to_test.getID() == 3);
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testFalse(){
        Shelf falseShelf = new Shelf("src/test/resources/TestShelf_2_EqualCorners.json");
        AbstractCommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(3,2);
        assertTrue(to_test.getID() == 3);
        assertFalse(to_test.check(falseShelf));
    }
}