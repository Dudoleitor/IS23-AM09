package it.polimi.ingsw.shared;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
        }
    }


    @Test
    void getTile() {
        assertEquals(Tile.Empty,board.getTile(pos1));
    }

    @Test
    void getTile2(){
        String jsonPath = "src/test/resources/BoardTests/boardTestInsert.json";
        try {
            Board jsonBoard = new Board(jsonPath);
        } catch (IOException e) {
            fail();
            throw new RuntimeException(e);
        } catch (ParseException e) {
            fail();
            throw new RuntimeException(e);
        }
    }

    @Test
    void pickTile() {
        assertEquals(Tile.Empty, board.pickTile(pos1));
    }

    @Test
    void pickTile1() {
        assertEquals(Tile.Empty, board1.pickTile(pos1));
    }

    @Test
    void pickTile2() {
        assertEquals(Tile.Invalid, board2.pickTile(pos2));
    }

    @Test
    void boardFiller(){
        try{
            String jsonPath = "src/test/resources/BoardTests/boardTestInsert.json";
            Board b = new Board(jsonPath);
            b.fill();
        }
        catch (IOException e) {
            fail();
            throw new RuntimeException(e);
        } catch (ParseException e) {
            fail();
            throw new RuntimeException(e);
        }
    }

}
