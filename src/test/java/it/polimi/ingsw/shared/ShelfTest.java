package it.polimi.ingsw.shared;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ShelfTest {
    final int rows = 6;
    final int columns = 5;
    final String value = "Cat";
    @Test
    void generateShelf() throws JsonBadParsingException {
        Shelf s = new Shelf("src/test/resources/ShelfTests/ShelfInsert.json");
        Shelf s1 = new Shelf(4, 4);

        Exception e0 = assertThrows(ShelfRuntimeException.class,() -> new Shelf(0,5));
        assertEquals("Error while generating shelf : dimensions are no valid numbers", e0.getMessage());
        Exception e1 = assertThrows(JsonBadParsingException.class,() -> new Shelf("src/test/resources/ShelfTests/ShelfBadJson1.json"));
        assertEquals("Error while creating Shelf : input Shelf has not a valid configuration", e1.getMessage());
        Exception e2 = assertThrows(JsonBadParsingException.class,() -> new Shelf("src/test/resources/ShelfTests/ShelfBadJson2.json"));
        assertEquals("Error while generating Shelf from JSON : Tile is Invalid type", e2.getMessage());
        Exception e3 = assertThrows(JsonBadParsingException.class,() -> new Shelf("src/test/resources/ShelfTests/notExists.json"));
        assertEquals("Error while generating Shelf from JSON : file not found", e3.getMessage());
        Exception e4 = assertThrows(JsonBadParsingException.class,() -> new Shelf("src/test/resources/ShelfTests/ShelfBadJson3.json"));
        assertEquals("Error while creating Shelf : Tile type not found", e4.getMessage());
        Exception e5 = assertThrows(ShelfRuntimeException.class,() -> new Shelf((Shelf) null));
        assertEquals("Error while creating Shelf : input Shelf is null pointer", e5.getMessage());
    }
    @Test
    void insertTest() throws JsonBadParsingException, BadPositionException {
        Shelf s = new Shelf("src/test/resources/ShelfTests/ShelfBadTileInsert.json");
        s.insertTile(Tile.Trophy,0);
        assertEquals(s.getTile(1, 0), Tile.Trophy);
        Exception e1 = assertThrows(BadPositionException.class,() -> s.insertTile(Tile.Invalid,0));
        assertEquals("Error while inserting in Shelf : Tile is Invalid type", e1.getMessage());
        Exception e2 =assertThrows(BadPositionException.class, () -> s.insertTile(Tile.Trophy,3));
        assertEquals("Error while inserting in Shelf : selected column is already full", e2.getMessage());
        Exception e3 =assertThrows(BadPositionException.class, () -> s.insertTile(Tile.Trophy,-1));
        assertEquals("Error while inserting in Shelf : selected column is not valid", e3.getMessage());
        Exception e4 =assertThrows(ShelfRuntimeException.class, () -> s.insertTile(null,4));
        assertEquals("Error while inserting tile : Tile type doesn't exists", e4.getMessage());


    }
    @Test
    void shelfEqualsTest() throws BadPositionException {
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
    void getTileTest() throws JsonBadParsingException, BadPositionException {
        Shelf s = new Shelf("src/test/resources/ShelfTests/ShelfGenericTest.json");
        assertEquals(Tile.Trophy, s.getTile(3,0));
        Exception e1 = assertThrows(BadPositionException.class, () -> s.getTile(-1,0));
        assertEquals("Error while getting Tile in Shelf : Coordinates are beyond boundaries", e1.getMessage());
        Exception e2 = assertThrows(ShelfRuntimeException.class, () -> s.getTile(null));
        assertEquals("Error while getting Tile in Shelf : Position object is null pointer", e2.getMessage());
    }
    @Test
    void checkAdiacentTest() throws BadPositionException {
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
    void getHighestColumnTest() throws JsonBadParsingException, BadPositionException {
        Shelf s = new Shelf("src/test/resources/ShelfTests/ShelfGenericTest.json");
        assertEquals(s.getRows(), s.getHighestColumn());
        s.insertTile(Tile.Book, 2);
        assertEquals(s.getRows()-1, s.getHighestColumn());
    }
    @Test
    void JsonObjectTest() throws JsonBadParsingException, BadPositionException {
        JSONParser jsonParser = new JSONParser(); //initialize JSON parser
        Object obj = null; //acquire JSON object file
        try {
            obj = jsonParser.parse(new FileReader("src/test/resources/ShelfTests/ShelfInsert.json"));
        } catch (IOException | ParseException e) {
            fail();
        }
        JSONObject objShelf = (JSONObject) ((JSONObject) obj).get("shelf"); //acquire shelf object
        Shelf shelf = new Shelf(objShelf);
        assertEquals(4,shelf.getRows());
        assertEquals(4,shelf.getColumns());
        assertEquals(shelf.getTile(0,0),Tile.Cat);
    }
    @Test
    void validTile() throws JsonBadParsingException {
        Shelf s = new Shelf("src/test/resources/ShelfTests/ShelfGenericTest.json");
        assertTrue(s.isValidTile(0,0));
        assertFalse(s.isValidTile(0,2));
    }
    @Test
    void indexOutOfBoundsTest()  {
        Shelf shelf = new Shelf(5,6);
        assertThrows(BadPositionException.class,() -> shelf.getTile(-1,0));
    }
    @Test
    void rowTiles() throws JsonBadParsingException {
        Shelf s = new Shelf("src/test/resources/ShelfTests/ShelfGenericTest.json");
        List<Tile> t = s.allTilesInRow(0);
        List<Tile> l = new ArrayList<>();
        l.add(Tile.Cat);
        l.add(Tile.Cat);
        l.add(Tile.Empty);
        l.add(Tile.Book);
        assertEquals(t , l);

        Exception e = assertThrows(ShelfRuntimeException.class, ()->s.allTilesInRow(-1));
        assertEquals("Error while getting in allTilesInRow for Shelf", e.getMessage());

    }
    @Test
    void columnTiles() throws JsonBadParsingException {
        Shelf s = new Shelf("src/test/resources/ShelfTests/ShelfGenericTest.json");
        List<Tile> t = s.allTilesInColumn(0);
        List<Tile> l = new ArrayList<>();
        l.add(Tile.Cat);
        l.add(Tile.Trophy);
        l.add(Tile.Cat);
        l.add(Tile.Trophy);
        assertEquals(t , l);

        Exception e = assertThrows(ShelfRuntimeException.class, ()->s.allTilesInColumn(-1));
        assertEquals("Error while getting in allTilesInColumn for Shelf", e.getMessage());
    }
    @Test
    void cornersTiles() throws JsonBadParsingException {
        Shelf s = new Shelf("src/test/resources/ShelfTests/ShelfGenericTest.json");
        List<Tile> t = s.getCorners();
        List<Tile> l = new ArrayList<>();
        l.add(Tile.Cat);
        l.add(Tile.Book);
        l.add(Tile.Trophy);
        l.add(Tile.Cat);
        assertEquals(t , l);
    }
    @Test
    void gettersTest()  {
        Shelf shelf = new Shelf(5,6);
        assertEquals(6,shelf.getColumns());
        assertEquals(5,shelf.getRows());
    }

    @Test
    void toJsonTest() throws JsonBadParsingException {
        String jsonPath = "src/test/resources/CommonGoalTests/TestShelf_1_2Squares.json";
        JSONObject jsonShelf = Shelf.pathToJSONObject(jsonPath);
        Shelf shelf = new Shelf(jsonPath);

        assertEquals(jsonShelf.toJSONString(), shelf.toJson().toJSONString());


    }
}
