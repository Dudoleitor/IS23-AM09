package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TwoAllDifferentLines extends AbstractCommonGoal {
    public TwoAllDifferentLines(int number_of_players){
        super(number_of_players);
    }
    public TwoAllDifferentLines(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public int getID() {
        return 9;
    }
    @Override
    public boolean check(Shelf shelf) {
        int differentLines = 0;
        for (int row = 0; row < shelf.getRows(); row++) {
            if(notEmptyAndAllDifferent(shelf.allTilesInRow(row))){
                differentLines++;
            }
        }
        return differentLines >= 2;
    }
}
