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
        for(int column = 0; column < shelf.getColumns(); column++){
            if(maxThreeTypes(allTilesInColumn(shelf,column))){
                correctColumns++;
            }
        }
        return correctColumns >= 4;
    }
}
