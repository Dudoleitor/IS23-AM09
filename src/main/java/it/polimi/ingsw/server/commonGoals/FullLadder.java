package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FullLadder extends AbstractCommonGoal {
    public FullLadder(int number_of_players){
        super(number_of_players);
    }
    public FullLadder(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public int getID() {
        return 10;
    }
    @Override
    public boolean check(Shelf shelf) {
        int[] heights = new int[shelf.getColumns()];
        for(int column = 0; column < shelf.getColumns(); column++){
            heights[column] = columnHeigth(shelf,column);
        }
        return isLadder(heights);
    }
    private boolean isLadder(int[] heights){
        if(heights.length == 1){
            return true;
        }
        boolean ascending = true;
        int expected = heights[0]+1;
        for(int i = 1; i < heights.length; i++){
            ascending = ascending && heights[i] == expected;
            expected++;
        }
        boolean descending = true;
        expected = heights[0]-1;
        for(int i = 1; i < heights.length; i++){
            descending = descending && heights[i] == expected;
            expected--;
        }
        return ascending || descending;
    }
    private int columnHeigth(Shelf shelf, int column){
        return (int) shelf.allTilesInColumn(column).stream()
                .filter(x->!x.equals(Tile.Empty)).count();
    }
}
