package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalStrategy;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.Shelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LaddersTest {
    @Test
    void testEmptyShelf() throws JsonBadParsingException {
        Shelf emptyShelf = new Shelf(6,5);
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.Ladders, 2);
        assertTrue(to_test.getID() == 7);
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void testTrueDescending() throws JsonBadParsingException {
        Shelf trueShelf = new Shelf(Jsonable.pathToJsonObject("src/test/resources/CommonGoalTests/TestShelf_1_Ladders.json",Shelf.class));
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.Ladders, 2);
        assertTrue(to_test.getID() == 7);
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testTrueAscending() throws JsonBadParsingException {
        Shelf trueShelf = new Shelf(Jsonable.pathToJsonObject("src/test/resources/CommonGoalTests/TestShelf_2_Ladders.json", Shelf.class));
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.Ladders, 2);
        assertTrue(to_test.getID() == 7);
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testFalse() throws JsonBadParsingException {
        Shelf falseShelf = new Shelf(Jsonable.pathToJsonObject("src/test/resources/CommonGoalTests/TestShelf_3_Ladders.json", Shelf.class));
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.Ladders, 2);
        assertTrue(to_test.getID() == 7);
        assertFalse(to_test.check(falseShelf));
    }
}