package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.*;

public abstract class AbstractCommonGoal {
    protected Stack<Integer> points;

    public abstract int getID();
    protected abstract boolean check(Shelf shelf);
    public void populatePointsStack(int number_of_players){
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
                //return exception or do nothing?
        }
    }

    public int givePoints(){
        return points.pop();
    }

    public ArrayList<Integer> showPointsStack(){ //useful in debugging
        return new ArrayList<>(points);
    }
    protected boolean notEmptyAndEqual(ArrayList<Tile> tiles){
        return tiles.stream().distinct().count() == 1 && !tiles.contains(Tile.Empty);
    }
    protected boolean notEmptyAndAllDifferent(ArrayList<Tile> tiles){
        return !tiles.contains(Tile.Empty) && tiles.stream().distinct().count() == tiles.size();
    }
    protected ArrayList<Tile> allTilesInColum(Shelf shelf,int column){
        ArrayList<Tile> tiles = new ArrayList<>();
        for(int row = 0; row < shelf.getRows();row++){
            tiles.add(shelf.getTile((new Position(row,column))));
        }
        return tiles;
    }
    protected  ArrayList<Tile> allTilesInRow(Shelf shelf, int row){
        ArrayList<Tile> tiles = new ArrayList<>();
        for(int column = 0; column < shelf.getColumns();column++){
            tiles.add(shelf.getTile((new Position(row,column))));
        }
        return tiles;
    }
    protected boolean maxThreeTypes(ArrayList<Tile> tiles){
        return tiles.size() > 0 &&
                !tiles.contains(Tile.Empty) &&
                tiles.stream().distinct().count() <= 3;
    }
}