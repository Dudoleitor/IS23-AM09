package it.polimi.ingsw.shared;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    void equalsTest() {
        Position a = new Position(0,0);
        Position b = new Position(0,0);
        //correct
        assertEquals(a, b);
        assertEquals(a, new Position(0, 0));

        //different
        Position c = new Position(0,1);
        assertNotEquals(c, a);
        //other objects
        Shelf k = new Shelf(5,6);
        assertNotEquals(a, k);

    }

    @Test
    void alignedColsTest(){
        List<Position> alignedCols = new ArrayList<>();

        //empty
        assertTrue(Position.areAligned(alignedCols));

        //correct
        alignedCols.add(new Position(0,0));
        alignedCols.add(new Position(1,0));
        alignedCols.add(new Position(2,0));
        assertTrue(Position.areAligned(alignedCols));

        //non sequential rows
        alignedCols.add(new Position(4,0));
        assertFalse(Position.areAligned(alignedCols));

        alignedCols.clear();

        //different columns
        alignedCols.add(new Position(0,0));
        alignedCols.add(new Position(1,0));
        alignedCols.add(new Position(2,1));

        assertFalse(Position.areAligned(alignedCols));
    }

    @Test
    void alignedRowsTest(){
        List<Position> alignedRows = new ArrayList<>();

        //correct (testing inverse order)
        alignedRows.add(new Position(0,2));
        alignedRows.add(new Position(0,1));
        alignedRows.add(new Position(0,0));
        assertTrue(Position.areAligned(alignedRows));

        //non sequential rows
        alignedRows.add(new Position(0,4));
        assertFalse(Position.areAligned(alignedRows));

        alignedRows.clear();

        //different columns
        alignedRows.add(new Position(0,2));
        alignedRows.add(new Position(0,1));
        alignedRows.add(new Position(2,0));

        assertFalse(Position.areAligned(alignedRows));

        alignedRows.clear();

        //empty
        assertTrue(Position.areAligned(alignedRows));

        //one tile
        alignedRows.add(new Position(2,5));

        assertTrue(Position.areAligned(alignedRows));
    }

    @Test
    void duplicatesTestFalse(){
        List<Position> positions = new ArrayList<>();

        //empty
        assertFalse(Position.duplicates(positions));

        positions.add(new Position(0,0));
        positions.add(new Position(1,0));
        positions.add(new Position(0,1));

        assertFalse(Position.duplicates(positions));
    }

    @Test
    void duplicatesTestTrue(){
        List<Position> dups = new ArrayList<>();

        dups.add(new Position(0,0));
        dups.add(new Position(0,0));
        dups.add(new Position(0,0));

        assertTrue(Position.duplicates(dups));
    }
    @Test
    void fromString(){
        Position p0 = Position.fromString("(0,0)");
        Position p1 = new Position(0,0);
        assertEquals(p1,p0);

        Position p2 = Position.fromString("15,1");
        Position p3 = new Position(15,1);
        assertEquals(p2,p3);

        Position bad0 = Position.fromString("lala");
        assertEquals(null,bad0);

        Position bad1 = Position.fromString("15,16,17");
        assertEquals(null,bad1);
    }
}