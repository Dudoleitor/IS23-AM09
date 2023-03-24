package it.polimi.ingsw.server.commonGoals;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.shared.Shelf;

import java.util.List;
import java.util.stream.IntStream;

public class ThreeColumnsWith3Types extends CommonGoal {
    public ThreeColumnsWith3Types(List<Integer> stackState){
        super(stackState);
    }
    @Override
    public int getID() {
        return 4;
    }
    @Override
    public boolean check(Shelf shelf) {
        Long result = IntStream.range(0,shelf.getColumns())
                .filter(column -> maxNTypes(shelf.allTilesInColumn(column),3))
                .count();
        return Math.toIntExact(result) >= 3;
    }
}
