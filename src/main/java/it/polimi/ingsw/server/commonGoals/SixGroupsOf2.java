package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;

import java.util.ArrayList;
import java.util.Stack;

public class SixGroupsOf2 extends AbstractCommonGoal {
    @Override
    public int getID() {
        return 1;
    }

    @Override
    public boolean check(Shelf shelf) { //WRONG
        int rows = shelf.getRows();
        int columns = shelf.getColumns();
        Position currentPosition;
        boolean[][] alreadyChecked = new boolean[rows][columns];
        int groups_found = 0;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                currentPosition = new Position(i,j);
                for(Position neighbour : uncheckedNeighbours(currentPosition,rows,columns,alreadyChecked)){
                    if(shelf.getTile(neighbour).equals(shelf.getTile(currentPosition))){
                        markPosition(alreadyChecked,currentPosition);
                        markPosition(alreadyChecked,neighbour);
                        groups_found++;
                    }
                }
            }
        }
        return groups_found >= 6;
    }
    private ArrayList<Position> uncheckedNeighbours(Position pos, int rows, int columns, boolean[][]alreadyChecked){
        ArrayList<Position> neighbours = new ArrayList<>();
        int i = pos.getRow();
        int j = pos.getColumn();
        if(i-1 >= 0){
            if(j-1 >= 0){
                if(alreadyChecked[i][j] == false){
                    neighbours.add(new Position(i-1,j-1));
                }
            }
            if(j+1 < columns){
                if(alreadyChecked[i][j] == false){
                    neighbours.add(new Position(i-1,j+1));
                }
            }
        }
        if(i+1 < rows){
            if (j - 1 >= 0) {
                if (alreadyChecked[i][j] == false) {
                    neighbours.add(new Position(i+1,j-1));
                }
            }
            if (j + 1 < columns) {
                if (alreadyChecked[i][j] == false) {
                    neighbours.add(new Position(i+1,j+1));
                }
            }
        }
        return neighbours;
    }
    private void markPosition(boolean[][] alreadyChecked, Position position){
        alreadyChecked[position.getRow()][position.getColumn()] = true;
    }
    public SixGroupsOf2(int number_of_players){
        points = new Stack<Integer>();
        populatePointsStack(number_of_players);
    }
}