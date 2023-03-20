package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.Stack;

public abstract class AbstractCommonGoal {
    /**
     * A Stack of points where players pop from when they complete CommonGoals
     */
    protected Stack<Integer> points;

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
     * populate the point stacks of the Common Goal accordingly to the number of players (2-4)
     * @param number_of_players the number of players
     * @throws CommonGoalsException if number_of_players is smaller than 2 or bigger than 4
     */
    public void populatePointsStack(int number_of_players) throws CommonGoalsException{
        this.points = new Stack<>();
        switch (number_of_players){
            case 2:
                points.push(4);
                points.push(8);
                break;
            case 3:
                points.push(4);
                points.push(6);
                points.push(8);
                break;
            case 4:
                points.push(2);
                points.push(4);
                points.push(6);
                points.push(8);
                break;
            default:
                throw new CommonGoalsException("Error while Populating PointsStack : Illegal num of players");
        }
    }

    /**
     * Pops points from PointStack
     * @return the points popped from the Stack
     */
    public int givePoints(){
        return points.pop();
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
     * Returns all the Tiles in a specific column of the shelf
     * @param shelf a player's Shelf
     * @param column a column of the shelf
     * @return an ArrayList containing all the Tiles in the selected column of the shelf
     * @throws CommonGoalsException
     */
    protected ArrayList<Tile> allTilesInColumn(Shelf shelf, int column) throws CommonGoalsException{
        try {
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int row = 0; row < shelf.getRows(); row++) {
                tiles.add(shelf.getTile(row, column));
            }
            return tiles;
        } catch (NullPointerException e){
            throw new CommonGoalsException("Error while checking AllTilesInColumn : shelf is null pointer");
        }
    }

    /**
     * Returns all the Tiles in a specific row of the shelf
     * @param shelf a player's Shelf
     * @param row a row of the shelf
     * @return an ArrayList containing all the Tiles in the selected row of the shelf
     * @throws CommonGoalsException
     */
    protected  ArrayList<Tile> allTilesInRow(Shelf shelf, int row) throws CommonGoalsException{
        try {
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int column = 0; column < shelf.getColumns(); column++) {
                tiles.add(shelf.getTile(row, column));
            }
            return tiles;
        } catch (NullPointerException e){
            throw new CommonGoalsException("Error while checking AllTilesInRow : shelf is null pointer");
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
}