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
    protected boolean check(Shelf shelf) {
        Tile[] corners = getCorners(shelf);
        return Arrays.stream(corners).distinct().count() == 1;
    }

    private Tile[] getCorners(Shelf shelf){
        Tile[] corners = new Tile[4];
        int rows = shelf.getRows();
        int columns = shelf.getColumns();
        corners[0] = shelf.getTile(new Position(0,0));
        corners[1] = shelf.getTile(new Position(0,columns-1));
        corners[2] = shelf.getTile(new Position(rows-1,0));
        corners[3] = shelf.getTile(new Position(rows-1,columns-1));
        return corners;
    }
}
