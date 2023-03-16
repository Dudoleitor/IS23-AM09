package it.polimi.ingsw.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board = new Board(9,9,2);
    Position pos1 = new Position(3,4);
    Position pos2 = new Position(1, 2);
    Board board1 = new Board(9,9,3);
    Board board2 = new Board(9,9,4);


    @Test
    void getTile() {
        assertEquals(Tile.Empty,board.getTile(pos1));
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

}