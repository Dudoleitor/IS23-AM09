package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FourLineWith3Types extends AbstractCommonGoal {
    public FourLineWith3Types(int number_of_players){
        super(number_of_players);
    }
    public FourLineWith3Types(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public int getID() {
        return 8;
    }
    @Override
    public boolean check(Shelf shelf) {
        int correctRows = 0;
        for(int row = 0; row < shelf.getRows(); row++){
            if(maxNTypes(shelf.allTilesInRow(row),3)){
                correctRows++;
            }
        }
        return correctRows >= 4;
    }
}
