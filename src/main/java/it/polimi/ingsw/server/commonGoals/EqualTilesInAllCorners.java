package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.List;

public class EqualTilesInAllCorners extends AbstractCommonGoal {
    public EqualTilesInAllCorners(int number_of_players){
        super(number_of_players);
    }
    public EqualTilesInAllCorners(List<Integer> stackState){
        super(stackState);
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
