package it.polimi.ingsw.server;

import it.polimi.ingsw.server.commonGoals.*;

import java.util.ArrayList;
import java.util.Random;

public class CommonGoalsFactory {
    static final int number_of_goals = 12;

    public ArrayList<AbstractCommonGoal> createTwoGoals(int number_of_players){
        ArrayList<AbstractCommonGoal> active_goals = new ArrayList<>();
        for(int i : pickTwoRandomNumbers(number_of_goals)){
            active_goals.add(create_goal_with_ID(i+1,number_of_players));
        }
        return active_goals;
    }
    private int[] pickTwoRandomNumbers(int max){
        int[] result = new int[2];
        Random rand = new Random();
        result[0] = rand.nextInt(max);
        result[1] = result[0];
        while(result[1] == result[0]){
            result[1] = rand.nextInt(max);
        }
        return result;
    }

    public static AbstractCommonGoal create_goal_with_ID(int ID, int number_of_players){
        AbstractCommonGoal newGoal;
        switch (ID){
            case(1):
                newGoal = new SixGroupsOf2(number_of_players);
                break;
            case(2):
                newGoal = new TwoSquares(number_of_players);
                break;
            case(3):
                newGoal = new EqualsTilesInAllCorners(number_of_players);
                break;
            case(4):
                newGoal = new ThreeColumnsWith3Types(number_of_players);
                break;
            case(5):
                newGoal = new TwoAllDifferentColumns(number_of_players);
                break;
            case(6):
                newGoal = new EightEqualTiles(number_of_players);
                break;
            case(7):
                newGoal = new Ladders(number_of_players);
                break;
            case(8):
                newGoal = new FourLineWith3Types(number_of_players);
                break;
            case(9):
                newGoal = new TwoAllDifferentLines(number_of_players);
                break;
            case(10):
                newGoal = new FullLadder(number_of_players);
                break;
            case(11):
                newGoal = new FourGroupsOfFour(number_of_players);
                break;
            default:
                newGoal = new EqualX(number_of_players);
                break;
        }
        return newGoal;
    }
}
