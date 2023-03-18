package it.polimi.ingsw.shared;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


public class Shelf {
    private final Tile[][] tiles; //I chose to consider matrix coordinate (0,0) as the top-left corner of the shelf
    private final int rows;
    private final int columns;


    public Shelf(int rows, int columns) { //initialize a shelf with Empty tiles
        this.rows = rows;
        this.columns = columns;
        tiles = new Tile[rows][columns];
        for (int i = 0; i<rows; i++){
            for (int j = 0; j<columns; j++){
                tiles[i][j] = Tile.Empty;
            }
        }
    }
    public Shelf(Shelf s) throws ShelfGenericException {
        try{
            rows = s.getRows();
            columns = s.getColumns();
            tiles = new Tile[rows][columns];
            for(int i = 0; i<rows; i++){
                for(int j = 0; j<columns; j++){
                    tiles[i][j] = s.getTile(i,j);
                }
            }
        } catch(NullPointerException e) {
            throw new ShelfGenericException("Error while creating Shelf : input Shelf is null pointer");
        }
    }
    public Shelf(String jsonPath) throws ShelfGenericException{

        JSONParser jsonParser = new JSONParser();
        try{
            Object obj = jsonParser.parse(new FileReader(jsonPath));
            JSONArray array_row;
            JSONObject obj_shelf = (JSONObject) ((JSONObject) obj).get("shelf");

            rows = Math.toIntExact((long)(obj_shelf.get("rows")));
            columns = Math.toIntExact((long)(obj_shelf.get("columns")));
            tiles = new Tile[rows][columns];
            Tile t;

            JSONArray array_shelf = (JSONArray)obj_shelf.get("matrix");
            for(int i = 0; i < rows; i++){
                array_row = (JSONArray) array_shelf.get(i);
                for(int j = 0; j < columns; j++){
                    t = Tile.valueOf((array_row.get(j)).toString());
                    if(t.equals(Tile.Invalid)){
                        throw new ShelfGenericException("Error while generating Shelf from JSON : Tile is Invalid type");
                    }
                    tiles[i][j] = t;
                }
            }
        } catch (FileNotFoundException e){
            throw new ShelfGenericException("Error while creating Shelf : json file not found");
        } catch (ParseException | IOException | ClassCastException | NullPointerException e){
            throw new ShelfGenericException("Error while creating Shelf : bad json parsing");
        }
    }
    public Tile getTile(Position pos) throws ShelfGenericException{
        try {
            return getTile(pos.getRow(), pos.getColumn());
        } catch (NullPointerException e) {
            throw new ShelfGenericException("Error while getting Tile in Shelf : Position object is null pointer");
        }
    }
    public Tile getTile(int row, int column) throws ShelfGenericException {
        try {
            return tiles[row][column];
        } catch (IndexOutOfBoundsException e){
            throw new ShelfGenericException("Error while getting Tile in Shelf : Coordinates are beyond boundaries");
        }

    }

    public int getHighestColumn(){ //gives back the number of free cells of the most empty column
        int max = 0;
        boolean max_found = false;
        int count;
        boolean not_empty;

        for(int j = 0; !max_found && j < tiles[0].length; j++){
            count = 0;
            not_empty = false;
            for(int i = 0; !max_found && !not_empty && i < tiles.length; i++){
                if(tiles[i][j] == Tile.Empty){
                    count++;
                } else {
                    not_empty = true;
                }
                if(count == tiles.length)
                    max_found = true;
            }
            if(count > max)
                max = count;
        }
        return max;
    }
    public void insertTile(Tile tile, int column) throws ShelfGenericException {
        boolean is_empty = false;
        if(tile.equals(Tile.Empty)){
            return;
        } else if (tile.equals(Tile.Invalid)){
            throw new ShelfGenericException("Error while inserting in Shelf : Tile is Invalid type");
        }
        for(int i = tiles.length-1; !is_empty && i>=0; i--){
            if(tiles[i][column] == Tile.Empty){
                tiles[i][column] = tile;
                is_empty = true;
            }
        }
        if (!is_empty){
            throw new ShelfGenericException("Error while inserting in Shelf : selected column is already full");
        }
    }

    public int checkAdiacent(){
        int count;
        int points = 0;

        boolean[][] visited = new boolean[rows][columns];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if (!visited[i][j]) {
                    count = exploreAdiacents(visited, i, j);
                    if(count >= 6)
                        points = points+8;
                    else if (count == 5)
                        points = points+5;
                    else if (count == 4)
                        points = points+3;
                    else if (count == 3)
                        points = points+2;
                }
            }
        }
        return points;
    }
    private int exploreAdiacents(boolean[][] visited, int i, int j){
        visited[i][j] = true;
        int count = 0;
        if(tiles[i][j].equals(Tile.Empty) || tiles[i][j].equals(Tile.Invalid))
            return 0;
        if( i>0 && !visited[i-1][j] && tiles[i][j].equals(tiles[i-1][j])){
            count = count + exploreAdiacents(visited, i-1, j);
        }
        if( i<rows-1 && !visited[i+1][j] && tiles[i][j].equals(tiles[i+1][j])){
            count = count + exploreAdiacents(visited, i+1, j);
        }
        if( j>0 && !visited[i][j-1] && tiles[i][j].equals(tiles[i][j-1])){
            count = count + exploreAdiacents(visited, i, j-1);
        }
        if( j<columns-1 && !visited[i][j+1] && tiles[i][j].equals(tiles[i][j+1])){
            count = count + exploreAdiacents(visited, i, j+1);
        }
        return count+1;

    }

    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }

    public boolean equals(Object o) throws ShelfGenericException{
        boolean sameShelf = true;
        if(o == null || this.getClass() != o.getClass()){
            return false;
        } else if (this == o) {
            return true;
        }
        for (int i = 0; sameShelf && i < rows; i++) {
            for (int j = 0; sameShelf && j < columns; j++) {
                if (!tiles[i][j].equals(((Shelf) o).getTile(i, j))) {
                    sameShelf = false;
                }
            }
        }
        return sameShelf;
    }
    @Override
    public String toString(){
        String s = "";
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                s = s.concat(tiles[i][j].toString() + " ");
            }
            s = s.concat("\n");
        }
        return s;
    }

    @Override
    public int hashCode(){
        return Arrays.deepHashCode(tiles); //deepHashCode is similar to HashCode but also applied to any sub-array of elements
    }


}
