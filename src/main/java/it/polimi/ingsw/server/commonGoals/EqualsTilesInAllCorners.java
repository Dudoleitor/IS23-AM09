package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class EqualsTilesInAllCorners extends AbstractCommonGoal {
    public EqualsTilesInAllCorners(int number_of_players){
        points = new Stack<>();
        populatePointsStack(number_of_players);
    }
    @Override
    public int getID() {
        return 3;
    }
    @Override
    protected boolean check(Shelf shelf) { //Untested
        ArrayList<Tile> corners = getCorners(shelf);
        return notEmptyAndEqual(corners);
    }

    private ArrayList<Tile> getCorners(Shelf shelf){
        ArrayList<Tile> corners = new ArrayList<>();
        int rows = shelf.getRows();
        int columns = shelf.getColumns();
        corners.add(shelf.getTile(new Position(0,0)));
        corners.add(shelf.getTile(new Position(0,columns-1)));
        corners.add(shelf.getTile(new Position(rows-1,0)));
        corners.add(shelf.getTile(new Position(rows-1,columns-1)));
        return corners;
    }
}
