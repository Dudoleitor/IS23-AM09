package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalStrategy;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.Shelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SixGroupsOf2Test {
    @Test
    void testEmptyShelf() throws JsonBadParsingException {
        Shelf emptyShelf = new Shelf(6,5);
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.SixGroupsOf2, 2);
        assertTrue(to_test.getID() == 1);
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void testTrue() throws JsonBadParsingException {
        Shelf trueShelf = new Shelf(Jsonable.pathToJsonObject("src/test/resources/CommonGoalTests/TestShelf_1_6groupsOf2.json", Shelf.class));
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.SixGroupsOf2, 2);
        assertTrue(to_test.getID() == 1);
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testFalse() throws JsonBadParsingException {
        Shelf falseShelf = new Shelf(Jsonable.pathToJsonObject("src/test/resources/CommonGoalTests/TestShelf_2_TwoAllDifferentColumns.json",Shelf.class));
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.SixGroupsOf2, 2);
        assertTrue(to_test.getID() == 1);
        assertFalse(to_test.check(falseShelf));
    }
}