package it.polimi.ingsw.shared.model;

import it.polimi.ingsw.shared.Jsonable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PartialMove implements Jsonable {
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

    public PartialMove(JSONObject jsonMove){
        JSONArray posArray = (JSONArray) jsonMove.get("posArray");
        positions = new ArrayList<>();
        posArray.stream().
                forEach(p -> positions.add(new Position((JSONObject) p)));
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

    @Override
    public JSONObject toJson() {
        JSONObject jsonMove = new JSONObject();  // Object to return
        JSONArray jsonPositions = new JSONArray();
        // Saving parameters
        positions.stream()
                .forEach(p -> jsonPositions.add(p.toJson()));
        jsonMove.put("posArray",jsonPositions);

        return jsonMove;
    }
}
