package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.List;
import java.util.stream.IntStream;

public class FourLineWith3Types extends CommonGoal {
    public FourLineWith3Types(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public int getID() {
        return 8;
    }
    @Override
    public boolean check(Shelf shelf) {
        Long result = IntStream.range(0,shelf.getRows())
                .filter(row -> maxNTypes(shelf.allTilesInRow(row),3))
                .count();
        return Math.toIntExact(result) >= 4;
    }
}
