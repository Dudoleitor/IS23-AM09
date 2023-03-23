package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalsFactory;
import it.polimi.ingsw.shared.Shelf;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FourGroupsOfFourTest {
    @Test
    void testEmptyShelf(){
        Shelf emptyShelf = new Shelf(6,5);
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(11,2);
        assertTrue(to_test.getID() == 11);
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void testTrue() throws FileNotFoundException, ParseException, IOException {
        Shelf trueShelf = new Shelf("src/test/resources/CommonGoalTests/TestShelf_1_4groupsOf4.json");
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(11, 2);
        assertTrue(to_test.getID() == 11);
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testFalse()throws FileNotFoundException, ParseException, IOException{
        Shelf falseShelf = new Shelf("src/test/resources/CommonGoalTests/TestShelf_2_4groupsOf4.json");
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(11,2);
        assertTrue(to_test.getID() == 11);
        assertFalse(to_test.check(falseShelf));
    }
}