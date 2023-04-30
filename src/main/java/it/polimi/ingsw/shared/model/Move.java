package it.polimi.ingsw.shared.model;

import it.polimi.ingsw.shared.Jsonable;
import org.json.simple.JSONObject;

public class Move extends PartialMove implements Jsonable {
    private int column;
    public Move(PartialMove partial,int column) throws PartialMoveException {
        super(partial);
        this.column = column;
    }
    public Move(JSONObject jsonMove){
        super((JSONObject) jsonMove.get("positions"));
        this.column = Integer.parseInt(jsonMove.get("column").toString());

    }
    public int getColumn(){
        return column;
    }

    @Override
    public String toString(){
        return super.toString() + "Column: " + String.valueOf(column);
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonMove = new JSONObject();  // Object to return
        jsonMove.put("column", column);
        jsonMove.put("positions", super.toJson());

        return jsonMove;
    }
}
