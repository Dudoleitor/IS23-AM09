package it.polimi.ingsw.shared;

import it.polimi.ingsw.server.AbstractCommonGoal;
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
    private final CommonGoalsFactory goalsFactory = new CommonGoalsFactory();
    private ArrayList<AbstractCommonGoal> goals = new ArrayList<>();


    public Board(int numRows, int numColumns, int numPlayers) throws BoardGenericException {
        boardTiles = new Tile[numRows][numColumns];
        this.numPlayers = numPlayers;
        this.numColumns = numColumns;
        this.numRows = numRows;


        //0 is the uppest row
        //numRow is the lowest row


        if (numPlayers == 2) {
            for (int i = 0; i < numColumns; i++) {
                //switch (i) {
                if (i == 0) {
                    boardTiles[0][i] = Tile.Invalid;
                    boardTiles[1][i] = Tile.Invalid;
                    boardTiles[2][i] = Tile.Invalid;
                    boardTiles[3][i] = Tile.Invalid;
                    boardTiles[4][i] = Tile.Invalid;
                    boardTiles[5][i] = Tile.Invalid;
                    boardTiles[6][i] = Tile.Invalid;
                    boardTiles[7][i] = Tile.Invalid;
                    boardTiles[8][i] = Tile.Invalid;
                } else if (i == 1) {
                    boardTiles[0][i] = Tile.Invalid;
                    boardTiles[1][i] = Tile.Invalid;
                    boardTiles[2][i] = Tile.Invalid;
                    boardTiles[3][i] = Tile.Invalid;
                    boardTiles[4][i] = Tile.Empty;
                    boardTiles[5][i] = Tile.Empty;
                    boardTiles[6][i] = Tile.Invalid;
                    boardTiles[7][i] = Tile.Invalid;
                    boardTiles[8][i] = Tile.Invalid;
                } else if (i == 2) {
                    boardTiles[0][i] = Tile.Invalid;
                    boardTiles[1][i] = Tile.Invalid;
                    boardTiles[2][i] = Tile.Invalid;
                    boardTiles[3][i] = Tile.Empty;
                    boardTiles[4][i] = Tile.Empty;
                    boardTiles[5][i] = Tile.Empty;
                    boardTiles[6][i] = Tile.Invalid;
                    boardTiles[7][i] = Tile.Invalid;
                    boardTiles[8][i] = Tile.Invalid;
                } else if (i == 3) {
                    boardTiles[0][i] = Tile.Invalid;
                    boardTiles[1][i] = Tile.Empty;
                    boardTiles[2][i] = Tile.Empty;
                    boardTiles[3][i] = Tile.Empty;
                    boardTiles[4][i] = Tile.Empty;
                    boardTiles[5][i] = Tile.Empty;
                    boardTiles[6][i] = Tile.Empty;
                    boardTiles[7][i] = Tile.Invalid;
                    boardTiles[8][i] = Tile.Invalid;
                } else if (i == 4) {
                    boardTiles[0][i] = Tile.Invalid;
                    boardTiles[1][i] = Tile.Empty;
                    boardTiles[2][i] = Tile.Empty;
                    boardTiles[3][i] = Tile.Empty;
                    boardTiles[4][i] = Tile.Empty;
                    boardTiles[5][i] = Tile.Empty;
                    boardTiles[6][i] = Tile.Empty;
                    boardTiles[7][i] = Tile.Empty;
                    boardTiles[8][i] = Tile.Invalid;
                } else if (i == 5) {
                    boardTiles[0][i] = Tile.Invalid;
                    boardTiles[1][i] = Tile.Invalid;
                    boardTiles[2][i] = Tile.Empty;
                    boardTiles[3][i] = Tile.Empty;
                    boardTiles[4][i] = Tile.Empty;
                    boardTiles[5][i] = Tile.Empty;
                    boardTiles[6][i] = Tile.Empty;
                    boardTiles[7][i] = Tile.Empty;
                    boardTiles[8][i] = Tile.Invalid;
                } else if (i == 6) {
                    boardTiles[0][i] = Tile.Invalid;
                    boardTiles[1][i] = Tile.Invalid;
                    boardTiles[2][i] = Tile.Invalid;
                    boardTiles[3][i] = Tile.Empty;
                    boardTiles[4][i] = Tile.Empty;
                    boardTiles[5][i] = Tile.Empty;
                    boardTiles[6][i] = Tile.Invalid;
                    boardTiles[7][i] = Tile.Invalid;
                    boardTiles[8][i] = Tile.Invalid;
                } else if (i == 7) {
                    boardTiles[0][i] = Tile.Invalid;
                    boardTiles[1][i] = Tile.Invalid;
                    boardTiles[2][i] = Tile.Invalid;
                    boardTiles[3][i] = Tile.Empty;
                    boardTiles[4][i] = Tile.Empty;
                    boardTiles[5][i] = Tile.Invalid;
                    boardTiles[6][i] = Tile.Invalid;
                    boardTiles[7][i] = Tile.Invalid;
                    boardTiles[8][i] = Tile.Invalid;
                } else if (i == 8) {
                    boardTiles[0][i] = Tile.Invalid;
                    boardTiles[1][i] = Tile.Invalid;
                    boardTiles[2][i] = Tile.Invalid;
                    boardTiles[3][i] = Tile.Invalid;
                    boardTiles[4][i] = Tile.Invalid;
                    boardTiles[5][i] = Tile.Invalid;
                    boardTiles[6][i] = Tile.Invalid;
                    boardTiles[7][i] = Tile.Invalid;
                    boardTiles[8][i] = Tile.Invalid;
                } else {
                    throw new BoardGenericException("Error while generating Board: Invalid switch case");
                }
            }
        } else if (numPlayers == 3) {

            for (int i = 0; i < numColumns; i++) {

                    if(i == 0) {
                        boardTiles[0][i] = Tile.Invalid;
                        boardTiles[1][i] = Tile.Invalid;
                        boardTiles[2][i] = Tile.Invalid;
                        boardTiles[3][i] = Tile.Invalid;
                        boardTiles[4][i] = Tile.Invalid;
                        boardTiles[5][i] = Tile.Empty;
                        boardTiles[6][i] = Tile.Invalid;
                        boardTiles[7][i] = Tile.Invalid;
                        boardTiles[8][i] = Tile.Invalid;
                    } else if(i == 2 || i == 6) {
                        boardTiles[0][i] = Tile.Invalid;
                        boardTiles[1][i] = Tile.Invalid;
                        boardTiles[2][i] = Tile.Empty;
                        boardTiles[3][i] = Tile.Empty;
                        boardTiles[4][i] = Tile.Empty;
                        boardTiles[5][i] = Tile.Empty;
                        boardTiles[6][i] = Tile.Empty;
                        boardTiles[7][i] = Tile.Invalid;
                        boardTiles[8][i] = Tile.Invalid;
                    } else if(i == 1) {

                        boardTiles[0][i] = Tile.Invalid;
                        boardTiles[1][i] = Tile.Invalid;
                        boardTiles[2][i] = Tile.Invalid;
                        boardTiles[3][i] = Tile.Invalid;

                        boardTiles[4][i] = Tile.Empty;
                        boardTiles[5][i] = Tile.Empty;

                        boardTiles[6][i] = Tile.Invalid;
                        boardTiles[7][i] = Tile.Invalid;
                        boardTiles[8][i] = Tile.Invalid;
                    } else if(i == 3) {

                        boardTiles[0][i] = Tile.Empty;
                        boardTiles[1][i] = Tile.Empty;
                        boardTiles[2][i] = Tile.Empty;
                        boardTiles[3][i] = Tile.Empty;
                        boardTiles[4][i] = Tile.Empty;
                        boardTiles[5][i] = Tile.Empty;
                        boardTiles[6][i] = Tile.Empty;
                        boardTiles[7][i] = Tile.Invalid;
                        boardTiles[8][i] = Tile.Invalid;
                    } else if(i == 4) {

                        boardTiles[0][i] = Tile.Invalid;
                        boardTiles[1][i] = Tile.Empty;
                        boardTiles[2][i] = Tile.Empty;
                        boardTiles[3][i] = Tile.Empty;
                        boardTiles[4][i] = Tile.Empty;
                        boardTiles[5][i] = Tile.Empty;
                        boardTiles[6][i] = Tile.Empty;
                        boardTiles[7][i] = Tile.Empty;
                        boardTiles[8][i] = Tile.Invalid;
                    } else if(i == 5) {

                        boardTiles[0][i] = Tile.Invalid;
                        boardTiles[1][i] = Tile.Invalid;
                        boardTiles[2][i] = Tile.Empty;
                        boardTiles[3][i] = Tile.Empty;
                        boardTiles[4][i] = Tile.Empty;
                        boardTiles[5][i] = Tile.Empty;
                        boardTiles[6][i] = Tile.Empty;
                        boardTiles[7][i] = Tile.Empty;
                        boardTiles[8][i] = Tile.Empty;
                    } else if(i == 7) {

                        boardTiles[0][i] = Tile.Invalid;
                        boardTiles[1][i] = Tile.Invalid;
                        boardTiles[2][i] = Tile.Invalid;
                        boardTiles[3][i] = Tile.Empty;
                        boardTiles[4][i] = Tile.Empty;
                        boardTiles[5][i] = Tile.Invalid;
                        boardTiles[6][i] = Tile.Invalid;
                        boardTiles[7][i] = Tile.Invalid;
                        boardTiles[8][i] = Tile.Invalid;
                    } else if(i == 8) {

                        boardTiles[0][i] = Tile.Invalid;
                        boardTiles[1][i] = Tile.Invalid;
                        boardTiles[2][i] = Tile.Invalid;
                        boardTiles[3][i] = Tile.Empty;
                        boardTiles[4][i] = Tile.Invalid;
                        boardTiles[5][i] = Tile.Invalid;
                        boardTiles[6][i] = Tile.Invalid;
                        boardTiles[7][i] = Tile.Invalid;
                        boardTiles[8][i] = Tile.Invalid;
                    } else {
                        throw new BoardGenericException("Error while generating Board: Invalid switch case");
                }
            }

        } else if (numPlayers == 4) {

            for (int i = 0; i < numColumns; i++) {
                if(i == 0) {
                    boardTiles[0][i] = Tile.Invalid;
                    boardTiles[1][i] = Tile.Invalid;
                    boardTiles[2][i] = Tile.Invalid;
                    boardTiles[3][i] = Tile.Invalid;
                    boardTiles[4][i] = Tile.Empty;
                    boardTiles[5][i] = Tile.Empty;
                    boardTiles[6][i] = Tile.Invalid;
                    boardTiles[7][i] = Tile.Invalid;
                    boardTiles[8][i] = Tile.Invalid;
                } else if(i == 2 || i == 6) {

                    boardTiles[0][i] = Tile.Invalid;
                    boardTiles[1][i] = Tile.Invalid;
                    boardTiles[2][i] = Tile.Empty;
                    boardTiles[3][i] = Tile.Empty;
                    boardTiles[4][i] = Tile.Empty;
                    boardTiles[5][i] = Tile.Empty;
                    boardTiles[6][i] = Tile.Empty;
                    boardTiles[7][i] = Tile.Invalid;
                    boardTiles[8][i] = Tile.Invalid;
                } else if(i == 1 || i == 7) {

                    boardTiles[0][i] = Tile.Invalid;
                    boardTiles[1][i] = Tile.Invalid;
                    boardTiles[2][i] = Tile.Invalid;
                    boardTiles[3][i] = Tile.Empty;
                    boardTiles[4][i] = Tile.Empty;
                    boardTiles[5][i] = Tile.Empty;
                    boardTiles[6][i] = Tile.Invalid;
                    boardTiles[7][i] = Tile.Invalid;
                    boardTiles[8][i] = Tile.Invalid;
                } else if(i == 3) {

                    boardTiles[0][i] = Tile.Empty;
                    boardTiles[1][i] = Tile.Empty;
                    boardTiles[2][i] = Tile.Empty;
                    boardTiles[3][i] = Tile.Empty;
                    boardTiles[4][i] = Tile.Empty;
                    boardTiles[5][i] = Tile.Empty;
                    boardTiles[6][i] = Tile.Empty;
                    boardTiles[7][i] = Tile.Empty;
                    boardTiles[8][i] = Tile.Invalid;
                } else if(i == 4) {

                    boardTiles[0][i] = Tile.Empty;
                    boardTiles[1][i] = Tile.Empty;
                    boardTiles[2][i] = Tile.Empty;
                    boardTiles[3][i] = Tile.Empty;
                    boardTiles[4][i] = Tile.Empty;
                    boardTiles[5][i] = Tile.Empty;
                    boardTiles[6][i] = Tile.Empty;
                    boardTiles[7][i] = Tile.Empty;
                    boardTiles[8][i] = Tile.Empty;
                } else if(i == 5) {

                    boardTiles[0][i] = Tile.Invalid;
                    boardTiles[1][i] = Tile.Empty;
                    boardTiles[2][i] = Tile.Empty;
                    boardTiles[3][i] = Tile.Empty;
                    boardTiles[4][i] = Tile.Empty;
                    boardTiles[5][i] = Tile.Empty;
                    boardTiles[6][i] = Tile.Empty;
                    boardTiles[7][i] = Tile.Empty;
                    boardTiles[8][i] = Tile.Empty;
                } else if(i == 8) {

                    boardTiles[0][i] = Tile.Invalid;
                    boardTiles[1][i] = Tile.Invalid;
                    boardTiles[2][i] = Tile.Invalid;
                    boardTiles[3][i] = Tile.Empty;
                    boardTiles[4][i] = Tile.Empty;
                    boardTiles[5][i] = Tile.Invalid;
                    boardTiles[6][i] = Tile.Invalid;
                    boardTiles[7][i] = Tile.Invalid;
                    boardTiles[8][i] = Tile.Invalid;
                } else {
                        throw new BoardGenericException("Error while generating Board: Invalid switch case");
                }
            }
        } else {
            throw new BoardGenericException("Error while generating Board: Invalid num of players");
        }
    }


    public Board(String jsonPath) throws BoardGenericException{


        JSONParser jsonParser = new JSONParser();
        try{
            tilesToDraw = new ArrayList<>();
            Object obj = jsonParser.parse(new FileReader(jsonPath));
            JSONArray boardLine;
            JSONObject objBoard = (JSONObject) ((JSONObject) obj).get("board");
            numRows = Math.toIntExact((long)(objBoard.get("numRows")));
            numColumns = Math.toIntExact((long)(objBoard.get("numColumns")));
            boardTiles = new Tile[numRows][numColumns];
            JSONArray arrayDeck = (JSONArray)objBoard.get("deck");
            JSONArray arrayBoard = (JSONArray)objBoard.get("matrix");

            for (Object o : arrayDeck) {
                tilesToDraw.add(Tile.valueOfLabel((String) o));
            }
            for(int i = 0; i < numRows; i++){
                boardLine = (JSONArray) arrayBoard.get(i);
                for(int j = 0; j < numColumns; j++){
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
    public void fill() throws OutOfTilesException{
        Tile t;
        for(int i = 0; i<numRows; i++){
            for(int j = 0; j<numColumns; j++){
                t = boardTiles[i][j];
                if (!t.equals(Tile.Invalid) && !t.equals(Tile.Empty)) {
                    tilesToDraw.add(t);
                }
            }
        }
        Collections.shuffle(tilesToDraw);
        for(int i = 0; i<numRows; i++){
            for(int j = 0; j<numColumns; j++){
                if(!boardTiles[i][j].equals(Tile.Invalid)) {
                    if(tilesToDraw.size() > 0) {
                        boardTiles[i][j] = tilesToDraw.remove(0);
                    }else{
                        throw new OutOfTilesException("No more tiles left in the deck");
                    }
                }

            }
        }

    }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        boolean same = true;

        if(numRows != board.getNumRows() || numColumns != board.getNumColumns() || numPlayers != getNumPlayers())
            same = false;
        for(int i = 0; same && i<numRows; i++){
            for(int j = 0; same && j<numColumns; j++){
                if(!boardTiles[i][j].equals(board.getTile(i,j))){
                    same = false;
                }
            }
        }
        List<AbstractCommonGoal> Bgoals = board.getCommonGoals();
        if(goals.size() != Bgoals.size())
            same = false;
        for(int i = 0;same && i<goals.size(); i++){
            if(!goals.get(i).equals(Bgoals.get(i)))
                same = false;
        }
        return same;

    }

    @Override
    public int hashCode() {
        int result = Objects.hash(tilesToDraw, numPlayers, goalsFactory, goals);
        result = 31 * result + Arrays.deepHashCode(boardTiles);
        return result;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumColumns() {
        return numColumns;
    }

    //get tile in position pos
    public Tile getTile(Position pos) throws BoardGenericException{
        return getTile(pos.getRow(), pos.getColumn());
    }

    public Tile getTile(int i, int j) throws BoardGenericException{
        try {
            return boardTiles[i][j];
        } catch (IndexOutOfBoundsException e){
            throw new BoardGenericException("Error while getting tile from Board : illegal coordinates");
        }
    }

    /* public void fill() {

    } */

    //Pick a tile from the board --> when you pick it,
    //it gets eliminated by the board
    public Tile pickTile(Position pos) throws BoardGenericException {
        try {
            Tile selectedTile;
            selectedTile = boardTiles[pos.getRow()][pos.getColumn()];
            boardTiles[pos.getRow()][pos.getColumn()] = Tile.Empty;
            return selectedTile;
        } catch (NullPointerException e){
            throw new BoardGenericException("Error while picking tile: pos is null pointer ");
        }
    }

    //get the two goals from CommonGoalsFactory
    public void initializeGoals() {
        goals = goalsFactory.createTwoGoals(numPlayers);
    }

    //return the two common goals
    public List<AbstractCommonGoal> getCommonGoals() {
        List<AbstractCommonGoal> matchGoals = new ArrayList<>();
        for(int i = 0; i < goals.size(); i++) {
            matchGoals.add(goals.get(i));
        }
        return matchGoals;
    }

    //complete when PartialMove is implemented
    /*public List<PartialMove> getValidMoves(List<PartialMove> moves) {

    }*/



}
