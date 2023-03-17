package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.Stack;

public class FullLadder extends AbstractCommonGoal {
    public FullLadder(int number_of_players){
        points = new Stack<>();
        populatePointsStack(number_of_players);
    }
    @Override
    public int getID() {
        return 10;
    }
    @Override
    protected boolean check(Shelf shelf) {
        int[] heights = new int[shelf.getColumns()];
        for(int column = 0; column < shelf.getColumns(); column++){
            heights[column] = columnHeigth(shelf,column);
        }
        return isLadder(heights);
    }
    private boolean isLadder(int[] heights){
        boolean result = true;
        if(heights.length == 1){
            return true;
        }
        int previous = heights[0];
        for(int i = 1; i < heights.length; i++){
            result = result && heights[i] == previous+1;
            previous++;
        }
        return result;
    }
    private int columnHeigth(Shelf shelf, int column){
        return (int) allTilesInColumn(shelf,column).stream()
                .filter(x->!x.equals(Tile.Empty)).count();
    }
}
