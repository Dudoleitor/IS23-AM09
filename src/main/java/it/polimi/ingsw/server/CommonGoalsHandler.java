package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Shelf;

import java.util.ArrayList;
import java.util.Random;

public class CommonGoalsHandler {
    static final ArrayList<AbstractCommonGoal> all_goals = new ArrayList<>();
    AbstractCommonGoal firstGoal;
    AbstractCommonGoal secondGoal;
    public int giveAllPoints(Shelf shelf){
        return firstGoal.givePoints(shelf) + secondGoal.givePoints(shelf);
    }
    public CommonGoalsHandler(int number_of_players){
        if(all_goals.size() == 0){
            initiate_all_goals(number_of_players);
        }
        int[] randomInts = pickTwoRandomNumbers();
        firstGoal = all_goals.get(randomInts[0]);
        secondGoal = all_goals.get(randomInts[1]);
    }
    private int[] pickTwoRandomNumbers(){
        int[] result = new int[2];
        Random rand = new Random();
        result[0] = rand.nextInt(all_goals.size());
        result[1] = result[0];
        while(result[1] == result[0]){
            result[1] = rand.nextInt(all_goals.size());
        }
        return result;
    }
    private void initiate_all_goals(int number_of_players){
        all_goals.add(new CommonGoal1(number_of_players));
        all_goals.add(new CommonGoal2(number_of_players));
        all_goals.add(new CommonGoal3(number_of_players));
        all_goals.add(new CommonGoal4(number_of_players));
        all_goals.add(new CommonGoal5(number_of_players));
        all_goals.add(new CommonGoal6(number_of_players));
        all_goals.add(new CommonGoal7(number_of_players));
        all_goals.add(new CommonGoal8(number_of_players));
        all_goals.add(new CommonGoal9(number_of_players));
        all_goals.add(new CommonGoal10(number_of_players));
        all_goals.add(new CommonGoal11(number_of_players));
        all_goals.add(new CommonGoal12(number_of_players));
    }
}
