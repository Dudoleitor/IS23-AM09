package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.List;

public class ThreeColumnsWith3Types extends CommonGoal {
    public ThreeColumnsWith3Types(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public int getID() {
        return 4;
    }
    @Override
    public boolean check(Shelf shelf) {
        int correctColumns = 0;
        for(int column = 0; column < shelf.getColumns(); column++){
            if(maxNTypes(shelf.allTilesInColumn(column),3)){
                correctColumns++;
            }
        }
        return correctColumns >= 3;
    }
}
