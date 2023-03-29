package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    @Test
    void cloningTest() throws PartialMoveException, InvalidMoveException {
        PartialMove pm = new PartialMove();
        pm.addPosition(new Position(0,1));
        pm.addPosition(new Position(2,3));
        pm.addPosition(new Position(4,5));
        Move mo = new Move(pm,15);

        assertEquals(15,mo.getColumn());
        assertTrue(mo.getBoardPositions().containsAll(pm.getBoardPositions()));
        assertEquals(pm.getBoardPositions().get(0),mo.getBoardPositions().get(0));
        assertEquals(pm.getBoardPositions().get(2),mo.getBoardPositions().get(2));
    }
}