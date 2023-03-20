package it.polimi.ingsw.shared;

public class Position {
    private final int row;
    private final int column;

    /**
     * position builder
     * @param row is position row
     * @param column is position column
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * @return row attribute
     */
    public int getRow() {
        return row;
    }

    /**
     *
     * @return column attribute
     */
    public int getColumn() {
        return column;
    }
}
