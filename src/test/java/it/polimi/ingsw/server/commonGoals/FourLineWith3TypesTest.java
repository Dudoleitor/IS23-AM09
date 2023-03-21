package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.server.CommonGoalsFactory;
import it.polimi.ingsw.shared.Shelf;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FourLineWith3TypesTest {
    @Test
    void testEmptyShelf(){
        Shelf emptyShelf = new Shelf(6,5);
        AbstractCommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(8,2);
        assertTrue(to_test.getID() == 8);
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void testTrue() throws FileNotFoundException, ParseException, IOException {
        Shelf trueShelf = new Shelf("src/test/resources/CommonGoalTests/TestShelf_1_4lines3types.json");
        AbstractCommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(8, 2);
        assertTrue(to_test.getID() == 8);
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testFalse()throws FileNotFoundException, ParseException, IOException{
        Shelf falseShelf = new Shelf("src/test/resources/CommonGoalTests/TestShelf_2_4lines3types.json");
        AbstractCommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(8,2);
        assertTrue(to_test.getID() == 8);
        assertFalse(to_test.check(falseShelf));
    }
}