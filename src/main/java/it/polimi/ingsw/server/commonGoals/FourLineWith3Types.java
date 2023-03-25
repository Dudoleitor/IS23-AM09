package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalRuntimeException;
import it.polimi.ingsw.server.CommonGoalsException;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.ShelfGenericException;
import it.polimi.ingsw.shared.ShelfRuntimeException;

import java.util.List;
import java.util.stream.IntStream;

public class FourLineWith3Types extends CommonGoal {
    public FourLineWith3Types(List<Integer> stackState) {
        super(stackState);
    }

    @Override
    public int getID() {
        return 8;
    }

    @Override
    public boolean check(Shelf shelf) throws CommonGoalsException, ShelfGenericException {
        try {
            Long result = IntStream.range(0, shelf.getRows())
                    .filter(row -> {
                        try {
                            return maxNTypes(shelf.allTilesInRow(row), 3);
                        } catch (CommonGoalsException e) {
                            throw new CommonGoalRuntimeException(e.getMessage());
                        } catch (ShelfGenericException e) {
                            throw new ShelfRuntimeException(e.getMessage());
                        }
                    })
                    .count();
            return Math.toIntExact(result) >= 4;
        } catch (CommonGoalRuntimeException e){
            throw new CommonGoalsException(e.getMessage());
        } catch (ShelfRuntimeException e){
            throw new ShelfGenericException(e.getMessage());
        }
    }
}