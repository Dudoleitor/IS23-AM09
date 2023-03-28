package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.ShelfGenericException;
import it.polimi.ingsw.shared.Tile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class CommonGoal {
    /**
     * A Stack of points where players pop from when they complete CommonGoals
     */
    protected Stack<Integer> points;

    /**
     * Construcor that initializes a CommonGoal with a pre-determined stack State
     * @param stackState a List of integers that will form the stack state
     */
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
    public abstract boolean check(Shelf shelf) throws ShelfGenericException, CommonGoalsException;

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

    private int recursiveIslandVisit(Shelf shelf, Position position, boolean[][] alreadyChecked, Tile type) throws ShelfGenericException {
        int row = position.getRow();
        int column = position.getColumn();
        int result = 0;
        if(shelf.isOutOfBounds(row,column) || alreadyChecked[row][column] == true){
            return 0;
        }
        else if(shelf.getTile(row,column).equals(type)){
                alreadyChecked[row][column] = true;
                result = 1;
                for(Position neighbour : position.neighbours()){
                    result += recursiveIslandVisit(shelf,neighbour,alreadyChecked,type);
                }
        }
        return result;
    }

    /**
     * Returns the size of the "island" of Tiles with the same color in which the selected tile is in.
     * If the Tile is Empty or Invalid it returns 0. Visited Tiles are marked on alreadyChecked
     *@param shelf the player's shelf
     *@param position is the position of the Tile
     *@param alreadyChecked a matrix of booleans that keeps track of the visited Tiles
     *@return the size of the island
     */
    protected int validIslandSize(Shelf shelf, Position position, boolean[][] alreadyChecked) throws ShelfGenericException {
        if(shelf.isValidTile(position)){
            Tile islandType = shelf.getTile(position);
            return recursiveIslandVisit(shelf,position,alreadyChecked,islandType);
        }
        else{
            return 0;
        }
    }

    /**
     * This method is used to save the status of the shelf with a json object.
     * @return JSONObject with status.
     */
    public JSONObject toJson() {
        JSONObject commonGoalJson = new JSONObject();  // Object to return
        JSONArray goalStack = new JSONArray(); //points pile

        for(long point : showPointsStack()){
            goalStack.add(point);
        }
        commonGoalJson.put("id", (long)getID());
        commonGoalJson.put("stack", goalStack);

        return commonGoalJson;
    }
    @Override
    public boolean equals(Object o){ //check they have same ID
        if(o == null)
            return false;
        else if (o == this)
            return true;

        CommonGoal c = (CommonGoal) o;
        if(c.getID() != this.getID())
            return false;
        return true;
    }

}