package it.polimi.ingsw.shared;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.InvalidMoveException;
import it.polimi.ingsw.server.PartialMove;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Board implements Jsonable {

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
     * @throws JsonBadParsingException when parsing errors occurs
     */
    public Board(int numPlayers) throws JsonBadParsingException{
            this(Jsonable.pathToJsonObject(boardPathForNumberOfPlayers(numPlayers),Board.class));
            tilesToDraw =  createShuffledDeck(22);
            initializeGoals(); //generate two randomly picked goals
    }

    /**
     * Constructor by copy of Board
     * @param toCopy
     * @throws JsonBadParsingException
     */
    public Board(Board toCopy) throws JsonBadParsingException {
        this(toCopy.toJson());
        goals = new ArrayList<>();
        for(CommonGoal cg : toCopy.getCommonGoals()){
            goals.add(new CommonGoal(cg));
        }
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
    public Board(JSONObject objBoard, List<JSONObject> objCommonGoals) throws JsonBadParsingException {
        this(objBoard);
        if(objCommonGoals != null){
            initializeGoals(objCommonGoals);
        }
    }

    public static String boardPathForNumberOfPlayers(int number_of_players){
        String path;
        if(number_of_players <= Constants.maxSupportedPlayers &&
                number_of_players >= Constants.minSupportedPlayers){
            path = Constants.jsonPathForBoard.replace("?",String.valueOf(number_of_players));
        }
        else{
            throw new BoardRuntimeException("Error while creating Board : invalid number of players");
        }
        return path;
    }

    /**
     * Constructor used to initialize board from previously generated JSON
     * @param objBoard is a board object that serializes the board
     */
    private Board(JSONObject objBoard) throws JsonBadParsingException{
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
                throw new JsonBadParsingException("Error while creating Board : board is not a valid configuration for given num player");
            }
        } catch (ClassCastException | NullPointerException | TileGenericException e){
            throw new JsonBadParsingException("Error while creating Board : bad json parsing");
        }
    }

    /**
     *Check if the board is valid
     * @return is board is valid comparing to standard configuration for given number of players
     */
    private boolean checkValidBoard(){
        boolean valid = true;
        JSONArray boardLine;
        JSONObject objBoard;
        try {
            objBoard = Jsonable.pathToJsonObject(boardPathForNumberOfPlayers(numPlayers),Board.class);
        } catch (JsonBadParsingException e) {
            throw new BoardRuntimeException("Error while checking board validity : JsonPath to numPlayer is not valid");
        }
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
        String str = "Board:\n";
        int rows = getNumRows();
        int columns = getNumColumns();
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < columns; col++){
                try {
                    str = str.concat(getTile(row,col).toColorFulString()+" ");
                } catch (BadPositionException e) {
                    //Will never be executed
                }
            }
            str = str.concat("\n");
        }
        str = str.concat("Common Goals:\n");
        if(getCommonGoals().size() == 0){
            str = str.concat(Color.coloredString("None",Color.Yellow));
        }
        for(CommonGoal cg : getCommonGoals()){
            str = str.concat(cg.toString());
        }
        return str;
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

        } catch (BadPositionException e){
            return false;
        }
    }

    /**
     * Checks if two Board objects have the same board without checking all other
     * parameters
     * @param other an other Board
     * @return true if the Boards (Tile[][]) are equal
     */
    public boolean sameBoard(Board other){
        if(other == null){
            return false;
        }
        if(this == other){
            return true;
        }
        if (numRows != other.getNumRows() || numColumns != other.getNumColumns()) //if they have different sizes or num of players
            return false;
        for (int i = 0; i < numRows; i++) { //check they have same tiles in board
            for (int j = 0; j < numColumns; j++) {
                try {
                    if (!boardTiles[i][j].equals(other.getTile(i, j))) {
                        return false;
                    }
                } catch (BadPositionException e) {
                    return false;
                }
            }
        }
        return true;
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
     * @throws BadPositionException when getting position is out of bound or pos is NullPointer
     */
    public Tile getTile(Position pos) throws BadPositionException {
        try {
            return getTile(pos.getRow(), pos.getColumn());
        } catch (NullPointerException e){
            throw new BoardRuntimeException("Error while getting tile: pos is null pointer");
        }
    }

    /**
     * Get tile in position (i,j)
     * @param row is row index
     * @param column is column index
     * @return Tile in position (i,j)
     * @throws BadPositionException when getting coordinates are out of bound
     */
    public Tile getTile(int row, int column) throws BadPositionException{
        try {
            return boardTiles[row][column];
        } catch (IndexOutOfBoundsException e){
            throw new BadPositionException("Error while getting tile from Board : illegal coordinates");
        }
    }
    /**
     * Pick a tile from the board removing it from the board
     * @param row is the x coord
     * @param column is the y coord
     * @return Tile in position pos
     * @throws BadPositionException when picking position is out of bound or pos is NullPointer
     */
    public Tile pickTile(int row, int column) throws BadPositionException { //maybe add pick tile by coordinates
        try {
            Tile selectedTile;
            selectedTile = boardTiles[row][column];
            if(selectedTile.equals(Tile.Invalid))
                throw new BadPositionException("Error while picking Tile : Chosen one is Invalid type");
            boardTiles[row][column] = Tile.Empty;
            return selectedTile;
        } catch (IndexOutOfBoundsException e){
            throw new BadPositionException("Error while picking tile from Board : illegal coordinates");
        }
    }

    /**
     * Pick a tile from the board removing it from the board
     * @param pos is the position object
     * @return Tile in position pos
     * @throws BadPositionException when picking position is out of bound
     */
    public Tile pickTile(Position pos) throws BadPositionException { //maybe add pick tile by coordinates
        try {
            return pickTile(pos.getRow(), pos.getColumn());
        } catch (NullPointerException e){
            throw new BoardRuntimeException("Error while picking tile: pos is null pointer");
        }
    }


    /**
     * Initialize two random goals from CommonGoalsFactory
     */
    private void initializeGoals(){
        try {
            goals = CommonGoal.createTwoGoals(numPlayers);
        } catch (JsonBadParsingException e){
            throw new BoardRuntimeException("Error while creating board : common goal exception");
        }
    }

    /**
     * Initialize the common goals passed as json objects
     * @param commonGoals a List of serialized common goals
     */
    private void initializeGoals(List<JSONObject> commonGoals){
        goals = new ArrayList<>();
        for (JSONObject jsonObject : commonGoals) {
            goals.add(new CommonGoal(jsonObject));
        }
    }
    /**
     * get the CommonGoals
     * @return CommonGoals array copy
     */
    public List<CommonGoal> getCommonGoals() {
        return goals;
    }

    /**
     * @return deck of tiles
     */
    public List<Tile> getTilesToDraw() {
        return new ArrayList<>(tilesToDraw);
    }


    /**
     * @param partialMove is the move that the player is building
     * @return the possible positions that the player can add to the move
     */
    public List<Position> getValidPositions(PartialMove partialMove) throws InvalidMoveException {
        List<Position> positions = new ArrayList<>();

        if (partialMove == null) {
            return positions;
        }

        List<Position> movePositions = partialMove.getBoardPositions();
        if(!Position.areAligned(movePositions))
            throw new InvalidMoveException("Positions are not alligned");
        if (movePositions.size() >= partialMove.getMaxNumMoves()) { //if the move is complete --> it has maxNumMoves positions inside
            return positions;
        }

        Predicate<Position> validfreeSide = (p) ->{
                        return !isOutOfBounds(p)
                        && !isNotValid(p) //check it isn't Empty or Invalid
                        && hasFreeSide(p);};

        List<Position> temL = new ArrayList<>();

        switch(movePositions.size()) {
            case 0: //case when partialMove has no positions inside
                IntStream.range(0, getNumRows()).forEach(i -> //create stream of i
                        IntStream.range(0, getNumColumns()) // create stream of j
                                .filter(j -> hasFreeSide(i, j))
                                .forEach(j -> temL.add(new Position(i, j))));
                positions.addAll(temL);
                break;

            case 1:
                positions = movePositions.get(0).neighbours().stream()
                        .filter(p -> validfreeSide.test(p))
                        .collect(Collectors.toList());
                break;

            default:
                if (Position.sameRow(movePositions)) {
                    positions = movePositions.stream()
                            .map(Position::neighbours)
                            .flatMap(Collection::stream)
                            .filter(p -> validfreeSide.test(p) &&
                                    !movePositions.contains(p) && //check it wasn't already in partialMove
                                    Position.sameRow(movePositions, p)) //check is aligned with other tiles
                            .collect(Collectors.toList());
                } else {
                    positions = movePositions.stream()
                            .map(Position::neighbours)
                            .flatMap(Collection::stream)
                            .filter(p -> validfreeSide.test(p) &&
                                    !movePositions.contains(p) && //check it wasn't already in partialMove
                                    Position.sameColumn(movePositions, p))
                            .collect(Collectors.toList());
                }
                break;
        }

        return positions;

    }

    /**
     * @param row is the given row
     * @param column is the given column
     * @return true if position has at least one free adjacent side
     */
    public boolean hasFreeSide(int row, int column){
        Position p = new Position(row, column);
        return hasFreeSide(p);
    }
    /**
     * @param position is the given row
     * @return true if position has at least one free adjacent side
     */
    public boolean hasFreeSide(Position position){
        if(isOutOfBounds(position.getRow(), position.getColumn()))
            throw new BoardRuntimeException("Error while checking hasFreeSide on board : Index Out Of Bounds");

        if(isNotValid(position))
            return false;

        if(position.getRow() == 0 ||
                position.getColumn() == 0 ||
                position.getRow() == getNumRows() - 1 ||
                position.getColumn() == getNumColumns() - 1)
            //check the extreme cases where pos has at least one free side for sure
            return true;

        return position.neighbours().stream().anyMatch(this::isNotValid); //return true if there is any neighbour which is either Empty or Invalid
    }

    //check if pos in the board has one adjacent empty (or invalid) cell
    private boolean isNotValid (Position pos){
        try {
            return getTile(pos).equals(Tile.Empty) || getTile(pos).equals(Tile.Invalid);
        } catch (BadPositionException e) {
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
        return row < 0 ||
                row >= getNumRows() ||
                column < 0 ||
                column >= getNumColumns();
    }

    public boolean isOutOfBounds(Position pos){
        return isOutOfBounds(pos.getRow(), pos.getColumn());
    }

    /**
     * This method is used to save the status of the board with a json object.
     * @return JSONObject with status.
     */
    public JSONObject toJson() {
        JSONObject boardJson = new JSONObject();  // Object to return

        // Saving final parameters
        boardJson.put("numPlayers", Long.valueOf(numPlayers));
        boardJson.put("numRows", Long.valueOf(numRows));
        boardJson.put("numColumns", Long.valueOf(numColumns));

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
