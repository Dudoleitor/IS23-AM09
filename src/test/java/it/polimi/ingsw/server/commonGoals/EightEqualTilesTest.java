package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalStrategy;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.Shelf;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EightEqualTilesTest{
    @Test
    void testEmptyShelf() throws JsonBadParsingException {
        Shelf emptyShelf = new Shelf(6,5);
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.EightEqualTiles,2);
        assertTrue(to_test.getID() == 6);
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void trueTest() throws JsonBadParsingException {
        Shelf trueShelf = new Shelf(Jsonable.pathToJsonObject("src/test/resources/CommonGoalTests/TestShelf_1_8equalsTiles.json",Shelf.class));
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.EightEqualTiles,2);
        assertTrue(to_test.getID() == 6);
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void falseTest() throws JsonBadParsingException {
        Shelf falseShelf = new Shelf(Jsonable.pathToJsonObject("src/test/resources/CommonGoalTests/TestShelf_2_8equalsTiles.json",Shelf.class));
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.EightEqualTiles,2);
        assertTrue(to_test.getID() == 6);
        assertFalse(to_test.check(falseShelf));
    }

}