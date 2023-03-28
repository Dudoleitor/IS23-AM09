package it.polimi.ingsw.shared;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalsException;
import it.polimi.ingsw.server.CommonGoalsFactory;
import it.polimi.ingsw.server.PartialMove;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Board {

    private final Tile[][] boardTiles;
    private List<Tile> tilesToDraw;
    private final int numPlayers;
    private final int numRows;
    private final int numColumns;
    private ArrayList<CommonGoal> goals;

    //Constructors
    /**
     * Constructor used to initialize board from default setup
     * @param numPlayers is the number of players of the game
     * @throws BoardGenericException when parsing errors occurs
     */
    public Board(int numPlayers) throws BoardGenericException{
            this(pathToJsonObject(boardPathForNumberOfPlayers(numPlayers)));
            tilesToDraw =  createShuffledDeck(22);
            initializeGoals(); //generate two randomly picked goals
    }

    private List<Tile> createShuffledDeck(int numOfTilePerType){
        ArrayList<Tile> deck = new ArrayList<>();
        for (Tile t : Tile.values()){ //t assumes every Tile enum possible value and add to tilesToDraw if they aren't empty or invalid
            if(!t.equals(Tile.Empty) && !t.equals(Tile.Invalid)) {
                for (int i = 0; i < numOfTilePerType; i++) {
                    deck.add(t);
                }
            }
        }
        Collections.shuffle(deck); //mix tiles
        return deck;
    }
    public Board(JSONObject objBoard, List<JSONObject> objCommonGoals) throws BoardGenericException {
        this(objBoard);
        if(objCommonGoals != null){ //skipped in testing only
            initializeGoals(objCommonGoals);
        }
    }

    public static String boardPathForNumberOfPlayers(int number_of_players) throws BoardGenericException{
        switch(number_of_players){
            case(2):
                return "src/main/resources/BoardInit2players.json";
            case(3):
                return "src/main/resources/BoardInit3players.json";
            case(4):
                return "src/main/resources/BoardInit4players.json";
            default:
                throw new BoardGenericException("Error while creating Board : invalid number of players");
        }
    }

    /**
     * This static method returns the JSONObject from a json file,
     * it uses the "board" attribute.
     * @param jsonPath path to the json file
     * @return JSONObject with the content
     */
    public static JSONObject pathToJsonObject(String jsonPath) throws BoardGenericException {
        try {
            JSONParser jsonParser = new JSONParser(); //initialize parser
            Object obj = jsonParser.parse(new FileReader(jsonPath)); //acquire JSON object file
            return (JSONObject) ((JSONObject) obj).get("board"); //acquire board object
        } catch (IOException | ParseException e){
            throw new BoardGenericException("Error while generating Board from Json : file was not found");
        }
    }
    /**
     * Constructor used to initialize board from previously generated JSON
     * @param objBoard is a board object that serializes the board
     */
    private Board(JSONObject objBoard) throws BoardGenericException{
        try{
            goals = new ArrayList<>();
            tilesToDraw = new ArrayList<>();
            numPlayers = Math.toIntExact((long)(objBoard.get("numPlayers")));
            numRows = Math.toIntExact((long)(objBoard.get("numRows")));
            numColumns = Math.toIntExact((long)(objBoard.get("numColumns")));
            boardTiles = new Tile[numRows][numColumns];
            JSONArray arrayBoard = (JSONArray)objBoard.get("matrix"); //matrix is the copy of the board
            JSONArray arrayDeck = (JSONArray)objBoard.get("deck"); //deck is the copy of tilesToDraw
            JSONArray boardLine;

            for (Object o : arrayDeck) { //copy each Tile contained in arrayDeck to tilesToDraw
                tilesToDraw.add(Tile.valueOfLabel((String) o));
            }
            for(int i = 0; i < arrayBoard.size(); i++){ //copy entire matrix to board
                boardLine = (JSONArray) arrayBoard.get(i); //acquire line of matrix
                for(int j = 0; j < boardLine.size(); j++){
                    boardTiles[i][j] = Tile.valueOfLabel((String) boardLine.get(j));
                }
            }
            if(!checkValidBoard()){
                throw new BoardGenericException("Error while creating board : board is not a valid configuration for given num player");
            }
        } catch (ClassCastException | NullPointerException | TileGenericException e){
            throw new BoardGenericException("Error while creating Board : bad json parsing");
        }
    }
    private boolean checkValidBoard() throws BoardGenericException {
        boolean valid = true;
        JSONArray boardLine;
        JSONObject objBoard = pathToJsonObject(boardPathForNumberOfPlayers(numPlayers));
        JSONArray arrayBoard = (JSONArray) objBoard.get("matrix");

        Tile t;
        try {
            for (int i = 0; valid && i < arrayBoard.size(); i++) { //copy entire matrix to board
                boardLine = (JSONArray) arrayBoard.get(i); //acquire line of matrix
                for (int j = 0; valid && j < boardLine.size(); j++) {
                    t = Tile.valueOfLabel((String) boardLine.get(j));
                    if (!(boardTiles[i][j].equals(t)) && (boardTiles[i][j].equals(Tile.Invalid) || t.equals(Tile.Invalid))) { //check if boards has invalid tiles in same position and nowhere else
                        valid = false;
                    }
                }
            }
            return valid;
        } catch (TileGenericException e){
            return false;
        }
    }
/*public String toString() {
    return "Board{" +
            "boardTiles=" + Arrays.toString(boardTiles) +
            ", tilesToDraw=" + tilesToDraw +
            ", numPlayers=" + numPlayers +
            ", goalsFactory=" + goalsFactory +
            ", goals=" + goals +
            '}';
}*/

    /**
     * Randomly refill the board removing every valid tile previously contained
     * @throws OutOfTilesException whenever tilesToDraw deck is empty, so no more tiles can be drawn
     */
    public void fill() throws OutOfTilesException{
        Arrays.stream(boardTiles).flatMap(Arrays::stream) //create stream from matrix
                .filter(x -> !x.equals(Tile.Invalid) && !x.equals(Tile.Empty)) //preserve any valid tile
                .forEach(tilesToDraw::add);

        Collections.shuffle(tilesToDraw); //mix deck

        for(int i = 0; i<numRows; i++){ //draw tiles from tilesToDraw deck and use them to fill the board in valid cells
            for(int j = 0; j<numColumns; j++){
                if(!boardTiles[i][j].equals(Tile.Invalid)) {
                    if(tilesToDraw.size() > 0) { //check if there are tiles left in the deck
                        boardTiles[i][j] = tilesToDraw.remove(0);
                    }else{
                        throw new OutOfTilesException("The END is near : No more tiles left in the deck");
                    }
                }

            }
        }

    }

    /**
     * Convert the matrix to a printable String
     * @return the string visual conversion of the matrix
     */
    @Override
    public String toString(){
        String s = "";
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numColumns; j++){
                s = s.concat(boardTiles[i][j].toColorFulString() + " ");
            }
            s = s.concat("\n");
        }
        return s;
    }

    /**
     * compare two Board objects
     * @param o is the object to compare with
     * @return true if they have the same attributes, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        try {
            if (this == o) return true; //if it's same object
            if (o == null || getClass() != o.getClass()) return false; //if they are of different classes
            Board board = (Board) o;
            if (numRows != board.getNumRows() || numColumns != board.getNumColumns() || numPlayers != getNumPlayers()) //if they have different sizes or num of players
                return false;
            for (int i = 0; i < numRows; i++) { //check they have same tiles in board
                for (int j = 0; j < numColumns; j++) {
                    if (!boardTiles[i][j].equals(board.getTile(i, j))) {
                        return false;
                    }
                }
            }
            List<CommonGoal> Bgoals = board.getCommonGoals();
            if (goals.size() != Bgoals.size() || !goals.containsAll(Bgoals))
                return false;

            List<Tile> t1 = new ArrayList<>(tilesToDraw); //copy tileToDraw
            List<Tile> t2 = new ArrayList<>(board.getTilesToDraw());

            t1.sort(Comparator.comparing(Tile::toString)); //sort t1 list
            t2.sort(Comparator.comparing(Tile::toString));//sort t2 list

            if (!t1.equals(t2)) //check if the two lists contains the same elements in the same order
                return false;

            return true;

        } catch (BoardGenericException e){
            return false;
        }
    }
    /**
     * generate hashcode from private attributes of board class
     * @return calculated hashcode
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(tilesToDraw, numPlayers, goals);
        result = 31 * result + Arrays.deepHashCode(boardTiles);
        return result;
    }

    /**
     * @return numPlayer attribute
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * @return numRows attribute
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * @return numColumns attribute
     */
    public int getNumColumns() {
        return numColumns;
    }

    /**
     * Get tile in position pos
     * @param pos is the position object
     * @return Tile in position pos
     * @throws BoardGenericException when getting position is out of bound or pos is NullPointer
     */
    public Tile getTile(Position pos) throws BoardGenericException {
        try {
            return getTile(pos.getRow(), pos.getColumn());
        } catch (NullPointerException e){
            throw new BoardGenericException("Error while getting tile: pos is null pointer");
        }
    }

    /**
     * Get tile in position (i,j)
     * @param i is row index
     * @param j is column index
     * @return Tile in position (i,j)
     * @throws BoardGenericException when getting coordinates are out of bound
     */
    public Tile getTile(int i, int j) throws BoardGenericException{
        try {
            return boardTiles[i][j];
        } catch (IndexOutOfBoundsException e){
            throw new BoardGenericException("Error while getting tile from Board : illegal coordinates");
        }
    }
    /**
     * Pick a tile from the board removing it from the board
     * @param i is the x coord
     * @param j is the y coord
     * @return Tile in position pos
     * @throws BoardGenericException when picking position is out of bound or pos is NullPointer
     */
    public Tile pickTile(int i, int j) throws BoardGenericException { //maybe add pick tile by coordinates
        try {
            Tile selectedTile;
            selectedTile = boardTiles[i][j];
            if(selectedTile.equals(Tile.Invalid))
                throw new BoardGenericException("Error while picking Tile : Chosen one is Invalid type");
            boardTiles[i][j] = Tile.Empty;
            return selectedTile;
        } catch (IndexOutOfBoundsException e){
            throw new BoardGenericException("Error while picking tile from Board : illegal coordinates");
        }
    }

    /**
     * Pick a tile from the board removing it from the board
     * @param pos is the position object
     * @return Tile in position pos
     * @throws BoardGenericException when picking position is out of bound or pos is NullPointer
     */
    public Tile pickTile(Position pos) throws BoardGenericException { //maybe add pick tile by coordinates
        try {
            return pickTile(pos.getRow(), pos.getColumn());
        } catch (NullPointerException e){
            throw new BoardGenericException("Error while picking tile: pos is null pointer");
        }
    }


    /**
     * Initialize two random goals from CommonGoalsFactory
     */
    private void initializeGoals() throws BoardGenericException {
        try {
            goals = CommonGoalsFactory.createTwoGoals(numPlayers);
        } catch (CommonGoalsException e){
            throw new BoardGenericException("Error while creating board : common goal exception");
        }
    }

    /**
     * Initialize the common goals passed as json objects
     * @param commonGoals a List of serialized common goals
     */
    private void initializeGoals(List<JSONObject> commonGoals) throws BoardGenericException {
        goals = new ArrayList<>();
        try {
            for (JSONObject jsonObject : commonGoals) {
                goals.add(CommonGoalsFactory.create_from_json(jsonObject));
            }
        } catch (CommonGoalsException e){
            throw new BoardGenericException("Error while creating board : common goal exception");
        }
    }
    /**
     * get the CommonGoals
     * @return CommonGoals array copy
     */
    public List<CommonGoal> getCommonGoals() {
        return new ArrayList<>(goals);
    }

    public List<Tile> getTilesToDraw() {
        return new ArrayList<>(tilesToDraw);
    }


    /**
     * @param partialMove is the move that the player is building
     * @return the possible positions that the player can add to the move
     */
    public List<Position> getValidPositions(PartialMove partialMove) throws BoardGenericException {
        List<Position> positions = new ArrayList<>();

        //extreme case when partialMove has no positions inside
        if(partialMove.getBoardPositions().size() == 0) {
            try {
                IntStream.range(0, getNumRows()).forEach(i -> //create stream of i
                        IntStream.range(0, getNumColumns()) // create stream of j
                                .filter(j -> { //filter not valid positions
                                    try {
                                        return hasFreeSide(i, j);
                                    } catch (BoardGenericException e) {
                                        throw new BoardRuntimeException(e.getMessage());
                                    }
                                })
                                .forEach(j -> positions.add(new Position(i, j)))
                );
            } catch (BoardRuntimeException e){
                throw new BoardGenericException(e.getMessage());
            }
        }
        //if the move is complete --> it has maxNumMoves positions inside
        if(partialMove.getBoardPositions().size() >= partialMove.getMaxNumMoves()) {
            return positions;
        }

        /*if(partialMove.getBoardPositions().size() >= 1 && partialMove.getBoardPositions().size() < partialMove.getMaxNumMoves()){

        }*/

        return positions;
    }


    /**
     * @param row is the given row
     * @param column is the given column
     * @return true if position has at least one free adjacent side
     */
    public boolean hasFreeSide(int row, int column) throws BoardGenericException {
        Position p = new Position(row, column);
        return hasFreeSide(p);
    }
    /**
     * @param position is the given row
     * @return true if position has at least one free adjacent side
     */
    public boolean hasFreeSide(Position position) throws BoardGenericException {
        if(isOutOfBounds(position.getRow(), position.getColumn()))
            throw new BoardGenericException("Error while checking hasFreeSide on board : Index Out Of Bounds");

        if(isNotValid(position))
            return false;

        if(position.getRow() == 0 ||
                position.getColumn() == 0 ||
                position.getRow() == getNumRows() - 1 ||
                position.getColumn() == getNumColumns() - 1)
            //check the extreme cases where pos has at least one free side for sure
            return true;

        return position.neighbours().stream().anyMatch(pos -> isNotValid(pos));
    }

    //check if pos in the board has one adjacent empty (or invalid) cell
    private boolean isNotValid (Position pos){
        try {
            return getTile(pos).equals(Tile.Empty) || getTile(pos).equals(Tile.Invalid);
        } catch (BoardGenericException e) {
            return true;
        }
    };

    /**
     * Tells if the coordinates are compatible with the board
     * @param row integer
     * @param column integer
     * @return True if the coordinates are valid
     */
    public boolean isOutOfBounds(int row, int column){
        if(     row < 0 ||
                row >= getNumRows() ||
                column < 0 ||
                column >= getNumColumns()){
            return true;
        }
        return false;
    }

    /**
     * This method is used to save the status of the board with a json object.
     * @return JSONObject with status.
     */
    public JSONObject toJson() {
        JSONObject boardJson = new JSONObject();  // Object to return

        // Saving final parameters
        boardJson.put("numPlayers", numPlayers);
        boardJson.put("numRows", numRows);
        boardJson.put("numColumns", numColumns);

        // Looping to save the matrix
        JSONArray boardMatrix = new JSONArray();
        JSONArray boardRowJson;  // Single row
        List<String> boardRow;
        for (int row = 0; row < numRows; row++) {  // Iterating over the rows
            boardRowJson = new JSONArray();  // Will be added into the JSON matrix
            boardRow = new ArrayList<>();  // To convert from [] -> collection

            for (int col = 0; col < numColumns; col++) {  // Iterating over the columns
                boardRow.add(boardTiles[row][col].toString());
            }
            boardRowJson.addAll(boardRow);  // Converting from List to JSONArray
            boardMatrix.add(boardRowJson);
        }

        boardJson.put("matrix", boardMatrix);

        JSONArray boardDeck = new JSONArray();
        // Converting from List to JSONArray
        boardDeck.addAll(tilesToDraw
                .stream()
                .map(Tile::toString)
                .collect(Collectors.toList()));
        boardJson.put("deck", boardDeck);
        return boardJson;
    }



}
