package it.polimi.ingsw.shared;

import it.polimi.ingsw.server.CommonGoalsException;
import it.polimi.ingsw.server.PartialMove;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    void boardConstructor() throws CommonGoalsException, IOException, ParseException, BoardGenericException {
        Board t1 = new Board(2);
        Board t2 = new Board(3);
        Board b1 = new Board(4);
        Board b2 = new Board(Board.pathToJsonObject("src/test/resources/BoardTests/BoardTestPlayerIni.json"), null);
        assertEquals(b1,b2);

        Exception e1 = assertThrows(BoardGenericException.class, () -> new Board(-1));
        assertEquals(e1.getMessage(), "Error while creating Board : invalid number of players");
        Exception e2 = assertThrows(BoardGenericException.class, () -> new Board(Board.pathToJsonObject("src/test/resources/BoardTests/WrongPath.json"), null));
        assertEquals(e2.getMessage(), "Error while generating Board from Json : file was not found");
        Exception e3 = assertThrows(BoardGenericException.class, () -> new Board(Board.pathToJsonObject("src/test/resources/BoardTests/BoardBadConfiguration.json"),null));
        assertEquals(e3.getMessage(), "Error while creating board : board is not a valid configuration for given num player");
        Exception e4 = assertThrows(BoardGenericException.class, () -> new Board(Board.pathToJsonObject("src/test/resources/BoardTests/BoardBadJsonFile.json"),null));
        assertEquals(e4.getMessage(), "Error while creating Board : bad json parsing");
    }
    @Test
    void testFill() throws CommonGoalsException, BoardGenericException, IOException, ParseException, OutOfTilesException {
        Board b1 = new Board(4);
        Board b2 = new Board(Board.pathToJsonObject("src/test/resources/BoardTests/BoardFillLowTilesTest.json"), null);

        b1.fill();
        boolean noEmptyFound = true;
        for(int i = 0; noEmptyFound && i < b1.getNumRows(); i++){
            for(int j = 0; noEmptyFound && j < b1.getNumColumns(); j++){
                if(b1.getTile(i,j).equals(Tile.Empty)){
                    noEmptyFound = false;
                }
            }
        }
        assertTrue(noEmptyFound);

        Exception e = assertThrows(OutOfTilesException.class, () -> b2.fill());
        assertEquals(e.getMessage(), "The END is near : No more tiles left in the deck");
    }

    @Test
    void getterTest() throws CommonGoalsException, IOException, ParseException, BoardGenericException {
        Board b = new Board(3);
        assertEquals(3, b.getNumPlayers());
        assertEquals(9, b.getNumColumns());
        assertEquals(9, b.getNumRows());
    }
    @Test
    void tileGetPickTest() throws BoardGenericException, CommonGoalsException {
        Board b = new Board(Board.pathToJsonObject("src/test/resources/BoardTests/BoardGenericTest.json"), null);

        assertEquals(Tile.Trophy, b.getTile(new Position(4,0)));
        Exception e1 = assertThrows(BoardGenericException.class, () -> b.getTile(new Position(-1, 0)));
        assertEquals(e1.getMessage(), "Error while getting tile from Board : illegal coordinates");
        Exception e2 = assertThrows(BoardGenericException.class, () -> b.getTile(null));
        assertEquals(e2.getMessage(), "Error while getting tile: pos is null pointer");

        Tile t = b.pickTile(4,0);
        assertEquals(Tile.Trophy, t);
        assertEquals(Tile.Empty, b.getTile(new Position(4,0)));
        Exception e3 = assertThrows(BoardGenericException.class, () -> b.pickTile(new Position(-1, 0)));
        assertEquals(e3.getMessage(), "Error while picking tile from Board : illegal coordinates");
        Exception e4 = assertThrows(BoardGenericException.class, () -> b.pickTile(null));
        assertEquals(e4.getMessage(), "Error while picking tile: pos is null pointer");
        Exception e5 = assertThrows(BoardGenericException.class, () -> b.pickTile(new Position(0,0)));
        assertEquals(e5.getMessage(), "Error while picking Tile : Chosen one is Invalid type");
    }
    @Test
    void getValidPositionsTest() throws BoardGenericException, CommonGoalsException {
        Board b = new Board(Board.pathToJsonObject("src/test/resources/BoardTests/BoardGenericTest.json"), null);
        assertTrue(b.hasFreeSide(4,0));
        assertTrue(b.hasFreeSide(5,5));
        assertFalse(b.hasFreeSide(4,1));

        List<Position> p = new ArrayList<>();
        for(int j = 1; j< 9; j++) {
            p.add(new Position(3,j));
        }
        p.add(new Position(4,0));
        p.add(new Position(4,8));

        for(int j = 0; j< 8; j++) {
            p.add(new Position(5,j));
        }

        assertEquals(p, b.getValidPositions(new PartialMove())); //TODO to complete after finished method
    }


    @Test
    void toJsonTest1() throws CommonGoalsException, IOException, ParseException, BoardGenericException {
        String jsonPath = Board.boardPathForNumberOfPlayers(2);
        JSONObject jsonBoardExpected = Board.pathToJsonObject(jsonPath);
        Board board = new Board(2);

        JSONObject jsonBoardToTest = board.toJson();
        jsonBoardExpected.remove("deck");  // Ignoring deck as it's randomly generated
        jsonBoardToTest.remove("deck");

        assertEquals(jsonBoardExpected.toJSONString(), jsonBoardToTest.toJSONString());
    }
    @Test
    void toJsonTest2() throws CommonGoalsException, IOException, ParseException, BoardGenericException {
        String jsonPath = "src/test/resources/BoardTests/BoardTestInsert.json";
        JSONObject jsonBoard = Board.pathToJsonObject(jsonPath);
        Board board = new Board(jsonBoard, new ArrayList<>());

        assertEquals(jsonBoard.toJSONString(), board.toJson().toJSONString());
    }
}

