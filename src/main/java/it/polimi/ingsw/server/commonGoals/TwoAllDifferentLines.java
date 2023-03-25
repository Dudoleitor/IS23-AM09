package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalRuntimeException;
import it.polimi.ingsw.server.CommonGoalsException;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.ShelfGenericException;
import it.polimi.ingsw.shared.ShelfRuntimeException;

import java.util.List;
import java.util.stream.IntStream;

public class TwoAllDifferentLines extends CommonGoal {
    public TwoAllDifferentLines(List<Integer> stackState) {
        super(stackState);
    }

    @Override
    public int getID() {
        return 9;
    }

    @Override
    public boolean check(Shelf shelf) throws CommonGoalsException, ShelfGenericException {
        try {
            Long result = IntStream.range(0, shelf.getRows())
                    .filter(row -> {
                        try {
                            return notEmptyAndAllDifferent(shelf.allTilesInRow(row));
                        } catch (CommonGoalsException e) {
                            throw new CommonGoalRuntimeException(e.toString());
                        } catch (ShelfGenericException e) {
                            throw new ShelfRuntimeException(e.toString());
                        }
                    })
                    .count();
            return Math.toIntExact(result) >= 2;
        } catch (CommonGoalRuntimeException e){
            throw new CommonGoalsException(e.getMessage());
        } catch (ShelfRuntimeException e){
            throw new ShelfGenericException(e.getMessage());
        }
    }
}