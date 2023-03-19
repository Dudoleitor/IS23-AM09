package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.Stack;

public class TwoSquares extends AbstractCommonGoal {
    @Override
    public int getID() {
        return 2;
    }
    public TwoSquares(int number_of_players){
        points = new Stack<>();
        populatePointsStack(number_of_players);
    }
    @Override
    //TODO implement check
    public boolean check(Shelf shelf) {
        return true;
    }
}
