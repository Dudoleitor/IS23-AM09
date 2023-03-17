package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.Stack;

public class FourLineWith3Types extends AbstractCommonGoal {
    public FourLineWith3Types(int number_of_players){
        points = new Stack<>();
        populatePointsStack(number_of_players);
    }
    @Override
    public int getID() {
        return 8;
    }
    @Override
    protected boolean check(Shelf shelf) {
        int correctRows = 0;
        for(int row = 0; row < shelf.getRows(); row++){
            if(maxThreeTypes(allTilesInRow(shelf,row))){
                correctRows++;
            }
        }
        return correctRows >= 4;
    }
}
