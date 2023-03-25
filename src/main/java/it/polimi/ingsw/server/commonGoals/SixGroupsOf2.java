package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.ShelfGenericException;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.List;

public class SixGroupsOf2 extends CommonGoal {
    @Override
    public int getID() {
        return 1;
    }

    @Override
    public boolean check(Shelf shelf) throws ShelfGenericException {
        int rows = shelf.getRows();
        int columns = shelf.getColumns();
        boolean[][] alreadyChecked = new boolean[rows][columns];
        int groups_found = 0;
        for(int row = 0; row < rows; row++){
            for(int column = 0; column < columns; column++){
                    if(validIslandSize(shelf,row,column,alreadyChecked) >=2){
                        groups_found++;
                    }
                }
            }
        return groups_found >= 6;
    }

    public SixGroupsOf2(List<Integer> stackState){
        super(stackState);
    }
}