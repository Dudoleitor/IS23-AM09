package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalsException;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.ShelfGenericException;
import it.polimi.ingsw.shared.Tile;

import java.util.ArrayList;
import java.util.List;

public class EqualTilesInAllCorners extends CommonGoal {
    public EqualTilesInAllCorners(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public int getID() {
        return 3;
    }
    @Override
    public boolean check(Shelf shelf) throws ShelfGenericException, CommonGoalsException {
        return notEmptyAndEqual(shelf.getCorners());
    }
}
