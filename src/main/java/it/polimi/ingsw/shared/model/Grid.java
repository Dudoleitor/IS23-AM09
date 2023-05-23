package it.polimi.ingsw.shared.model;

public interface Grid {
    public int getRows();
    public int getColumns();
    public Tile getTile(Position pos) throws BadPositionException;
    public Tile getTile(int row, int column) throws BadPositionException;
}
