package it.polimi.ingsw.shared;

import it.polimi.ingsw.server.CommonGoalsException;
import it.polimi.ingsw.server.PartialMove;
import it.polimi.ingsw.server.PartialMoveException;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Position pos1 = new Position(3, 4);
    Position pos2 = new Position(1, 5);
    Board board;
    PartialMove partialMove = new PartialMove();

    {
        try {
            board = new Board(3);
        } catch (BoardGenericException e) {
            throw new RuntimeException(e);
        }
    }

    Board board1;

    {
        try {
            board1 = new Board(3);
        } catch (BoardGenericException e) {
            throw new RuntimeException(e);
        }
    }

    Board board2;

    {
        try {
            board2 = new Board(4);
        } catch (BoardGenericException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void getTile() throws BoardGenericException {
        assertEquals(Tile.Empty, board.getTile(pos1));
    }

    @Test
    void getTile2() {
        String jsonPath = "src/test/resources/BoardTests/boardTestInsert.json";
        try {
            Board jsonBoard = new Board(Board.pathToJsonObject(jsonPath), new ArrayList<>());
        } catch (BoardGenericException e) {
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
        assertEquals(Tile.Empty, board2.pickTile(pos2));
    }

    @Test
    void boardFiller() {
        try {
            String jsonPath = "src/test/resources/BoardTests/boardTestInsert.json";
            Board b = new Board(Board.pathToJsonObject(jsonPath), new ArrayList<>());
            b.fill();
        } catch (BoardGenericException e) {
            throw new RuntimeException(e);
        } catch (OutOfTilesException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void getValidPositionsTest() {
        try {
            //partialMove.addPosition(pos3);
            String jsonPath = "src/test/resources/BoardTests/boardTestInsert.json";
            Board b = new Board(Board.pathToJsonObject(jsonPath), new ArrayList<>());
            b.fill();
            Position pos2 = new Position(0, 3);


            assertTrue(b.getValidPositions(partialMove).contains(pos2));


        } catch (BoardGenericException e) {
            throw new RuntimeException(e);
        } catch (OutOfTilesException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void getValidPositionsTest2() {
        try {
            String jsonPath = "src/test/resources/BoardTests/boardTestInsert.json";
            Board b = new Board(Board.pathToJsonObject(jsonPath), new ArrayList<>());
            b.fill();
            Position pos1 = new Position(8, 5);
            Position pos2 = new Position(7, 5);
            partialMove.addPosition(pos1);
            partialMove.addPosition(pos2);
            Position pos3 = new Position(6, 5);


            assertFalse(b.getValidPositions(partialMove).contains(pos3));


        } catch (BoardGenericException e) {
            throw new RuntimeException(e);
        } catch (OutOfTilesException e) {
            throw new RuntimeException(e);
        } catch (PartialMoveException e) {
            e.printStackTrace();
        }
    }

}
