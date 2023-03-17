package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.*;

public abstract class AbstractCommonGoal {
    protected Stack<Integer> points;

    public abstract int getID();
    protected abstract boolean check(Shelf shelf);
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

    public int givePoints(){
        return points.pop();
    }

    public ArrayList<Integer> showPointsStack(){ //useful in debugging
        return new ArrayList<>(points);
    }
    protected boolean notEmptyAndEqual(ArrayList<Tile> tiles) throws CommonGoalsException {
        try {
            return tiles.stream().distinct().count() == 1 && !tiles.contains(Tile.Empty);

        } catch (NullPointerException e){
            throw new CommonGoalsException("Error while checking notEmptyAndEqual : tiles is null pointer");
        }
    }
    protected boolean notEmptyAndAllDifferent(ArrayList<Tile> tiles) throws CommonGoalsException {
        try {
            return !tiles.contains(Tile.Empty) && tiles.stream().distinct().count() == tiles.size();
        } catch (NullPointerException e){
            throw new CommonGoalsException("Error while checking notEmptyAndAllDifferent : tiles is null pointer");
        }
    }
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
    protected boolean maxThreeTypes(ArrayList<Tile> tiles) throws CommonGoalsException {
        try {
            return tiles.size() > 0 &&
                    !tiles.contains(Tile.Empty) &&
                    tiles.stream().distinct().count() <= 3;

        } catch (NullPointerException e){
            throw new CommonGoalsException("Error while checking maxThreeTypes : tiles is null pointer");
        }
    }
}