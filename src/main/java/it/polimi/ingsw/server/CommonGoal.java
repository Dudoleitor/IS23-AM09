package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class CommonGoal implements Jsonable{
    //VARIABLES
    /**
     * A Stack of points where players pop from when they complete CommonGoals
     */
    private final Stack<Integer> points;
    private final CommonGoalStrategy strategy;

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
        this.strategy = strategy;
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
     * @param strategy is the check static function
     * @param number_of_players talks by itself //TODO to change one day or the other
     */
    public CommonGoal(CommonGoalStrategy strategy, int number_of_players) throws JsonBadParsingException {
        this(getJsonObject(number_of_players,strategy));
    }

    /**
     * Extracts the id from the JsonObject
     * @param jsonObject is the ini object
     * @return the id
     */
    private static int getIdFromJson(JSONObject jsonObject){
        long id = (long) jsonObject.get("id");
        return Math.toIntExact(id);
    }
    /**
     * Extracts the stack state from the JsonObject
     * @param jsonObject is the reload object
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
     * @param number_of_players's been created to be intuitive //TODO to change one day or the other
     * @param strategy is the check static function
     * @return a jsonObject with the required characteristics
     * @throws JsonBadParsingException when Json file is bad formatted
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
        //get a list of all the IDs
        List<Integer> allIDs= Arrays.stream(CommonGoalStrategy.values()).map(CommonGoalStrategy::getId).collect(Collectors.toList());
        //Select two random ids between the ones selected
        for(int i : pickTwoRandomNumbers(allIDs.size())){
            active_goals.add(
                    new CommonGoal(CommonGoalStrategy.findById(allIDs.get(i)), //select the i_th ID in the list
                            number_of_players));
        }
        return active_goals;
    }
    /** Picks two random numbers to generate the goals
     * @param max the first excluded number to generate
     * @return an Array containing the two randomly selected integers
     */
    static int[] pickTwoRandomNumbers(int max) { //max must be positive
        if(max < 0){
            throw new CommonGoalRuntimeException("Error in pickTwoRandomNumbers : max must be positive");
        }
        int[] result = new int[2];
        Random rand = new Random();
        result[0] = rand.nextInt(max);
        result[1] = result[0];
        while(result[1] == result[0]){
            result[1] = rand.nextInt(max);
        }
        return result;
    }

    //GETTERS
    /**
     * Returns the ID of the CommonGoal
     * @return CommonGoal ID (1-12)
     */
    public int getID(){
        return strategy.getId();
    }

    /**
     * Checks the condition of the CommonGoal
     * @param shelf the shelf of one of the players
     * @return TRUE if the condition of the CommonGoal is verified
     */
    public boolean check(Shelf shelf){
        return strategy.getCheck().test(shelf);
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

    public String toString(){
        String str = Color.coloredString(strategy.name(),Color.Yellow);
        str = str.concat(": ");
        for(Integer points : showPointsStack()){
            str = str.concat(points + " ");
        }
        str = str.concat("\n");
        return str;
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