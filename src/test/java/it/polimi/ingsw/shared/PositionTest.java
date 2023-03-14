package it.polimi.ingsw.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    @Test
    void getter_test_row(){
        Position pos = new Position(10,12);
        assertEquals(10,pos.getRow());
    }
    @Test
    void getter_test_column(){
        Position pos = new Position(10,12);
        assertEquals(12,pos.getColumn());
    }
}