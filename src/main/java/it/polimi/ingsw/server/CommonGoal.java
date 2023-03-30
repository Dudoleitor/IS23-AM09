package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommonGoal implements Jsonable{
    //VARIABLES
    /**
     * A Stack of points where players pop from when they complete CommonGoals
     */
    private Stack<Integer> points;
    private int id;
    private Predicate<Shelf> check;

    //CONSTRUCTORS
    /**
     * Construcor that initializes a CommonGoal with a pre-determined stack State and Strategy.
     * This constructor is called by all others to actually build the CommonGoal
     * @param stackState a List of integers that will form the stack state
     */
    private CommonGoal(CommonGoalStrategy strategy, List<Integer> stackState){
        points = new Stack<>();
        for(Integer i : stackState){
            points.push(i);
        }
        id = strategy.getId();
        check = strategy.getCheck();
    }
    /**
     * Loads a common goal from a JSON object
     * @param jsonObject the serialized object
     */
    public CommonGoal(JSONObject jsonObject){
        this(CommonGoalStrategy.findById(getIdFromJson(jsonObject)),
                getStackStateFromJson(jsonObject));
    }
    /**
     * Cretes a CommonGoal with a specified id for a specified number of players
     * @param strategy
     * @param number_of_players
     */
    public CommonGoal(CommonGoalStrategy strategy, int number_of_players) throws JsonBadParsingException {
        this(getJsonObject(number_of_players,strategy));
    }

    /**
     * Extracts the id from the JsonObject
     * @param jsonObject
     * @return the id
     */
    private static int getIdFromJson(JSONObject jsonObject){
        long id = (long) jsonObject.get("id");
        return Math.toIntExact(id);
    }
    /**
     * Extracts the stack state from the JsonObject
     * @param jsonObject
     * @return a List of Integers representing the stack state
     */
    private static List<Integer> getStackStateFromJson(JSONObject jsonObject){
        try {
            JSONArray stackFromJson = (JSONArray) jsonObject.get("stack");
            return (List<Integer>) stackFromJson.stream().map(x -> Math.toIntExact((Long) x)).collect(Collectors.toList());
        } catch (NullPointerException e){
            throw new CommonGoalRuntimeException("Error while creating CommonGoal : file Json not found");
        }
    }

    /**
     * @param number_of_players
     * @param strategy
     * @return a jsonObject with the required characteristics
     * @throws JsonBadParsingException
     */
    private static JSONObject getJsonObject(int number_of_players, CommonGoalStrategy strategy) throws JsonBadParsingException {
        JSONObject jsonObject;
        String path;
        if(number_of_players <= Constants.maxSupportedPlayers &&
                number_of_players >= Constants.minSupportedPlayers){
            path = Constants.jsonPathForCommonGoals.replace("?",String.valueOf(number_of_players));
        }
        else{
            throw new CommonGoalRuntimeException("Error while creating CommonGoal: Invalid number of Players");
        }
        jsonObject = Jsonable.pathToJsonObject(path,CommonGoal.class);
        jsonObject.put("id",(long) strategy.getId());
        return jsonObject;
    }

    /**
     * Generates two distinct common goals for the match
     * @param number_of_players the number of players
     * @return an Arraylist containig the two randomly selected goals
     */
    public static ArrayList<CommonGoal> createTwoGoals(int number_of_players) throws JsonBadParsingException {
        ArrayList<CommonGoal> active_goals = new ArrayList<>();
        for(int i : pickTwoRandomNumbers(CommonGoalStrategy.values().length)){
            active_goals.add(new CommonGoal(CommonGoalStrategy.findById(i),number_of_players));
        }
        return active_goals;
    }
    /** Picks two random numbers to generate the goals
     * @param max the maximum accepted random number to generate
     * @return an Array containing the two randomly selected integers
     */
    static int[] pickTwoRandomNumbers(int max) { //max must be positive
        if(max < 0){
            throw new CommonGoalRuntimeException("Error in pickTwoRandomNumbers : max must be positive");
        }
        int[] result = new int[2];
        Random rand = new Random();
        result[0] = rand.nextInt(max) + 1;
        result[1] = result[0];
        while(result[1] == result[0]){
            result[1] = rand.nextInt(max) + 1;
        }
        return result;
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
    public boolean check(Shelf shelf){
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