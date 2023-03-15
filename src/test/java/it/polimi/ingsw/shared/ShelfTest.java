package it.polimi.ingsw.shared;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ShelfTest {
    final int rows = 6;
    final int columns = 5;
    final String value = "Cat";
    @Test
    void insert_test(){
        Shelf s = new Shelf(rows, columns);
        for(int i = 0; i<rows; i++){ // check it initializes the object correctly
            for(int j = 0; j<columns; j++){
                assertEquals(s.getTile(new Position(i,j)), Tile.Empty);
            }
        }
        for(int j = 0; j < columns-1; j++){
            for(int i = 0; i < rows-2; i++){
                s.insertTile(Tile.valueOf(value),j);
                assertEquals(s.getTile(new Position(rows-i-1,j)), Tile.valueOf(value)); // check insert work correctly
            }
        }
        assertEquals(s.getHighestColumn(), rows); //check getHighestColumn doesn't change in value after filling all columns except for last one
        for(int i = 0; i<rows-2; i++){
            s.insertTile(Tile.valueOf(value),columns-1);
            assertEquals(s.getHighestColumn(), rows-i-1); //check getHighestColumn change in value as I fill last column
        }
    }
}