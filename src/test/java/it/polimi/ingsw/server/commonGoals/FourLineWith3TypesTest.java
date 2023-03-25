package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalsException;
import it.polimi.ingsw.server.CommonGoalsFactory;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.ShelfGenericException;
import it.polimi.ingsw.shared.TileGenericException;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FourLineWith3TypesTest {
    @Test
    void testEmptyShelf() throws CommonGoalsException, ShelfGenericException {
        Shelf emptyShelf = new Shelf(6,5);
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(8,2);
        assertTrue(to_test.getID() == 8);
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void testTrue() throws FileNotFoundException, ParseException, IOException, ShelfGenericException, CommonGoalsException, TileGenericException {
        Shelf trueShelf = new Shelf("src/test/resources/CommonGoalTests/TestShelf_1_4lines3types.json");
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(8, 2);
        assertTrue(to_test.getID() == 8);
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testFalse() throws FileNotFoundException, ParseException, IOException, ShelfGenericException, CommonGoalsException, TileGenericException {
        Shelf falseShelf = new Shelf("src/test/resources/CommonGoalTests/TestShelf_2_4lines3types.json");
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(8,2);
        assertTrue(to_test.getID() == 8);
        assertFalse(to_test.check(falseShelf));
    }
}