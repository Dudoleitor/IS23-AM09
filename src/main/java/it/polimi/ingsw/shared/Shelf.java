package it.polimi.ingsw.shared;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;


public class Shelf {
    private final Tile[][] tiles; //matrix coordinate (0,0) is the top-left corner of the shelf
    private final int rows;
    private final int columns;


    /**
     * generate Shelf by input parameters
     * @param rows is num of rows
     * @param columns is num of columns
     */
    public Shelf(int rows, int columns) throws ShelfGenericException { //initialize a shelf with Empty tiles
        if(rows <= 0 || columns <= 0){
            throw new ShelfGenericException("Error while generating shelf : dimensions are no valid numbers");
        }
        this.rows = rows;
        this.columns = columns;
        tiles = new Tile[rows][columns];
        for (int i = 0; i<rows; i++){ //initialize tiles as a Tile.Empty matrix
            for (int j = 0; j<columns; j++){
                tiles[i][j] = Tile.Empty;
            }
        }
    }

    /**
     * generate Shelf by copy of another Shelf
     * @param s is Shelf to copy from
     * @throws ShelfGenericException when input shelf is NullPointer
     */
    public Shelf(Shelf s) throws ShelfGenericException {
        try{
            if(!s.isValid()){
                throw new ShelfGenericException("Error while creating Shelf : input Shelf is not valid");
            }
            rows = s.getRows();
            columns = s.getColumns();
            tiles = new Tile[rows][columns];
            for(int i = 0; i<rows; i++){ //copy all Tiles from Shelf s to tiles
                for(int j = 0; j<columns; j++){
                    tiles[i][j] = s.getTile(i,j);
                }
            }
        } catch(NullPointerException e) {
            throw new ShelfGenericException("Error while creating Shelf : input Shelf is null pointer");
        }
    }

    /**
     * Generates Shelf by JSON file
     * @param jsonPath is the file path to the JSON file
     * @throws ShelfGenericException when JSON file can't be found or bad JSON parsing is done
     * @throws FileNotFoundException when JSON file can't be found
     * @throws ParseException when parsing problems occurs
     * @throws IOException when there are problems accessing file
     */
    public Shelf(String jsonPath) throws ShelfGenericException{
        this(pathToJSONObject(jsonPath)); //get shelf JSONObject
    }

    /**
     * This static method returns the JSONObject from a json file,
     * it uses the "shelf" attribute.
     * @param jsonPath path to the json file
     * @return JSONObject with the content
     * @throws IOException when an IO error happens
     * @throws ParseException when a parsing error happens
     */
    public static JSONObject pathToJSONObject(String jsonPath) throws ShelfGenericException {
        try {
            JSONObject result;
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(jsonPath);
            result = (JSONObject) jsonParser.parse(reader);
            return (JSONObject) result.get("shelf");
        } catch (IOException | ParseException e) {
            throw new ShelfGenericException("Error while generating Shelf from JSON : file not found");
        }
    }

    /**
     * Loads a Shelf from Json Object
     * @param jsonShelf is the JSONObject containing the parameters necessary to build the Shelf object
     * @throws ShelfGenericException when a problem in the creation of shelf occurs
     */
    public Shelf(JSONObject jsonShelf) throws ShelfGenericException {
        rows = Math.toIntExact((long) (jsonShelf.get("numRows")));
        columns = Math.toIntExact((long) (jsonShelf.get("numColumns")));
        tiles = new Tile[rows][columns];

        JSONArray array_shelf = (JSONArray) jsonShelf.get("matrix"); //matrix is the copy of the shelf
        JSONArray shelfLine;

        Tile t;
        try {
            for (int i = 0; i < rows; i++) { //copy entire matrix to shelf
                shelfLine = (JSONArray) array_shelf.get(i); //acquire line of matrix
                for (int j = 0; j < columns; j++) {
                    t = Tile.valueOfLabel((String) shelfLine.get(j));
                    if (t.equals(Tile.Invalid)) { //check whether Tile is Invalid
                        throw new ShelfGenericException("Error while generating Shelf from JSON : Tile is Invalid type");
                    }
                    tiles[i][j] = t;
                }
            }
            if (!isValid()) {
                throw new ShelfGenericException("Error while creating Shelf : bad tiles configuration");
            }
        } catch (TileGenericException e) {
            throw new ShelfGenericException("Error while creating Shelf : Tile type not found");
        }
    }
    private boolean isValid(){
        boolean valid = true;
        boolean notEmptyFound;

        for(int j = 0; valid && j < columns; j++){
            notEmptyFound = false;
            for( int i = 0; valid && i < rows; i++){ //check if there are empty tiles between valid tiles ineach column
                if(tiles[i][j].equals(Tile.Empty)){
                    if(notEmptyFound)
                        valid = false;
                } else if (tiles[i][j].equals(Tile.Invalid)) {
                    valid = false;
                } else {
                    notEmptyFound = true;
                }
            }
        }
        return valid;
    }

