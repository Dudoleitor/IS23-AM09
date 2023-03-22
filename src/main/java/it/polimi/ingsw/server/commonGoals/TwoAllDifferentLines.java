package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.List;

public class TwoAllDifferentLines extends CommonGoal {
    public TwoAllDifferentLines(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public int getID() {
        return 9;
    }
    @Override
    public boolean check(Shelf shelf) {
        //count the rows that follow the requirements
        int differentLines = 0;
        for (int row = 0; row < shelf.getRows(); row++) {
            if(notEmptyAndAllDifferent(shelf.allTilesInRow(row))){
                differentLines++;
            }
        }
        return differentLines >= 2;
    }
}
