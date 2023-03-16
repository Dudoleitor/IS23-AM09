package it.polimi.ingsw.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board = new Board(9,9,2);
    Position pos1 = new Position(3,4);

    @Test
    void getTile() {
        assertEquals(Tile.Empty,board.getTile(pos1));
    }

    @Test
    void pickTile() {
        assertEquals(Tile.Empty, board.pickTile(pos1));
    }

    @Test
    void initializeGoals() {
    }

    @Test
    void getCommonGoals() {
    }
}