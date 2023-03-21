package it.polimi.ingsw.server;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalsFactoryTest {
    @Test
    void testIDCreation(){
        CommonGoalsFactory factory = new CommonGoalsFactory();
        for(int i = 1; i <= 12; i++){
            assertEquals(i,factory.create_goal_with_ID(i,2).getID());
        }
    }
    @Test
    void testCreationWithArray(){
        CommonGoalsFactory factory = new CommonGoalsFactory();
        ArrayList<Integer> stackState = new ArrayList<>();
        IntStream.range(1,10).forEach(x->stackState.add(x));
        AbstractCommonGoal goal = CommonGoalsFactory.create_goal_with_ID(1,stackState);
        assertEquals(1,goal.getID());
        assertEquals(9,goal.givePoints());
    }
}