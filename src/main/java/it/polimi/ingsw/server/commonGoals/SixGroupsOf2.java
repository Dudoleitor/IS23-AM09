package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.List;

public class SixGroupsOf2 extends CommonGoal {
    @Override
    public int getID() {
        return 1;
    }

    @Override
    public boolean check(Shelf shelf) { //TODO fix (?)
        int rows = shelf.getRows();
        int columns = shelf.getColumns();
        Position currentPosition;
        boolean[][] alreadyChecked = new boolean[rows][columns];
        int groups_found = 0;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                currentPosition = new Position(i,j);
                for(Position neighbour : uncheckedNeighbours(currentPosition,rows,columns,alreadyChecked)){
                    if(isCouple(shelf,currentPosition,neighbour,alreadyChecked)){
                        markPosition(alreadyChecked,currentPosition);
                        markPosition(alreadyChecked,neighbour);
                        groups_found++;
                    }
                }
            }
        }
        return groups_found >= 6;
    }

    private boolean isCouple(Shelf shelf, Position currentPosition, Position neighbour, boolean[][] alreadyChecked){
        return !shelf.getTile(currentPosition).equals(Tile.Empty) &&
                !alreadyChecked[currentPosition.getRow()][currentPosition.getColumn()] &&
                shelf.getTile(neighbour).equals(shelf.getTile(currentPosition));
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
    public SixGroupsOf2(List<Integer> stackState){
        super(stackState);
    }
}