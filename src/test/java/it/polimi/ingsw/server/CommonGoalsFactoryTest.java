package it.polimi.ingsw.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalsFactoryTest {
    @Test
    void testIDCreation(){
        CommonGoalsFactory factory = new CommonGoalsFactory();
        for(int i = 1; i <= 12; i++){
            assertEquals(i,factory.create_goal_with_ID(i,2).getID());
        }
    }
}