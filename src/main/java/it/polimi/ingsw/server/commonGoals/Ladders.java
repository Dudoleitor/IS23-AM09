package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Ladders extends AbstractCommonGoal {
    public Ladders(int number_of_players){
        points = new Stack<>();
        populatePointsStack(number_of_players);
    }
    @Override
    public int getID() {
        return 7;
    }
    @Override
    protected boolean check(Shelf shelf) {
        ArrayList<ArrayList<Tile>> ladders = generateLadders(shelf);
        for(ArrayList<Tile> ladder : ladders){
            if(notEmptyAndEqual(ladder)){
                return true;
            }
        }
        return false;
    }

    private ArrayList<ArrayList<Tile>> generateLadders(Shelf shelf){
        int rows = shelf.getRows();
        int columns = shelf.getColumns();
        ArrayList<ArrayList<Tile>> ladders = new ArrayList<>();
        ArrayList<Tile> currentLadder;
        for(int initialrow = 0; initialrow < 2; initialrow++){ //value 2 is hardcoded
            currentLadder = new ArrayList<>();
            for(int i = 0; i < Math.min(rows,columns); i++){
                currentLadder.add(shelf.getTile(new Position(initialrow+i,i)));
            }
            ladders.add(new ArrayList<>(currentLadder));
            for(int i = 0; i < Math.min(rows,columns); i++){
                currentLadder.add(shelf.getTile(new Position(initialrow+i,columns-1-i)));
            }
            ladders.add(new ArrayList<>(currentLadder));
        }
        return ladders;
    }
}
