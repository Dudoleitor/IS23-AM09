package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalStrategy;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.Shelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FourGroupsOfFourTest {
    private final String basePath = getClass().getClassLoader().getResource("CommonGoalTests").getPath() + "/";

    @Test
    void testEmptyShelf() throws JsonBadParsingException {
        Shelf emptyShelf = new Shelf(6,5);
        CommonGoal to_test = new CommonGoal(CommonGoalStrategy.FourGroupsOf4, 2);
        assertEquals(11, to_test.getID());
        assertFalse(to_test.check(emptyShelf));
    }
    @Test
    void testTrue() throws JsonBadParsingException {
        Shelf trueShelf = new Shelf(Jsonable.pathToJsonObject(basePath + "TestShelf_1_4groupsOf4.json",Shelf.class));
        CommonGoal to_test =new CommonGoal(CommonGoalStrategy.FourGroupsOf4, 2);
        assertEquals(11, to_test.getID());
        assertTrue(to_test.check(trueShelf));
    }
    @Test
    void testFalse() throws JsonBadParsingException {
        Shelf falseShelf = new Shelf(Jsonable.pathToJsonObject(basePath + "TestShelf_2_4groupsOf4.json",Shelf.class));
        CommonGoal to_test =new CommonGoal(CommonGoalStrategy.FourGroupsOf4, 2);
        assertEquals(11, to_test.getID());
        assertFalse(to_test.check(falseShelf));
    }
}