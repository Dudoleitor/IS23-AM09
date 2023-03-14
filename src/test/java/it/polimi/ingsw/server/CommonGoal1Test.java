package it.polimi.ingsw.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class CommonGoal1Test {
    @Test
    void test_2_players(){
        CommonGoal1 tested_object = new CommonGoal1();
        tested_object.populatePointsStack(2);
        ArrayList<Integer> stack_state = tested_object.showPointsStack();

        assertEquals(2,stack_state.size());
        assertEquals(4,stack_state.get(0));
        assertEquals(8,stack_state.get(1));
    }
    @Test
    void test_3_players(){
        CommonGoal1 tested_object = new CommonGoal1();
        tested_object.populatePointsStack(3);
        ArrayList<Integer> stack_state = tested_object.showPointsStack();

        assertEquals(3,stack_state.size());
        assertEquals(4,stack_state.get(0));
        assertEquals(6,stack_state.get(1));
        assertEquals(8,stack_state.get(2));
    }
    @Test
    void test_4_players(){
        CommonGoal1 tested_object = new CommonGoal1();
        tested_object.populatePointsStack(4);
        ArrayList<Integer> stack_state = tested_object.showPointsStack();

        assertEquals(4,stack_state.size());
        assertEquals(2,stack_state.get(0));
        assertEquals(4,stack_state.get(1));
        assertEquals(6,stack_state.get(2));
        assertEquals(8,stack_state.get(3));
    }
    @Test
    void test_not_enough_players(){
        CommonGoal1 tested_object = new CommonGoal1();
        tested_object.populatePointsStack(0);
        ArrayList<Integer> stack_state = tested_object.showPointsStack();

        assertEquals(0,stack_state.size());
    }
    @Test
    void too_much_players(){
        CommonGoal1 tested_object = new CommonGoal1();
        tested_object.populatePointsStack(5);
        ArrayList<Integer> stack_state = tested_object.showPointsStack();

        assertEquals(0,stack_state.size());
    }

    @Test
    void test_pop(){
        CommonGoal1 tested_object = new CommonGoal1();
        tested_object.populatePointsStack(4);
        ArrayList<Integer> stack_state = tested_object.showPointsStack();

        assertEquals(4,stack_state.size());
        assertEquals(2,stack_state.get(0));
        assertEquals(4,stack_state.get(1));
        assertEquals(6,stack_state.get(2));
        assertEquals(8,stack_state.get(3));

        stack_state.clear();
        assertEquals(8,tested_object.getPoints());

        stack_state = tested_object.showPointsStack();

        assertEquals(3,stack_state.size());
        assertEquals(2,stack_state.get(0));
        assertEquals(4,stack_state.get(1));
        assertEquals(6,stack_state.get(2));

        stack_state.clear();
        assertEquals(6,tested_object.getPoints());

        stack_state = tested_object.showPointsStack();

        assertEquals(2,stack_state.size());
        assertEquals(2,stack_state.get(0));
        assertEquals(4,stack_state.get(1));
    }
}