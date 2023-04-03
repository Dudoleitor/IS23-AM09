package it.polimi.ingsw.server;

public class Move extends PartialMove {
    private int column;
    public Move(PartialMove partial,int column) throws PartialMoveException {
        super(partial);
        this.column = column;
    }
    public int getColumn(){
        return column;
    }

    @Override
    public String toString(){
        return super.toString() + "Column: " + String.valueOf(column);
    }
}
