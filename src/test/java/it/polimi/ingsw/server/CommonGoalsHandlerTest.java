package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Shelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalsHandlerTest {
    @Test
    void giveAllPointsTest1(){
        Shelf shelf = new Shelf();
        CommonGoalsHandler tested_object = new CommonGoalsHandler(4);
        assertEquals(16, tested_object.giveAllPoints(shelf));
        assertEquals(12, tested_object.giveAllPoints(shelf));
        assertEquals(8, tested_object.giveAllPoints(shelf));
        assertEquals(4, tested_object.giveAllPoints(shelf));
    }
}