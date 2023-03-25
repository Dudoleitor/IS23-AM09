package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalRuntimeException;
import it.polimi.ingsw.server.CommonGoalsException;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.ShelfGenericException;
import it.polimi.ingsw.shared.ShelfRuntimeException;

import java.util.List;
import java.util.stream.IntStream;

public class ThreeColumnsWith3Types extends CommonGoal {
    public ThreeColumnsWith3Types(List<Integer> stackState) {
        super(stackState);
    }

    @Override
    public int getID() {
        return 4;
    }

    @Override
    public boolean check(Shelf shelf) throws CommonGoalsException, ShelfGenericException {
        try {
            Long result = IntStream.range(0, shelf.getColumns())
                    .filter(column -> {
                        try {
                            return maxNTypes(shelf.allTilesInColumn(column), 3);
                        } catch (CommonGoalsException e) {
                            throw new CommonGoalRuntimeException(e.getMessage());
                        } catch (ShelfGenericException e) {
                            throw new ShelfRuntimeException(e.getMessage());
                        }
                    })
                    .count();
            return Math.toIntExact(result) >= 3;
        } catch (CommonGoalRuntimeException e){
            throw new CommonGoalsException(e.getMessage());
        } catch (ShelfRuntimeException e){
            throw new ShelfGenericException(e.getMessage());
        }
    }
}