package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.shared.model.CommonGoal;
import it.polimi.ingsw.shared.model.CommonGoalStrategy;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.model.Shelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EqualXTest {
    private final String basePath = "CommonGoalTests/";

    @Test
    void testEmptyShelf() throws JsonBadParsingException {
        Shelf emptyShelf = new Shelf(6,5);
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.EqualX, 2);
        assertEquals(12, to_test.getID());
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void testTrue() throws JsonBadParsingException {
        Shelf trueShelf = new Shelf(Jsonable.pathToJsonObject(basePath + "TestShelf_1_XShape.json",Shelf.class));
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.EqualX, 2);
        assertEquals(12, to_test.getID());
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testFalse() throws JsonBadParsingException {
        Shelf falseShelf = new Shelf(Jsonable.pathToJsonObject(basePath + "TestShelf_2_XShape.json",Shelf.class));
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.EqualX, 2);
        assertEquals(12, to_test.getID());
        assertFalse(to_test.check(falseShelf));
    }
}