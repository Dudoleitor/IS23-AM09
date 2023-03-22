package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.List;

public class TwoAllDifferentColumns extends CommonGoal {
    public TwoAllDifferentColumns(List<Integer> stackState){
            super(stackState);
        }
    @Override
    public int getID() {
        return 5;
    }
    @Override
    public boolean check(Shelf shelf) {
        //count the columns that follow the requirements
        int differentColumns = 0;
        for (int column = 0; column < shelf.getColumns(); column++) {
            if(notEmptyAndAllDifferent(shelf.allTilesInColumn(column))){
                differentColumns++;
            }
        }
        return differentColumns >= 2;
    }
}
