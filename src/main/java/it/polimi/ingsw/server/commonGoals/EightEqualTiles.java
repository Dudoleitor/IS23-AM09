package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.*;

public class EightEqualTiles extends CommonGoal {
    public EightEqualTiles(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public int getID() {
        return 6;
    }
    @Override
    public boolean check(Shelf shelf) {
        Tile currentTile;
        //initialize counters for all tyoe of Tiles
        HashMap<Tile,Integer> counters = new HashMap<>();
        for(Tile tile : Tile.values()){
            counters.put(tile,0);
        }
        //count all tiles in different counters
        for(int row = 0; row < shelf.getRows(); row++){
            for(int column = 0; column < shelf.getColumns(); column++){
                currentTile = shelf.getTile(row,column);
                if(shelf.isValidTile(row,column)){
                    incrementCounter(counters,currentTile);
                }
            }
        }
        return counters.values().stream().filter(x -> x>= 8).count() > 0;
    }
    private void incrementCounter(HashMap<Tile,Integer> counters, Tile tile){
        counters.put(tile,counters.get(tile)+1);
    }
}
