package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Position;

import java.util.ArrayList;
import java.util.List;

public class Move extends PartialMove {
    private int column;
    public Move(PartialMove partial,int column) throws PartialMoveException {
        super(partial);
        this.column = column;
    }
    public int getColumn(){
        return column;
    }
}
