package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.List;

public class EqualX extends CommonGoal {
    public EqualX(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public int getID() {
        return 12;
    }
    @Override
    public boolean check(Shelf shelf) {
        int columns = shelf.getColumns();
        int rows = shelf.getRows();
        //Shelf too small to complete goal
        if(columns < 3 || rows < 3){
            return false;
        }
        //Look for an "x" shape of tiles that are equal and not Empty
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
