package it.polimi.ingsw.server;

import it.polimi.ingsw.server.commonGoals.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
            active_goals.add(create_goal_with_ID(i+1,number_of_players));
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
        result[0] = rand.nextInt(max);
        result[1] = result[0];
        while(result[1] == result[0]){
            result[1] = rand.nextInt(max);
        }
        return result;
    }

    /**
     * Creates a common goal from the id and the number of players
     * @param id of the common goal
     * @param number_of_players
     * @return the generated commongoal
     */
    public static CommonGoal create_goal_with_ID(int id, int number_of_players) {
        Object obj = null;
        try {
            JSONParser jsonParser = new JSONParser(); //initialize parser
            switch (number_of_players) {
                case (2):
                    obj = jsonParser.parse(new FileReader("src/main/resources/Two_Players_CommonGoal.json")); //acquire JSON object file
                    break;
                case (3):
                    obj = jsonParser.parse(new FileReader("src/main/resources/Three_Players_CommonGoal.json")); //acquire JSON object file
                    break;
                case (4):
                    obj = jsonParser.parse(new FileReader("src/main/resources/Four_Players_CommonGoal.json")); //acquire JSON object file
                    break;
                default:
                    throw new CommonGoalsException("Error while creating CommonGoal: Invalid number of Players");
            }

        } catch (FileNotFoundException e) {
            throw new CommonGoalsException("Error while creating CommonGoal : json file not found");
        } catch (IOException | ClassCastException | NullPointerException e) {
            throw new CommonGoalsException("Error while creating CommonGoal : bad json parsing");
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) ((JSONObject) obj).get("CommonGoal"); //acquire board object
        JSONArray stackFromJson = (JSONArray) jsonObject.get("stack");
        List<Integer> stackState = (List<Integer>) stackFromJson.stream().map(x -> Math.toIntExact((Long) x)).collect(Collectors.toList());
        return create_goal_with_ID(id, stackState);
    }

    /**
     * Creates a common goal from the id and a list of integer
     * @param ID of the common goal
     * @param stackState a list of integers to populate the stack
     * @return the generated common goal
     */
    private static CommonGoal create_goal_with_ID(int ID, List<Integer> stackState){
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
    public static CommonGoal create_from_json(JSONObject jsonObject){
        int id = Math.toIntExact((Long) jsonObject.get("id"));
        JSONArray stackFromJson = (JSONArray) jsonObject.get("stack");
        List<Integer> stackState = (List<Integer>) stackFromJson.stream().map(x -> Math.toIntExact((Long)x)).collect(Collectors.toList());
        return create_goal_with_ID(id,stackState);
    }
}
