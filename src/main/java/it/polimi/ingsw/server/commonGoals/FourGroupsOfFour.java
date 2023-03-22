package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.List;

public class FourGroupsOfFour extends CommonGoal {
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
