package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.Stack;

public class TwoAllDifferentLines extends AbstractCommonGoal {
    public TwoAllDifferentLines(int number_of_players){
        points = new Stack<>();
        populatePointsStack(number_of_players);
    }
    @Override
    public int getID() {
        return 9;
    }
    @Override
    protected boolean check(Shelf shelf) {
        int differentLines = 0;
        for (int row = 0; row < shelf.getRows(); row++) {
            if(notEmptyAndAllDifferent(allTilesInRow(shelf,row))){
                differentLines++;
            }
        }
        return differentLines >= 2;
    }
}
