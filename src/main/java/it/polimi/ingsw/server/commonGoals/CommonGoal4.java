package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.Stack;

public class CommonGoal4 extends AbstractCommonGoal {
    public CommonGoal4(int number_of_players){
        points = new Stack<>();
        populatePointsStack(number_of_players);
    }
    @Override
    public int getID() {
        return 4;
    }
    @Override
    protected boolean check(Shelf shelf) {
        return true;
    }
}
