package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Position;

import java.util.ArrayList;
import java.util.List;

public class PartialMove {
    protected List<Position> positions;
    private final int maxNumMoves = 3;
    public List<Position> getBoardPositions(){
        return new ArrayList<>(positions);
    }
    public void addPosition(Position pos) throws PartialMoveException{
        if(pos == null){
            throw new PartialMoveException("Error while adding position : Pointer was Null");
        }
        positions.add(pos);
    }
    PartialMove(PartialMove to_clone) throws PartialMoveException{ //cloning constructor
        try {
            positions = new ArrayList<>(to_clone.getBoardPositions());
        } catch (NullPointerException e){
            throw new PartialMoveException("Error while cloning PartialMove  : to_clone is null pointer");
        }
    }
    public PartialMove(){ //default constructor
        positions = new ArrayList<>();
    }

    public int getMaxNumMoves() {
        return maxNumMoves;
    }
}
