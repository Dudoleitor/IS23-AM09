package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.*;

public class EightEqualTiles extends AbstractCommonGoal {
    public EightEqualTiles(int number_of_players){
        super(number_of_players);
    }
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
        HashMap<Tile,Integer> counters = new HashMap<>();
        for(Tile tile : Tile.values()){
            counters.put(tile,0);
        }
        for(int row = 0; row < shelf.getRows(); row++){
            for(int column = 0; column < shelf.getColumns(); column++){
                currentTile = shelf.getTile(new Position(row,column));
                if(!currentTile.equals(Tile.Empty) && !currentTile.equals(Tile.Invalid)){
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
