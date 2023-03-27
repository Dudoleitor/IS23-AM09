package it.polimi.ingsw.shared;

import it.polimi.ingsw.server.CommonGoalsException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Position pos1 = new Position(3,4);
    Position pos2 = new Position(1, 2);
    Board board;

    {
        try {
            board = new Board(3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (BoardGenericException e) {
            throw new RuntimeException(e);
        } catch (CommonGoalsException e) {
            throw new RuntimeException(e);
        }
    }
    Board board1;
    {
        try {
            board1 = new Board(3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (BoardGenericException e) {
            throw new RuntimeException(e);
        } catch (CommonGoalsException e) {
            throw new RuntimeException(e);
        }
    }

    Board board2;
    {
        try {
            board2 = new Board(4);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (BoardGenericException e) {
            throw new RuntimeException(e);
        } catch (CommonGoalsException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void getTile() throws BoardGenericException {
        assertEquals(Tile.Empty,board.getTile(pos1));
    }

    @Test
    void getTile2(){
        String jsonPath = "src/test/resources/BoardTests/boardTestInsert.json";
        try {
            Board jsonBoard = new Board(Board.pathToJsonObject(jsonPath),new ArrayList<>());
        } catch (IOException e) {
            fail();
            throw new RuntimeException(e);
        } catch (ParseException e) {
            fail();
            throw new RuntimeException(e);
        } catch (BoardGenericException e) {
            throw new RuntimeException(e);
        } catch (CommonGoalsException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void pickTile() throws BoardGenericException {
        assertEquals(Tile.Empty, board.pickTile(pos1));
    }

    @Test
    void pickTile1() throws BoardGenericException {
        assertEquals(Tile.Empty, board1.pickTile(pos1));
    }

    @Test
    void pickTile2() throws BoardGenericException {
        assertEquals(Tile.Invalid, board2.pickTile(pos2));
    }

    @Test
    void boardFiller(){
        try{
            String jsonPath = "src/test/resources/BoardTests/boardTestInsert.json";
            Board b = new Board(Board.pathToJsonObject(jsonPath),new ArrayList<>());
            b.fill();
        }
        catch (IOException e) {
            fail();
            throw new RuntimeException(e);
        } catch (ParseException e) {
            fail();
            throw new RuntimeException(e);
        } catch (BoardGenericException e) {
            throw new RuntimeException(e);
        } catch (OutOfTilesException e) {
            throw new RuntimeException(e);
        } catch (CommonGoalsException e) {
            throw new RuntimeException(e);
        }
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
        String jsonPath = "src/test/resources/BoardTests/boardTestInsert.json";
        JSONObject jsonBoard = Board.pathToJsonObject(jsonPath);
        Board board = new Board(jsonBoard, new ArrayList<>());

        assertEquals(jsonBoard.toJSONString(), board.toJson().toJSONString());
    }
}
