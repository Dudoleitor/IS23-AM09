package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.ShelfGenericException;
import it.polimi.ingsw.shared.Tile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.*;

public class CommonGoal {
    //VARIABLES
    /**
     * A Stack of points where players pop from when they complete CommonGoals
     */
    private Stack<Integer> points;
    private int id;
    private Predicate<Shelf> check;

    //CONSTRUCTORS
    /**
     * Construcor that initializes a CommonGoal with a pre-determined stack State
     * @param stackState a List of integers that will form the stack state
     */
    public CommonGoal(CommonGoalStrategy strategy, List<Integer> stackState){
        points = new Stack<>();
        for(Integer i : stackState){
            points.push(i);
        }
        id = strategy.getId();
        check = strategy.getCheck();
    }
    //GETTERS
    /**
     * Returns the ID of the CommonGoal
     * @return CommonGoal ID (1-12)
     */
    public int getID(){
        return id;
    }

    /**
     * Checks the condition of the CommonGoal
     * @param shelf the shelf of one of the players
     * @return TRUE if the condition of the CommonGoal is verified
     */
    public boolean check(Shelf shelf) throws ShelfGenericException, CommonGoalsException{
        return check.test(shelf);
    }

    /**
     * Shows the state of the Points Stack
     * @return the Points Stack as an ArrayList
     */
    public ArrayList<Integer> showPointsStack(){ //useful in debugging
        return new ArrayList<>(points);
    }

    //MODIFIERS
    /**
     * Pops points from PointStack
     * @return the points popped from the Stack
     */
    public int givePoints(){
        if(points.size() > 0){
            return points.pop();
        }
        else{
            return 0;
        }
    }

    //OTHERS
    /**
     * This method is used to save the status of the shelf with a json object.
     * @return JSONObject with status.
     */
    public JSONObject toJson() {
        JSONObject commonGoalJson = new JSONObject();  // Object to return
        JSONArray goalStack = new JSONArray(); //points pile

        for(long point : showPointsStack()){
            goalStack.add(point);
        }
        commonGoalJson.put("id", (long)getID());
        commonGoalJson.put("stack", goalStack);

        return commonGoalJson;
    }
    @Override
    public boolean equals(Object o){ //check they have same ID
        if(o == null)
            return false;
        else if (o == this)
            return true;

        CommonGoal c = (CommonGoal) o;
        if(c.getID() != this.getID())
            return false;
        return true;
    }
}