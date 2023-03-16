package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.Stack;

public class ThreeEqualColumns extends AbstractCommonGoal {
    public ThreeEqualColumns(int number_of_players){
        points = new Stack<>();
        populatePointsStack(number_of_players);
    }
    @Override
    public int getID() {
        return 5;
    }
    @Override
    protected boolean check(Shelf shelf) {
        int equalColumns = 0;
        for (int column = 0; column < shelf.getColumns(); column++) {
            if(allTilesInColum(shelf,column).stream().distinct().count() == 1){
                equalColumns++;
            }
        }
        return equalColumns >= 3;
    }

    ArrayList<Tile> allTilesInColum(Shelf shelf,int column){
        ArrayList<Tile> tiles = new ArrayList<>();
        for(int row = 0; row < shelf.getRows();row++){
            tiles.add(shelf.getTile((new Position(row,column))));
        }
        return tiles;
    }

}
