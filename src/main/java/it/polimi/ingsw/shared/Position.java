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

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;

        if(o.getClass() != this.getClass())
            return false;

        Position p = (Position) o;
        if(p.getRow() == getRow() && p.getColumn() == getColumn())
            return true;

        return false;
    }
}
