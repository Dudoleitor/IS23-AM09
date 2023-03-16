package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;

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
        ArrayList<Position[]> ladders =generateLadders(shelf);
        for(Position[] ladder : ladders){
            if(allEqualTiles(ladder)){
                return true;
            }
        }
        return false;
    }

    private ArrayList<Position[]> generateLadders(Shelf shelf){
        int rows = shelf.getRows();
        int columns = shelf.getColumns();
        ArrayList<Position[]> ladders = new ArrayList<>();
        Position[] currentLadder= new Position[Math.min(rows,columns)];
        for(int initialrow = 0; initialrow < 2; initialrow++){ //value 2 is hardcoded
            for(int i = 0; i < currentLadder.length; i++){
                currentLadder[i] = new Position(initialrow+i,i);
            }
            for(int i = 0; i < currentLadder.length; i++){
                currentLadder[i] = new Position(initialrow+i,columns-1-i);
            }
            ladders.add(currentLadder.clone());
        }
        return ladders;
    }

    private boolean allEqualTiles(Position[] tiles){
        return Arrays.stream(tiles).distinct().count() == 1;
    }
}
