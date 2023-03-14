package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Shelf;

import java.util.Stack;

public class CommonGoal2 extends AbstractCommonGoal{
    public CommonGoal2(int number_of_players){
        points = new Stack<>();
        populatePointsStack(number_of_players);
    }
    @Override
    public boolean check(Shelf shelf) {
        return true;
    }
}
