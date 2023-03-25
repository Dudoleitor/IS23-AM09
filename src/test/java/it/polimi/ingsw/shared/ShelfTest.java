package it.polimi.ingsw.shared;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class ShelfTest {
    final int rows = 6;
    final int columns = 5;
    final String value = "Cat";
    @Test
    void insert_test() throws ShelfGenericException {
        Shelf s = new Shelf(rows, columns);
        for (int i = 0; i < rows; i++) { // check it initializes the object correctly
            for (int j = 0; j < columns; j++) {
                assertEquals(s.getTile(new Position(i, j)), Tile.Empty);
            }
        }
        for (int j = 0; j < columns - 1; j++) {
            for (int i = 0; i < rows - 2; i++) {
                s.insertTile(Tile.valueOf(value), j);
                assertEquals(s.getTile(new Position(rows - i - 1, j)), Tile.valueOf(value)); // check insert work correctly
            }
        }
        assertEquals(s.getHighestColumn(), rows); //check getHighestColumn doesn't change in value after filling all columns except for last one
        for (int i = 0; i < rows - 2; i++) {
            s.insertTile(Tile.valueOf(value), columns - 1);
            assertEquals(s.getHighestColumn(), rows - i - 1); //check getHighestColumn change in value as I fill last column
        }
    }
    @Test
    void shelf_equals_test() throws ShelfGenericException {
        Shelf s1 = new Shelf(rows, columns);
        Shelf s2 = new Shelf(rows, columns);
        for(int j = 0; j< columns-1; j++){
            s1.insertTile(Tile.valueOf("Cat"), j);
            s2.insertTile(Tile.valueOf("Cat"), j);
        }
        s1.insertTile(Tile.valueOf("Book"), 0);
        assert(!s1.equals(s2)); //check they are different
        s2.insertTile(Tile.valueOf("Book"), 0);
        assert(s1.equals(s2)); //check they are the same
    }
    @Test
    void checkAdiacentTest() throws ShelfGenericException {
        Shelf s = new Shelf(4,4);
        s.insertTile(Tile.Cat,0);
        s.insertTile(Tile.Cat,0);
        s.insertTile(Tile.Book,0);
        s.insertTile(Tile.Cat,0);

        s.insertTile(Tile.Trophy,1);
        s.insertTile(Tile.Cat,1);
        s.insertTile(Tile.Cat,1);
        s.insertTile(Tile.Cat,1);

        s.insertTile(Tile.Trophy,2);
        s.insertTile(Tile.Trophy,2);
        s.insertTile(Tile.Trophy,2);
        s.insertTile(Tile.Book,2);

        s.insertTile(Tile.Trophy,3);
        s.insertTile(Tile.Book,3);
        s.insertTile(Tile.Book,3);
        s.insertTile(Tile.Book,3);

        int res = s.countAdjacentPoints();
        assertEquals(16, res);

        s = new Shelf(4,4);
        s.insertTile(Tile.Trophy,0);
        s.insertTile(Tile.Book,0);
        s.insertTile(Tile.Empty,0);
        s.insertTile(Tile.Empty,0);

        s.insertTile(Tile.Trophy,1);
        s.insertTile(Tile.Book,1);
        s.insertTile(Tile.Cat,1);
        s.insertTile(Tile.Empty,1);

        s.insertTile(Tile.Trophy,2);
        s.insertTile(Tile.Book,2);
        s.insertTile(Tile.Trophy,2);
        s.insertTile(Tile.Empty,2);

        s.insertTile(Tile.Trophy,3);
        s.insertTile(Tile.Book,3);
        s.insertTile(Tile.Trophy,3);
        s.insertTile(Tile.Empty,3);

        res = s.countAdjacentPoints();
        assertEquals(6, res);
    }
    @Test
    public void ShelfJsonTester() throws FileNotFoundException, ParseException, IOException, ShelfGenericException, TileGenericException {
        String jsonPath = "src/test/resources/ShelfTests/ShelfInsert.json";
        Shelf s = new Shelf(jsonPath);
        Shelf s1 = new Shelf(4, 4);
        s1.insertTile(Tile.Trophy, 0);
        s1.insertTile(Tile.Cat, 0);
        s1.insertTile(Tile.Trophy, 0);
        s1.insertTile(Tile.Cat, 0);

        s1.insertTile(Tile.Cat, 1);
        s1.insertTile(Tile.Cat, 1);
        s1.insertTile(Tile.Cat, 1);
        s1.insertTile(Tile.Cat, 1);

        s1.insertTile(Tile.Book, 2);
        s1.insertTile(Tile.Cat, 2);
        s1.insertTile(Tile.Cat, 2);
        s1.insertTile(Tile.Empty, 2);

        s1.insertTile(Tile.Cat, 3);
        s1.insertTile(Tile.Cat, 3);
        s1.insertTile(Tile.Book, 3);
        s1.insertTile(Tile.Book, 3);

        assert(s.equals(s1));
    }
    @Test
    void JsonObjectTest() throws ShelfGenericException, TileGenericException {
        JSONParser jsonParser = new JSONParser(); //initialize JSON parser
        Object obj = null; //acquire JSON object file
        try {
            obj = jsonParser.parse(new FileReader("src/test/resources/ShelfTests/ShelfInsert.json"));
        } catch (IOException e) {
            fail();
        } catch (ParseException e) {
            fail();
        }
        JSONObject objShelf = (JSONObject) ((JSONObject) obj).get("shelf"); //acquire shelf object
        Shelf shelf = new Shelf(objShelf);
        assertEquals(4,shelf.getRows());
        assertEquals(4,shelf.getColumns());
        assertEquals(shelf.getTile(0,0),Tile.Cat);
    }

    @Test
    void indexOutOfBoundsTest(){
        Shelf shelf = new Shelf(5,6);
        assertThrows(ShelfGenericException.class,() -> shelf.getTile(-1,0));
    }

    @Test
    void gettersTest(){
        Shelf shelf = new Shelf(5,6);
        assertEquals(6,shelf.getColumns());
        assertEquals(5,shelf.getRows());
    }
}
