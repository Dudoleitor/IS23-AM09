package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalsException;
import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.ShelfGenericException;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.List;

public class TwoSquares extends CommonGoal {
    @Override
    public int getID() {
        return 2;
    }
    public TwoSquares(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public boolean check(Shelf shelf) throws CommonGoalsException, ShelfGenericException {
        int columns = shelf.getColumns();
        int rows = shelf.getRows();
        int equalSquaresCounter = 0;
        boolean[][] alreadyUsed = new boolean[rows][columns];
        //count all perfect squares (no overlap or adjacency)
        for(int row = 0; row < rows-1; row++){
            for(int column = 0; column < columns-1; column++){
                if(isPerfectSquare(shelf, alreadyUsed, row, column)){
                    markSquareAsUsed(alreadyUsed,row,column);
                    equalSquaresCounter++;
                }
            }
        }
        return equalSquaresCounter >= 2;
    }
    private ArrayList<Tile> get2x2Square(Shelf shelf,int row, int column) throws ShelfGenericException {
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(shelf.getTile(row,column));
        tiles.add(shelf.getTile(row+1,column));
        tiles.add(shelf.getTile(row,column+1));
        tiles.add(shelf.getTile(row+1,column+1));
        return tiles;
    }

    private boolean isAvailable(boolean[][] alreadyUsed,int row, int column){
        return !(alreadyUsed[row][column]) && !(alreadyUsed[row+1][column]) &&
        !(alreadyUsed[row][column+1]) && !(alreadyUsed[row+1][column+1]);
    }
    private void markSquareAsUsed(boolean[][] alreadyUsed, int row, int column){
        alreadyUsed[row][column] = true;
        alreadyUsed[row+1][column] = true;
        alreadyUsed[row][column+1] = true;
        alreadyUsed[row+1][column+1] = true;
    }

    private boolean isPerfectSquare(Shelf shelf, boolean[][] alreadyUsed, int row, int column) throws CommonGoalsException, ShelfGenericException {
        return isAvailable(alreadyUsed,row,column) &&
                notEmptyAndEqual(get2x2Square(shelf,row,column)) &&
                4 == validIslandSize(shelf,new Position(row,column),alreadyUsed);
    }
}
