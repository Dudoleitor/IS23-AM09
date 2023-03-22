package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class CommonGoal {
    /**
     * A Stack of points where players pop from when they complete CommonGoals
     */
    protected Stack<Integer> points;
    protected CommonGoal(List<Integer> stackState){
        points = new Stack<>();
        for(Integer i : stackState){
            points.push(i);
        }
    }
    /**
     * Returns the ID of the CommonGoal
     * @return CommonGoal ID (1-12)
     */
    public abstract int getID();

    /**
     * Checks the condition of the CommonGoal
     * @param shelf the shelf of one of the players
     * @return TRUE if the condition of the CommonGoal is verified
     */
    public abstract boolean check(Shelf shelf);

    /**
     * Pops points from PointStack
     * @return the points popped from the Stack
     */
    public int givePoints(){
        if(points.size() > 0){
            return points.pop();
        }
        else{
            return 0;
        }
    }

    /**
     * Shows the state of the Points Stack
     * @return the Points Stack as an ArrayList
     */
    public ArrayList<Integer> showPointsStack(){ //useful in debugging
        return new ArrayList<>(points);
    }

    /**
     * Checks if some tiles are all Equal and not Empty
     * @param tiles an ArrayList of Tiles
     * @return true if there are no Empty Tiles and they are all equal
     * @throws CommonGoalsException
     */
    protected boolean notEmptyAndEqual(ArrayList<Tile> tiles) throws CommonGoalsException {
        try {
            return tiles.stream().distinct().count() == 1 && !tiles.contains(Tile.Empty);

        } catch (NullPointerException e){
            throw new CommonGoalsException("Error while checking notEmptyAndEqual : tiles is null pointer");
        }
    }

    /**
     * Checks if some tiles are all different and not Empty
     * @param tiles an ArrayList of Tiles
     * @return true if there are no Empty Tiles and they are all different
     * @throws CommonGoalsException if tiles is Null
     */
    protected boolean notEmptyAndAllDifferent(ArrayList<Tile> tiles) throws CommonGoalsException {
        try {
            return !tiles.contains(Tile.Empty) && tiles.stream().distinct().count() == tiles.size();
        } catch (NullPointerException e){
            throw new CommonGoalsException("Error while checking notEmptyAndAllDifferent : tiles is null pointer");
        }
    }

    /**
     * Checks if some Tiles have maximum n types of Tiles and do not contain Empty Tiles
     * @param tiles an ArrayList of Tiles
     * @param n the maximum number of accepted types of Tiles in tiles
     * @return True if there are no empty Tiles and there are maximum n types of Tiles in tiles
     * @throws CommonGoalsException
     */
    protected boolean maxNTypes(ArrayList<Tile> tiles, int n) throws CommonGoalsException {
        try {
            return tiles.size() > 0 &&
                    !tiles.contains(Tile.Empty) &&
                    tiles.stream().distinct().count() <= n;

        } catch (NullPointerException e){
            throw new CommonGoalsException("Error while checking maxThreeTypes : tiles is null pointer");
        }
    }

    protected int islandSize(Shelf shelf, int row, int column, boolean[][] alreadyChecked, Tile type){
        if(invalidPosition(shelf,row,column) || alreadyChecked[row][column] == true){
            return 0;
        }
        else{
            if(shelf.getTile(row,column).equals(type)){
                alreadyChecked[row][column] = true;
                return  1 +
                        islandSize(shelf,row-1,column,alreadyChecked,type) +
                        islandSize(shelf,row+1,column,alreadyChecked,type) +
                        islandSize(shelf,row,column-1,alreadyChecked,type) +
                        islandSize(shelf,row,column+1,alreadyChecked,type);
            }
            else return 0;
        }
    }

    private boolean invalidPosition(Shelf shelf, int row, int column){
        if(row < 0 || row >= shelf.getRows() || column < 0 || column >= shelf.getColumns()){
            return true;
        }
        return false;
    }
}