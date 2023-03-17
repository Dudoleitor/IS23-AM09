package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.Stack;

public class ThreeColumnsWith3Types extends AbstractCommonGoal {
    public ThreeColumnsWith3Types(int number_of_players){
        points = new Stack<>();
        populatePointsStack(number_of_players);
    }
    @Override
    public int getID() {
        return 4;
    }
    @Override
    protected boolean check(Shelf shelf) {
        int correctColumns = 0;
        for(int row = 0; row < shelf.getRows(); row++){
            if(maxThreeTypes(allTilesInColum(shelf,row))){
                correctColumns++;
            }
        }
        return correctColumns >= 4;
    }
}
