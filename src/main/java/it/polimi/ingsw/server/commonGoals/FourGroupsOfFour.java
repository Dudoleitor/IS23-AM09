package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.ShelfGenericException;
import it.polimi.ingsw.shared.Tile;

import java.util.List;

public class FourGroupsOfFour extends CommonGoal {
    public FourGroupsOfFour(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public int getID() {
        return 11;
    }
    @Override
    public boolean check(Shelf shelf) throws ShelfGenericException {
        int rows = shelf.getRows();
        int columns = shelf.getColumns();
        boolean[][] alreadyChecked = new boolean[rows][columns];
        int groups_found = 0;
        //count all groups with more than 2 tiles
        for(int row = 0; row < rows; row++){
            for(int column = 0; column < columns; column++){
                if(validIslandSize(shelf,new Position(row,column),alreadyChecked) >=4){
                    groups_found++;
                }
            }
        }
        return groups_found >= 4;
    }
}