    /**
     * Get tile in position pos
     * @param pos is the position object
     * @return Tile in position pos
     * @throws ShelfGenericException when getting position is out of bound or pos is NullPointer
     */
    public Tile getTile(Position pos) throws ShelfGenericException{
        try {
            return getTile(pos.getRow(), pos.getColumn());
        } catch (NullPointerException e) {
            throw new ShelfGenericException("Error while getting Tile in Shelf : Position object is null pointer");
        }
    }
    /**
     * Get tile in position (row,column)
     * @param row is row index
     * @param column is column index
     * @return Tile in position (row,column)
     * @throws ShelfGenericException when getting coordinates are out of bound
     */
    public Tile getTile(int row, int column) throws ShelfGenericException {
        try {
            return tiles[row][column];
        } catch (IndexOutOfBoundsException e){
            throw new ShelfGenericException("Error while getting Tile in Shelf : Coordinates are beyond boundaries");
        }
    }

    /**
     * @return the max number of Empty tiles in a single column
     */
    public int getHighestColumn() throws ShelfGenericException {
        try {
            LongStream allColumns = LongStream.range(0, columns);
            Function<Long, Long> countEmptyTiles = x ->
            {
                try {
                    return allTilesInColumn(Math.toIntExact(x))
                            .stream()
                            .filter(tile -> tile.equals(Tile.Empty))
                            .count();
                } catch (ShelfGenericException e) {
                    throw new ShelfRuntimeException(e.getMessage());
                }
            };

            Long result = allColumns
                    .map(countEmptyTiles::apply)
                    .max()
                    .orElse(0);
            return Math.toIntExact(result);

        } catch (ShelfRuntimeException e){
            throw new ShelfGenericException(e.getMessage());
        }
    }
    /**
     * Insert tile to selected column
     * @param tile is the tile object to insert
     * @param column is the selected column
     * @throws ShelfGenericException whether tile is Invalid or column is already full
     */
    public void insertTile(Tile tile, int column) throws ShelfGenericException{
        boolean isEmpty = false;
        if(tile.equals(Tile.Empty)){ //if Tile is Empty then do nothing
            return;
        } else if (tile.equals(Tile.Invalid)){ //if Tile is Invalid then throw exception
            throw new ShelfGenericException("Error while inserting in Shelf : Tile is Invalid type");
        } else if (!tiles[0][column].equals(Tile.Empty)) { //if selected column has an element in first position aka (also known as) the column is full
            throw new ShelfGenericException("Error while inserting in Shelf : selected column is already full");
        }
        for (int i = tiles.length - 1; !isEmpty && i >= 0; i--) {// back-iterate the column to find first Empty cell to insert Tile
            if (tiles[i][column] == Tile.Empty) {
                tiles[i][column] = tile;
                isEmpty = true;
            }
        }

    }

