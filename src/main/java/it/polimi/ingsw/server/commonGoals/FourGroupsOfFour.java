package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FourGroupsOfFour extends AbstractCommonGoal {
    public FourGroupsOfFour(int number_of_players){
        super(number_of_players);
    }
    public FourGroupsOfFour(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public int getID() {
        return 11;
    }
    @Override
    //TODO implement check
    public boolean check(Shelf shelf) {
        return true;
    }
}
