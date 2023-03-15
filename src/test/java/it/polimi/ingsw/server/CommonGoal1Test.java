package it.polimi.ingsw.server;

import it.polimi.ingsw.server.commonGoals.CommonGoal1;
import it.polimi.ingsw.server.commonGoals.CommonGoal2;
import it.polimi.ingsw.shared.Shelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class CommonGoal1Test {
    final int rows = 6;
    final int columns = 5;
    @Test
    void test_2_players(){
        CommonGoal1 tested_object = new CommonGoal1(2);
        ArrayList<Integer> stack_state = tested_object.showPointsStack();

        assertEquals(2,stack_state.size());
        assertEquals(4,stack_state.get(0));
        assertEquals(8,stack_state.get(1));
    }
    @Test
    void test_3_players(){
        CommonGoal1 tested_object = new CommonGoal1(3);
        ArrayList<Integer> stack_state = tested_object.showPointsStack();

        assertEquals(3,stack_state.size());
        assertEquals(4,stack_state.get(0));
        assertEquals(6,stack_state.get(1));
        assertEquals(8,stack_state.get(2));
    }
    @Test
    void test_4_players(){
        CommonGoal1 tested_object = new CommonGoal1(4);
        ArrayList<Integer> stack_state = tested_object.showPointsStack();

        assertEquals(4,stack_state.size());
        assertEquals(2,stack_state.get(0));
        assertEquals(4,stack_state.get(1));
        assertEquals(6,stack_state.get(2));
        assertEquals(8,stack_state.get(3));
    }
    @Test
    void test_not_enough_players(){
        CommonGoal1 tested_object = new CommonGoal1(0);
        ArrayList<Integer> stack_state = tested_object.showPointsStack();

        assertEquals(0,stack_state.size());
    }
    @Test
    void too_much_players(){
        CommonGoal1 tested_object = new CommonGoal1(5);
        ArrayList<Integer> stack_state = tested_object.showPointsStack();

        assertEquals(0,stack_state.size());
    }

    @Test
    void test_pop(){
        CommonGoal1 tested_object = new CommonGoal1(4);
        ArrayList<Integer> stack_state = tested_object.showPointsStack();
        Shelf useless_shelf = new Shelf(rows, columns);

        assertEquals(4,stack_state.size());
        assertEquals(2,stack_state.get(0));
        assertEquals(4,stack_state.get(1));
        assertEquals(6,stack_state.get(2));
        assertEquals(8,stack_state.get(3));

        stack_state.clear();
        assertEquals(8,tested_object.givePoints(useless_shelf));

        stack_state = tested_object.showPointsStack();

        assertEquals(3,stack_state.size());
        assertEquals(2,stack_state.get(0));
        assertEquals(4,stack_state.get(1));
        assertEquals(6,stack_state.get(2));

        stack_state.clear();
        assertEquals(6,tested_object.givePoints(useless_shelf));

        stack_state = tested_object.showPointsStack();

        assertEquals(2,stack_state.size());
        assertEquals(2,stack_state.get(0));
        assertEquals(4,stack_state.get(1));
    }

    @Test
    void test_compatibility(){
        CommonGoal1 common_goal1 = new CommonGoal1(4);
        CommonGoal2 common_goal2 = new CommonGoal2(4);
        Shelf shelf = new Shelf(rows, columns);
        assertEquals(16,common_goal1.givePoints(shelf)+common_goal2.givePoints(shelf));
    }
}