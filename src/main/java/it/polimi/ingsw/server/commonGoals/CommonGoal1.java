package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.Stack;

public class CommonGoal1 extends AbstractCommonGoal {
    @Override
    public int getID() {
        return 1;
    }

    @Override
    public boolean check(Shelf shelf) {
        return true;
    }

    public CommonGoal1(int number_of_players){
        points = new Stack<Integer>();
        populatePointsStack(number_of_players);
    }
}
