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
    public boolean check(Shelf shelf) { //Untested
        ArrayList<Tile> corners = shelf.getCorners();
        return notEmptyAndEqual(corners);
    }
}
