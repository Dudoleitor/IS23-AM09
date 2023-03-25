package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalsException;
import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.ShelfGenericException;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.List;

public class Ladders extends CommonGoal {
    public Ladders(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public int getID() {
        return 7;
    }
    @Override
    public boolean check(Shelf shelf) throws CommonGoalsException, ShelfGenericException {
        //generate ladders
        ArrayList<ArrayList<Tile>> ladders = generateLadders(shelf);
        //check if at least one is composed of all equal tiles
        for(ArrayList<Tile> ladder : ladders){
            if(notEmptyAndEqual(ladder)){
                return true;
            }
        }
        return false;
    }

    private ArrayList<ArrayList<Tile>> generateLadders(Shelf shelf) throws ShelfGenericException {
        int rows = shelf.getRows();
        int columns = shelf.getColumns();
        ArrayList<ArrayList<Tile>> ladders = new ArrayList<>();
        ArrayList<Tile> currentLadder = new ArrayList<>();

        //collect tiles in ascending ladders
        for(int initial_row = 0; initial_row < 2; initial_row++){ //value 2 is hardcoded
            currentLadder.clear();
            for(int i = 0; i < Math.min(rows,columns); i++){
                currentLadder.add(shelf.getTile(new Position(initial_row+i,i)));
            }
            ladders.add(new ArrayList<>(currentLadder));
        }

        //collect tiles in descending ladders
        for(int initial_row = 0; initial_row < 2; initial_row++){ //value 2 is hardcoded
            currentLadder.clear();
            for(int i = 0; i < Math.min(rows,columns); i++){
                currentLadder.add(shelf.getTile(new Position(initial_row+i,columns-1-i)));
            }
            ladders.add(new ArrayList<>(currentLadder));
        }
        return ladders;
    }
}
