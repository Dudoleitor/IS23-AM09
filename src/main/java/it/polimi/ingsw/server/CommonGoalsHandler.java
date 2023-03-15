package it.polimi.ingsw.server;

import it.polimi.ingsw.server.commonGoals.*;
import it.polimi.ingsw.shared.Shelf;

import java.util.ArrayList;
import java.util.Random;

public class CommonGoalsHandler {
    ArrayList<AbstractCommonGoal> active_goals = new ArrayList<>();
    final int number_of_goals = 2;
    public int giveAllPoints(Shelf shelf){
        return active_goals.stream().mapToInt(x->x.givePoints(shelf)).sum();
    }

    int number_of_players;
    public CommonGoalsHandler(int number_of_players){
        this.number_of_players = number_of_players;
        int[] randomIDs = pickTwoRandomNumbers(number_of_goals);
        initiate_goals(randomIDs);
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
    private void initiate_goals(int[] pickedIDs){
        for(int i : pickedIDs){
            active_goals.add(create_goal_with_ID(i+1));
        }
    }

    private AbstractCommonGoal create_goal_with_ID(int ID){
        AbstractCommonGoal newGoal;
        switch (ID){
            case(1):
                newGoal = new CommonGoal1(number_of_players);
                break;
            case(2):
                newGoal = new CommonGoal2(number_of_players);
                break;
            case(3):
                newGoal = new CommonGoal3(number_of_players);
                break;
            case(4):
                newGoal = new CommonGoal4(number_of_players);
                break;
            case(5):
                newGoal = new CommonGoal5(number_of_players);
                break;
            case(6):
                newGoal = new CommonGoal6(number_of_players);
                break;
            case(7):
                newGoal = new CommonGoal7(number_of_players);
                break;
            case(8):
                newGoal = new CommonGoal8(number_of_players);
                break;
            case(9):
                newGoal = new CommonGoal9(number_of_players);
                break;
            case(10):
                newGoal = new CommonGoal10(number_of_players);
                break;
            case(11):
                newGoal = new CommonGoal11(number_of_players);
                break;
            default:
                newGoal = new CommonGoal12(number_of_players);
                break;
        }
        return newGoal;
    }
}
