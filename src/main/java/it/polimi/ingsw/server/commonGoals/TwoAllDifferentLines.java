package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.List;
import java.util.stream.IntStream;

public class TwoAllDifferentLines extends CommonGoal {
    public TwoAllDifferentLines(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public int getID() {
        return 9;
    }
    @Override
    public boolean check(Shelf shelf) {
        Long result = IntStream.range(0,shelf.getRows())
                .filter(row -> notEmptyAndAllDifferent(shelf.allTilesInRow(row)))
                .count();
        return Math.toIntExact(result) >= 2;
    }
}
