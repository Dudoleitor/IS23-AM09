package it.polimi.ingsw.shared;

import java.util.ArrayList;
import java.util.List;

public class PartialMove {
    protected List<Position> positions;
    private final int maxNumMoves = 3;
    public List<Position> getBoardPositions(){
        return new ArrayList<>(positions);
    }
    public void addPosition(Position pos) throws InvalidMoveException {
        if(pos == null){
            throw new InvalidMoveException("Error while adding position : Pointer was Null");
        }
        positions.add(pos);
    }
    public PartialMove(PartialMove to_clone) throws PartialMoveException { //cloning constructor
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
    @Override
    public String toString(){
        String s = "Positions:\n";
        for(Position p : positions){
            s = s.concat(p+"\n");
        }
        return s;
    }
}