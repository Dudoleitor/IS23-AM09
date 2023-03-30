package it.polimi.ingsw.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
    public static boolean areAligned(List<Position> positions){
        return ( sameColumn(positions) && adjacentRows(positions) ) ||
                ( sameRow(positions) && adjacentColumns(positions) );
    }

    private static boolean sameRow(List<Position> positions){
        if(positions.size() == 0){
            return true;
        }
        int rowToMatch = positions.get(0).getRow();
        //no Position has different row thant the first one
        return !positions.stream().map(p -> p.getRow()).anyMatch(row -> row != rowToMatch);
    }

    private static boolean sameColumn(List<Position> positions) {
        if (positions.size() == 0) {
            return true;
        }
        int colToMatch = positions.get(0).getColumn();
        //no Position has different column thant the first one
        return !positions.stream().map(p -> p.getColumn()).anyMatch(row -> row != colToMatch);
    }

    private static boolean adjacentRows(List<Position> positions) {
        List<Integer> rows = positions.stream().map(p -> p.getRow()).sorted().collect(Collectors.toList());
        return isSequential(rows);
    }

    private static boolean adjacentColumns(List<Position> positions) {
        List<Integer> columns = positions.stream().map(p -> p.getColumn()).sorted().collect(Collectors.toList());
        return isSequential(columns);
    }

    private static boolean isSequential(List<Integer> integers){
        if(integers.size() == 0){
            return true;
        }
        int expected = integers.get(0) + 1;
        for(int i = 1; i < integers.size(); i++){
            if(integers.get(i) != expected){
                return false;
            }
            expected++;
        }
        return true;
    }

    public static boolean duplicates(List<Position> positions){
        int distinctPositions = Math.toIntExact(positions.stream().distinct().count());
        return distinctPositions != positions.size();
    }

    public List<Position> neighbours(){
        List<Position> neighbours = new ArrayList<>();
        neighbours.add(new Position(getRow()-1,getColumn()));
        neighbours.add(new Position(getRow(),getColumn()-1));
        neighbours.add(new Position(getRow()+1,getColumn()));
        neighbours.add(new Position(getRow(),getColumn()+1));
        return neighbours;
    }

    public List<Position> square(){
        List<Position> square = new ArrayList<>();
        square.add(new Position(getRow(),getColumn()));
        square.add(new Position(getRow()+1,getColumn()));
        square.add(new Position(getRow(),getColumn()+1));
        square.add(new Position(getRow()+1,getColumn()+1));
        return square;
    }
}
