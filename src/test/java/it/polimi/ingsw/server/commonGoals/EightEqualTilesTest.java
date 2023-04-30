package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.shared.model.CommonGoal;
import it.polimi.ingsw.shared.model.CommonGoalStrategy;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.model.Shelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EightEqualTilesTest{
    private final String basePath = getClass().getClassLoader().getResource("CommonGoalTests").getPath() + "/";
    
    @Test
    void testEmptyShelf() throws JsonBadParsingException {
        Shelf emptyShelf = new Shelf(6,5);
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.EightEqualTiles,2);
        assertEquals(6, to_test.getID());
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void trueTest() throws JsonBadParsingException {
        Shelf trueShelf = new Shelf(Jsonable.pathToJsonObject(basePath + "TestShelf_1_8equalsTiles.json",Shelf.class));
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.EightEqualTiles,2);
        assertEquals(6, to_test.getID());
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void falseTest() throws JsonBadParsingException {
        Shelf falseShelf = new Shelf(Jsonable.pathToJsonObject(basePath + "TestShelf_2_8equalsTiles.json",Shelf.class));
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.EightEqualTiles,2);
        assertEquals(6, to_test.getID());
        assertFalse(to_test.check(falseShelf));
    }

}