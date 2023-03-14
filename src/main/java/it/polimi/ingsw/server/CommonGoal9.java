package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Shelf;

import java.util.Stack;

public class CommonGoal9 extends AbstractCommonGoal{
    public CommonGoal9(int number_of_players){
        points = new Stack<>();
        populatePointsStack(number_of_players);
    }

    @Override
    protected boolean check(Shelf shelf) {
        return true;
    }
}