    /**
     * @return points count from group of adjacent Tiles in the board
     */
    public int countAdjacentPoints() throws ShelfGenericException {
        int count;
        int points = 0;

        boolean[][] visited = new boolean[rows][columns]; //matrix to check already visited positions while exploring recursively
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if (!visited[i][j]) {//if a cell hasn't been explored yet, then it starts looking for greatest size group containing this cell
                    count = exploreAdjacents(visited, i, j);
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

    /**
     * recursive method to explore adjacent Tiles of same type
     * @param visited is matrix of already visited positions
     * @param i is the number of row of current position
     * @param j is the number of column of current position
     * @return the number of adjacent tiles of same type
     */
    private int exploreAdjacents(boolean[][] visited, int i, int j) throws ShelfGenericException {
        int count = 0;
        if(!isValidTile(i,j) || visited[i][j]) // check if current tile is valid for point count
            return 0;
        visited[i][j] = true;
        if( !isOutOfBounds(i-1,j) && tiles[i][j].equals(tiles[i-1][j])){ //check if Tile on left side is same type
            count = count + exploreAdjacents(visited, i-1, j);
        }
        if( !isOutOfBounds(i+1,j) && tiles[i][j].equals(tiles[i+1][j])){ //check if Tile on right side is same type
            count = count + exploreAdjacents(visited, i+1, j);
        }
        if( !isOutOfBounds(i,j-1) && tiles[i][j].equals(tiles[i][j-1])){ //check if Tile on upper side is same type
            count = count + exploreAdjacents(visited, i, j-1);
        }
        if( !isOutOfBounds(i,j+1) && tiles[i][j].equals(tiles[i][j+1])){ //check if the Tile below is same type
            count = count + exploreAdjacents(visited, i, j+1);
        }
        return count+1;

    }

    /**
     * Tells if the coordinates are compatible with the shelf
     * @param row integer
     * @param column integer
     * @return True if the coordinates are valid
     */
    public boolean isOutOfBounds(int row, int column){
        if(row < 0 || row >= getRows() || column < 0 || column >= getColumns()){
            return true;
        }
        return false;
    }
    /**
     * Tells if the position is Empty or Invalid
     * @param row integer
     * @param column integer
     * @return TRUE if the Tile is Empty or Invalid
     */
    public boolean isValidTile(int row, int column) throws ShelfGenericException {
        Tile tile = getTile(row,column);
        return !tile.equals(Tile.Empty) && !tile.equals(Tile.Invalid);
    }

    public boolean isValidTile(Position position) throws ShelfGenericException {
        return isValidTile(position.getRow(),position.getColumn());
    }
    /**
     * @return rows attribute
     */
    public int getRows() {
        return rows;
    }
    /**
     * @return columns attribute
     */
    public int getColumns() {
        return columns;
    }
    /**
     * compare two Shelf objects
     * @param o is the object to compare with
     * @return true if they have the same attributes, false otherwise
     */
    public boolean equals(Object o) {
        boolean sameShelf = true;
        if(o == null || this.getClass() != o.getClass()){ //check if they are not same class
            return false;
        } else if (this == o) { //check if it's the same object
            return true;
        }
        if(rows != ((Shelf) o).getRows() || columns != ((Shelf) o).getColumns()) //check if they have different dimensions
            return false;
        for (int i = 0; sameShelf && i < rows; i++) { //check they have same tiles in shelf
            for (int j = 0; sameShelf && j < columns; j++) {
                try {
                    if (!tiles[i][j].equals(((Shelf) o).getTile(i, j))) {
                        sameShelf = false;
                    }
                } catch (ShelfGenericException e) {
                    return false;
                }
            }
        }
        return sameShelf;
    }

    /**
     * Convert the matrix to a printable String
     * @return the string visual conversion of the matrix
     */
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

    /**
     * generate hashcode from tiles matrix of shelf class
     * @return calculated hashcode
     */
    @Override
    public int hashCode(){
        return Arrays.deepHashCode(tiles); //deepHashCode is similar to HashCode but also applied to any sub-array of elements
    }
    /**
     * Get all the Tiles in a specific column of the shelf
     * @param column a column of the shelf
     * @return an ArrayList containing all the Tiles in the selected column of the shelf
     */
    public ArrayList<Tile> allTilesInColumn(int column) throws ShelfGenericException {
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int row = 0; row < getRows(); row++) {
                tiles.add(getTile(row, column));
            }
            return tiles;
    }

    /**
     * Get all the Tiles in a specific row of the shelf
     * @param row a row of the shelf
     * @return an ArrayList containing all the Tiles in the selected row of the shelf
     */
    public  ArrayList<Tile> allTilesInRow(int row) throws ShelfGenericException {
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int column = 0; column < getColumns(); column++) {
                tiles.add(getTile(row, column));
            }
            return tiles;
    }

    /**
     * Get all the Tiles in a specific row of the shelf
     * @return all the Tiles in a specific row of the shelf
     */
    public ArrayList<Tile> getCorners() throws ShelfGenericException {
        ArrayList<Tile> corners = new ArrayList<>();
        int rows = getRows();
        int columns = getColumns();
        corners.add(getTile(0,0));
        corners.add(getTile(0,columns-1));
        corners.add(getTile(rows-1,0));
        corners.add(getTile(rows-1,columns-1));
        return corners;
    }

    /**
     * This method is used to save the status of the shelf with a json object.
     * @return JSONObject with status.
     */
    public JSONObject toJson() {
        JSONObject shelfJson = new JSONObject();  // Object to return

        // Saving final parameters
        shelfJson.put("numRows", rows);
        shelfJson.put("numColumns", columns);

        // Looping to save the matrix
        JSONArray shelfMatrix = new JSONArray();
        JSONArray shelfRowJson;  // Single row
        List<String> shelfRow;
        for (int row = 0; row < rows; row++) {  // Iterating over the rows
            shelfRowJson = new JSONArray();  // Will be added into the JSON matrix
            shelfRow = new ArrayList<>();  // To convert from [] -> collection

            for (int col = 0; col < columns; col++) {  // Iterating over the columns
                shelfRow.add(tiles[row][col].toString());
            }
            shelfRowJson.addAll(shelfRow);  // Converting from List to JSONArray
            shelfMatrix.add(shelfRowJson);
        }

        shelfJson.put("matrix", shelfMatrix);

        return shelfJson;
    }
}
