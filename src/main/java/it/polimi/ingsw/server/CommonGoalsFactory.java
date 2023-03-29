package it.polimi.ingsw.server;

import it.polimi.ingsw.server.commonGoals.*;
import it.polimi.ingsw.shared.Constants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CommonGoalsFactory {
    static final int number_of_goals = 12;

    /**
     * Generates two distinct common goals for the match
     * @param number_of_players the number of players
     * @return an Arraylist containig the two randomly selected goals
     * @throws CommonGoalsException
     */
    public static ArrayList<CommonGoal> createTwoGoals(int number_of_players) throws CommonGoalsException{
        ArrayList<CommonGoal> active_goals = new ArrayList<>();
        for(int i : pickTwoRandomNumbers(number_of_goals)){
            active_goals.add(create_goal_with_ID(i,number_of_players));
        }
        return active_goals;
    }

    /**
     * Picks two random numbers to generate the goals
     * @param max the maximum accepted random number to generate
     * @return an Array containing the two randomly selected integers
     * @throws CommonGoalsException
     */
    static int[] pickTwoRandomNumbers(int max) throws CommonGoalsException{ //max must be positive
        if(max < 0){
            throw new CommonGoalsException("Error in pickTwoRandomNumbers : max must be positive");
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

    /**
     * Creates a common goal from the id and the number of players
     * @param id of the common goal
     * @param number_of_players
     * @return the generated commongoal
     */
    public static CommonGoal create_goal_with_ID(int id, int number_of_players) throws CommonGoalsException {
        JSONObject jsonObject;
        String path;
        if(number_of_players <= Constants.maxSupportedPlayers &&
                number_of_players >= Constants.minSupportedPlayers){
            path = Constants.jsonPathForCommonGoals.replace("?",String.valueOf(number_of_players));
        }
        else{
            throw new CommonGoalsException("Error while creating CommonGoal: Invalid number of Players");
        }
        jsonObject = pathToJsonObject(path);
        jsonObject.put("id", Long.valueOf(id));
        return create_from_json(jsonObject);
    }

    /**
     * Creates a common goal from the id and a list of integer
     * @param ID of the common goal
     * @param stackState a list of integers to populate the stack
     * @return the generated common goal
     */
    private static CommonGoal create_goal_with_ID(int ID, List<Integer> stackState) throws CommonGoalsException {
        CommonGoal newGoal;
        switch (ID){
            case(1):
                newGoal = new SixGroupsOf2(stackState);
                break;
            case(2):
                newGoal = new TwoSquares(stackState);
                break;
            case(3):
                newGoal = new EqualTilesInAllCorners(stackState);
                break;
            case(4):
                newGoal = new ThreeColumnsWith3Types(stackState);
                break;
            case(5):
                newGoal = new TwoAllDifferentColumns(stackState);
                break;
            case(6):
                newGoal = new EightEqualTiles(stackState);
                break;
            case(7):
                newGoal = new Ladders(stackState);
                break;
            case(8):
                newGoal = new FourLineWith3Types(stackState);
                break;
            case(9):
                newGoal = new TwoAllDifferentLines(stackState);
                break;
            case(10):
                newGoal = new FullLadder(stackState);
                break;
            case(11):
                newGoal = new FourGroupsOfFour(stackState);
                break;
            case(12):
                newGoal = new EqualX(stackState);
                break;
            default:
                throw new CommonGoalsException("Error while creating CommonGoal: Invalid Id");
        }
        return newGoal;
    }

    /**
     * Loads a common goal from a JSON object
     * @param jsonObject the serialized object
     * @return the generated common goal
     */
    public static CommonGoal create_from_json(JSONObject jsonObject) throws CommonGoalsException {
        try {
            int id = Math.toIntExact((Long) jsonObject.get("id"));
            JSONArray stackFromJson = (JSONArray) jsonObject.get("stack");
            List<Integer> stackState = (List<Integer>) stackFromJson.stream().map(x -> Math.toIntExact((Long) x)).collect(Collectors.toList());
            return create_goal_with_ID(id, stackState);
        } catch (ClassCastException | NullPointerException e) {
            throw new CommonGoalsException("Error while creating CommonGoal : bad json parsing");
        }
    }
    public static JSONObject pathToJsonObject(String jsonPath) throws CommonGoalsException {
        Object obj = null;
        try {
            JSONParser jsonParser = new JSONParser(); //initialize parser
            obj = jsonParser.parse(new FileReader(jsonPath)); //acquire JSON object file
            JSONObject jsonObject = (JSONObject) ((JSONObject) obj).get("CommonGoal"); //get CommonGoal
            return jsonObject;
        } catch (IOException | ClassCastException | NullPointerException | ParseException e) {
            throw new CommonGoalsException("Error while creating CommonGoal : bad json parsing");
        }
    }
}
