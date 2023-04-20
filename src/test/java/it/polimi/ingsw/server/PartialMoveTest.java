package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.InvalidMoveException;
import it.polimi.ingsw.shared.PartialMove;
import it.polimi.ingsw.shared.PartialMoveException;
import it.polimi.ingsw.shared.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartialMoveTest {
    @Test
    void PositionsTest() throws PartialMoveException, InvalidMoveException {
        PartialMove pm = new PartialMove();
        pm.addPosition(new Position(0,1));
        pm.addPosition(new Position(2,3));
        pm.addPosition(new Position(4,5));

        assertEquals(3,pm.getBoardPositions().size());
        assertEquals(0,pm.getBoardPositions().get(0).getRow());
        assertEquals(5,pm.getBoardPositions().get(2).getColumn());
    }

    @Test
    void cloningConstructorTest() throws InvalidMoveException {
        PartialMove pm = new PartialMove();
        pm.addPosition(new Position(0,1));
        pm.addPosition(new Position(2,3));
        pm.addPosition(new Position(4,5));

        PartialMove clone = new PartialMove(pm);
        assertEquals(3,pm.getBoardPositions().size());
        assertEquals(0,pm.getBoardPositions().get(0).getRow());
        assertEquals(5,pm.getBoardPositions().get(2).getColumn());

        assertEquals(3,clone.getBoardPositions().size());
        assertEquals(0,clone.getBoardPositions().get(0).getRow());
        assertEquals(5,clone.getBoardPositions().get(2).getColumn());

        assertTrue(clone.getBoardPositions().containsAll(pm.getBoardPositions()));
    }

    @Test
    void nullPointerExceptionTest(){
        PartialMove pm = new PartialMove();
        assertThrows(InvalidMoveException.class, () -> pm.addPosition(null));
        assertThrows(PartialMoveException.class, () -> new PartialMove((PartialMove) null));

    }

    @Test
    void correctConstantTest(){
        PartialMove pm = new PartialMove();
        assertEquals(3,pm.getMaxNumMoves());
    }
}