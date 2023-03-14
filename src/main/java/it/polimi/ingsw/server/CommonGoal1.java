package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Shelf;

import java.util.Stack;

public class CommonGoal1 extends AbstractCommonGoal{
    @Override
    public boolean check(Shelf shelf) {
        return true;
    }

    public CommonGoal1(int number_of_players){
        points = new Stack<Integer>();
        populatePointsStack(number_of_players);
    }
}
