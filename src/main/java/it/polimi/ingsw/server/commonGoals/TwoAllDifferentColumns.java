package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.ArrayList;
import java.util.Stack;

public class TwoAllDifferentColumns extends AbstractCommonGoal {
    public TwoAllDifferentColumns(int number_of_players){
            super(number_of_players);
        }
    public TwoAllDifferentColumns(ArrayList<Integer> stackState){
            super(stackState);
        }
    @Override
    public int getID() {
        return 5;
    }
    @Override
    public boolean check(Shelf shelf) {
        int differentColumns = 0;
        for (int column = 0; column < shelf.getColumns(); column++) {
            if(notEmptyAndAllDifferent(shelf.allTilesInColumn(column))){
                differentColumns++;
            }
        }
        return differentColumns >= 2;
    }
}
