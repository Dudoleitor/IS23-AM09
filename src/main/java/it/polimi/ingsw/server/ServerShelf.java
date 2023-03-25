package it.polimi.ingsw.server;
import it.polimi.ingsw.shared.*;

public class ServerShelf extends Shelf {
    public ServerShelf(int rows, int columns) {
        super(rows, columns);
    }
    public ServerShelf(Shelf s) throws ShelfGenericException {
        super(s);
    }
}
