package it.polimi.ingsw.shared;

import it.polimi.ingsw.server.CommonGoal;
import it.polimi.ingsw.server.CommonGoalsFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Board {

    private Tile[][] boardTiles;
    private List<Tile> tilesToDraw;
    private int numPlayers;
    private int numRows;
    private int numColumns;
    private ArrayList<CommonGoal> goals;

    /**
     * Constructor used to initialize board from default setup
     * @param numPlayers is the number of players of the game
     * @throws BoardGenericException when parsing errors occurs
     */
    public Board(int numPlayers) throws BoardGenericException {
        try{
            String JSONBoardInit = "src/main/resources/BoardInit.json";
            int numOfTileType = 22;

            goals = CommonGoalsFactory.createTwoGoals(numPlayers);
            tilesToDraw = new ArrayList<>();
            this.numPlayers = numPlayers;

            JSONParser jsonParser= new JSONParser(); //initialize parser
            Object obj = jsonParser.parse(new FileReader(JSONBoardInit)); //acquire JSON object file

            JSONObject objBoard = (JSONObject) ((JSONObject) obj).get("board");
            numRows = Math.toIntExact((long)(objBoard.get("numRows")));
            numColumns = Math.toIntExact((long)(objBoard.get("numColumns")));
            boardTiles = new Tile[numRows][numColumns];

            JSONArray arrayBoard = (JSONArray)((JSONObject)objBoard.get("players")).get(String.valueOf(numPlayers)); //acquire object "players" and then the matrix corresponding to number of players integer
            JSONArray boardLine;

            for (Tile t : Tile.values()){ //t assumes every Tile enum possible value and add to tilesToDraw if they aren't empty or invalid
                if(!t.equals(Tile.Empty) && !t.equals(Tile.Invalid)) {
                    for (int i = 0; i < numOfTileType; i++) {
                        tilesToDraw.add(t);
                    }
                }
            }
            Collections.shuffle(tilesToDraw); //mix tiles

            for(int i = 0; i < arrayBoard.size(); i++){ //copy entire matrix to board
                boardLine = (JSONArray) arrayBoard.get(i); //acquire line of matrix
                for(int j = 0; j < boardLine.size(); j++){
                    boardTiles[i][j] = Tile.valueOfLabel((String) boardLine.get(j)); //get label and copy Tile value in the matrix
                }
            }

        } catch (FileNotFoundException e){
            throw new BoardGenericException("Error while creating Board : json file not found");
        } catch (IOException | ClassCastException | NullPointerException e){
            throw new BoardGenericException("Error while creating Board : bad json parsing");
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * Constructor used to initialize board from previously generated JSON
     * @param jsonPath is the path where is located JSON file
     * @throws BoardGenericException when parsing error occurs
     */
    public Board(String jsonPath) throws BoardGenericException{
        try{
            goals = new ArrayList<>();
            tilesToDraw = new ArrayList<>();

            JSONParser jsonParser = new JSONParser(); //initialize parser
            Object obj = jsonParser.parse(new FileReader(jsonPath)); //acquire JSON object file

            JSONObject objBoard = (JSONObject) ((JSONObject) obj).get("board"); //acquire board object
            numPlayers = Math.toIntExact((long)(objBoard.get("numPlayers")));
            numRows = Math.toIntExact((long)(objBoard.get("numRows")));
            numColumns = Math.toIntExact((long)(objBoard.get("numColumns")));
            boardTiles = new Tile[numRows][numColumns];
            /* TODO discuss with others about the acquisition of CommonGoals from JSON
            JSONArray goalJson = (JSONArray)objBoard.get("goals");
            for(Object o: goalJson) {
                goals.add(CommonGoalsFactory.create_from_json((JSONObject) o));
            }
            */
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
        } catch (FileNotFoundException e){
            throw new BoardGenericException("Error while creating Board : json file not found");
        } catch (IOException | ClassCastException | NullPointerException e){
            throw new BoardGenericException("Error while creating Board : bad json parsing");
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
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
        Tile t;
        for(int i = 0; i<numRows; i++){ //add each valid and non-empty tile left on the board to tilesToDraw deck
            for(int j = 0; j<numColumns; j++){
                t = boardTiles[i][j];
                if (!t.equals(Tile.Invalid) && !t.equals(Tile.Empty)) {
                    tilesToDraw.add(t);
                }
            }
        }
        Collections.shuffle(tilesToDraw); //mix deck

        for(int i = 0; i<numRows; i++){ //draw tiles from tilesToDraw deck and use them to fill the board in valid cells
            for(int j = 0; j<numColumns; j++){
                if(!boardTiles[i][j].equals(Tile.Invalid)) {
                    if(tilesToDraw.size() > 0) { //check if there are tiles left in the deck
                        boardTiles[i][j] = tilesToDraw.remove(0);
                    }else{
                        throw new OutOfTilesException("No more tiles left in the deck");
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
                s = s.concat(boardTiles[i][j].toString() + " ");
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
        if (this == o) return true; //if it's same object
        if (o == null || getClass() != o.getClass()) return false; //if they are of different classes
        Board board = (Board) o;
        if(numRows != board.getNumRows() || numColumns != board.getNumColumns() || numPlayers != getNumPlayers()) //if they have different sizes or num of players
            return false;
        boolean same = true;
        for(int i = 0; same && i<numRows; i++){ //check they have same tiles in board
            for(int j = 0; same && j<numColumns; j++){
                if(!boardTiles[i][j].equals(board.getTile(i,j))){
                    same = false;
                }
            }
        }
        List<CommonGoal> Bgoals = board.getCommonGoals();
        if(goals.size() != Bgoals.size())
            same = false;
        for(int i = 0;same && i<goals.size(); i++){ //check if they have same commonGoals()
            if (!goals.get(i).equals(Bgoals.get(i))) {
                same = false;
            }
        }
        List<Tile> t1 = new ArrayList<>(tilesToDraw); //copy tileToDraw
        List<Tile> t2 = new ArrayList<>(board.getTilesToDraw());

        t1.sort(Comparator.comparing(Tile::toString)); //sort t1 list
        t2.sort(Comparator.comparing(Tile::toString));//sort t2 list

        if(!t1.equals(t2)) //check if the two lists contains the same elements in the same order
            same = false;

        return same;

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
            throw new BoardGenericException("Error while picking tile: pos is null pointer");
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
     * @param pos is the position object
     * @return Tile in position pos
     * @throws BoardGenericException when picking position is out of bound or pos is NullPointer
     */
    public Tile pickTile(Position pos) throws BoardGenericException { //maybe add pick tile by coordinates
        try {
            Tile selectedTile;
            selectedTile = boardTiles[pos.getRow()][pos.getColumn()];
            boardTiles[pos.getRow()][pos.getColumn()] = Tile.Empty;
            return selectedTile;
        } catch (NullPointerException e){
            throw new BoardGenericException("Error while picking tile: pos is null pointer");
        } catch (IndexOutOfBoundsException e){
            throw new BoardGenericException("Error while getting tile from Board : illegal coordinates");
        }
    }


    /**
     * Initialize the goals from CommonGoalsFactory
     */
    public void initializeGoals() {
        goals = CommonGoalsFactory.createTwoGoals(numPlayers);
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

    //complete when PartialMove is implemented
    /*public List<PartialMove> getValidMoves(List<PartialMove> moves) {

    }*/



}
