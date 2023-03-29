package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalsFactory;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.TileGenericException;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EqualXTest {
    @Test
    void testEmptyShelf() throws JsonBadParsingException {
        Shelf emptyShelf = new Shelf(6,5);
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(12,2);
        assertTrue(to_test.getID() == 12);
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void testTrue() throws JsonBadParsingException {
        Shelf trueShelf = new Shelf("src/test/resources/CommonGoalTests/TestShelf_1_XShape.json");
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(12, 2);
        assertTrue(to_test.getID() == 12);
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testFalse() throws JsonBadParsingException {
        Shelf falseShelf = new Shelf("src/test/resources/CommonGoalTests/TestShelf_2_XShape.json");
        CommonGoal to_test = CommonGoalsFactory.create_goal_with_ID(12,2);
        assertTrue(to_test.getID() == 12);
        assertFalse(to_test.check(falseShelf));
    }
}