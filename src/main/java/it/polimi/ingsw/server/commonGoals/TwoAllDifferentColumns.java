package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.Stack;

public class TwoAllDifferentColumns extends AbstractCommonGoal {
    public TwoAllDifferentColumns(int number_of_players){
        points = new Stack<>();
        populatePointsStack(number_of_players);
    }
    @Override
    public int getID() {
        return 5;
    }
    @Override
    protected boolean check(Shelf shelf) {
        int differentColumns = 0;
        for (int column = 0; column < shelf.getColumns(); column++) {
            if(notEmptyAndAllDifferent(allTilesInColum(shelf,column))){
                differentColumns++;
            }
        }
        return differentColumns >= 2;
    }
}
