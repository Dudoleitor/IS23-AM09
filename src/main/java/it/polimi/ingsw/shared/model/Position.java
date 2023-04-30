package it.polimi.ingsw.shared.model;

import it.polimi.ingsw.shared.Jsonable;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Position implements Jsonable {
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
    public Position(JSONObject jsonChat){
        this.row = Integer.parseInt(jsonChat.get("row").toString());
        this.column = Integer.parseInt(jsonChat.get("column").toString());
    }

    public static Position fromString(String str){
        int row;
        int column;
        if(str == null || "".equals(str)){
            return null;
        }
        else {
            try {
                str = str.replace(" ", "");
                str = str.replace("(", "");
                str = str.replace(")", "");
                String[] values = str.split(",");
                if (values.length != 2) {
                    return null;
                } else {
                    row = Integer.valueOf(values[0]);
                    column = Integer.valueOf(values[1]);
                }
                return new Position(row, column);
            }
            catch (Exception e){
                return null;
            }
        }
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
    public String toString(){
        return "("+String.valueOf(row)+", "+String.valueOf(column)+")";
    }
    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
    public static boolean areAligned(List<Position> positions){
        return ( sameColumn(positions) && adjacentRows(positions) ) ||
                ( sameRow(positions) && adjacentColumns(positions) );
    }

    public static boolean sameRow(List<Position> positions){
        if(positions.size() == 0){
            return true;
        }
        int rowToMatch = positions.get(0).getRow();
        //no Position has different row thant the first one
        return !positions.stream().map(p -> p.getRow()).anyMatch(row -> row != rowToMatch);
    }
    public static boolean sameRow(List<Position> positions, Position p){
        List<Position> temL = new ArrayList<>(positions);
        temL.add(p);
        return sameRow(temL);
    }
    public static boolean sameColumn(List<Position> positions, Position p){
        List<Position> temL = new ArrayList<>(positions);
        temL.add(p);
        return sameColumn(temL);
    }

    public static boolean sameColumn(List<Position> positions) {
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

    @Override
    public JSONObject toJson() {
        JSONObject jsonPos = new JSONObject();  // Object to return

        // Saving parameters
        jsonPos.put("row", row);
        jsonPos.put("column", column);



        return jsonPos;
    }
}
