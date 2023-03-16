package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.Stack;

public class CommonGoal2 extends AbstractCommonGoal {
    @Override
    public int getID() {
        return 2;
    }
    public CommonGoal2(int number_of_players){
        points = new Stack<>();
        populatePointsStack(number_of_players);
    }
    @Override
    public boolean check(Shelf shelf) {
        return true;
    }
}
