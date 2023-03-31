package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalStrategy;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.Shelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThreeColumnsWith3TypesTest {
    @Test
    void testEmptyShelf() throws JsonBadParsingException {
        Shelf emptyShelf = new Shelf(6,5);
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.ThreeColumnsWith3Types, 2);
        assertEquals(4, to_test.getID());
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void testTrue() throws JsonBadParsingException {
        Shelf trueShelf = new Shelf(Jsonable.pathToJsonObject("src/test/resources/CommonGoalTests/TestShelf_1_3columns_3types.json", Shelf.class));
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.ThreeColumnsWith3Types, 2);
        assertEquals(4, to_test.getID());
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testFalse() throws JsonBadParsingException {
        Shelf falseShelf = new Shelf(Jsonable.pathToJsonObject("src/test/resources/CommonGoalTests/TestShelf_2_3columns_3types.json", Shelf.class));
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.ThreeColumnsWith3Types, 2);
        assertEquals(4, to_test.getID());
        assertFalse(to_test.check(falseShelf));
    }
}