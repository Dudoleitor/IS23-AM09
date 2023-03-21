package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ThreeColumnsWith3Types extends AbstractCommonGoal {
    public ThreeColumnsWith3Types(int number_of_players){
        super(number_of_players);
    }
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
