package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.List;
import java.util.stream.IntStream;

public class TwoAllDifferentColumns extends CommonGoal {
    public TwoAllDifferentColumns(List<Integer> stackState){
            super(stackState);
        }
    @Override
    public int getID() {
        return 5;
    }
    @Override
    public boolean check(Shelf shelf) {
        Long result = IntStream.range(0,shelf.getColumns())
                .filter(column -> notEmptyAndAllDifferent(shelf.allTilesInColumn(column)))
                .count();
        return Math.toIntExact(result) >= 2;
    }
}
