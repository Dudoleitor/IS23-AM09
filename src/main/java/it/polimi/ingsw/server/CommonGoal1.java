package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Shelf;

public class CommonGoal1 extends AbstractCommonGoal{
    @Override
    public boolean check(Shelf shelf) {
        return false;
    }

    @Override
    public int givePoints() {
        return 0;
    }
}
