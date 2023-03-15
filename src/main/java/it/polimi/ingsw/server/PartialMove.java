package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Position;

import java.util.ArrayList;
import java.util.List;

public class PartialMove {
    protected List<Position> positions;
    public List<Position> getBoardPositions(){
        return new ArrayList<>(positions);
    }
    public void addPosition(Position pos){
        positions.add(pos);
    }
    PartialMove(PartialMove to_clone){ //cloning constructor
        positions = new ArrayList<>(to_clone.getBoardPositions());
    }
    PartialMove(){ //default constructor
        positions = new ArrayList<>();
    }
}
