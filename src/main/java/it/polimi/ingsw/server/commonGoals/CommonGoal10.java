package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.Stack;

public class CommonGoal10 extends AbstractCommonGoal {
    public CommonGoal10(int number_of_players){
        points = new Stack<>();
        populatePointsStack(number_of_players);
    }
    @Override
    public int getID() {
        return 10;
    }
    @Override
    protected boolean check(Shelf shelf) {
        return true;
    }
}
