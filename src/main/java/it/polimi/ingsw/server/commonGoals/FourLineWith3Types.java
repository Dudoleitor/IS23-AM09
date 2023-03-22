package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.List;

public class FourLineWith3Types extends CommonGoal {
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
