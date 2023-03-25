package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.ShelfGenericException;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FullLadder extends CommonGoal {
    public FullLadder(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public int getID() {
        return 10;
    }
    @Override
    public boolean check(Shelf shelf) throws ShelfGenericException {
        //get all heights in order
        int[] heights = new int[shelf.getColumns()];
        for(int column = 0; column < shelf.getColumns(); column++){
            heights[column] = columnHeigth(shelf,column);
        }
        //check if they form a ladder
        return isLadder(heights);
    }
    private boolean isLadder(int[] heights){
        if(heights.length == 1){
            return true;
        }
        //check if heights form an ascending ladder
        boolean ascending = true;
        int expected = heights[0]+1;
        for(int i = 1; i < heights.length; i++){
            ascending = ascending && heights[i] == expected;
            expected++;
        }

        //check if heights form a descending ladder
        boolean descending = true;
        expected = heights[0]-1;
        for(int i = 1; i < heights.length; i++){
            descending = descending && heights[i] == expected;
            expected--;
        }
        return ascending || descending;
    }
    private int columnHeigth(Shelf shelf, int column) throws ShelfGenericException {
        return (int) shelf.allTilesInColumn(column).stream()
                .filter(x->!x.equals(Tile.Empty)).count();
    }
}
