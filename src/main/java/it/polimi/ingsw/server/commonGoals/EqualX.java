package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.Stack;

public class EqualX extends AbstractCommonGoal {
    public EqualX(int number_of_players){
        points = new Stack<>();
        populatePointsStack(number_of_players);
    }
    @Override
    public int getID() {
        return 12;
    }
    @Override
    public boolean check(Shelf shelf) {
        int columns = shelf.getColumns();
        int rows = shelf.getRows();
        if(columns < 3 || rows < 3){
            return false;
        }
        for(int row = 0; row < rows-2; row++){
            for(int column = 0; column < columns -2; column++){
                if(notEmptyAndEqual(getXShape(shelf,row,column))){
                    return true;
                }
            }
        }
        return false;
    }
    ArrayList<Tile> getXShape(Shelf shelf, int row, int column){
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(shelf.getTile(row,column));
        tiles.add(shelf.getTile(row+1,column+1));
        tiles.add(shelf.getTile(row+2,column+2));
        tiles.add(shelf.getTile(row,column+2));
        tiles.add(shelf.getTile(row+2,column));
        return tiles;
    }
}
