package it.polimi.ingsw.shared;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.server.CommonGoalsFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Objects;


public class Board {

    private Tile boardTiles[][];
    private List<Tile> tilesToDraw;
    private int numPlayers;
    private int numRows;
    private int numColumns;
    private CommonGoalsFactory goalsFactory = new CommonGoalsFactory();
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


    public Board(String jsonPath, int numRows, int numColumns) throws BoardGenericException{

        this.numRows = numRows;
        this.numColumns = numColumns;

        JSONParser jsonParser = new JSONParser();
        try{
            Object obj = jsonParser.parse(new FileReader(jsonPath));
            JSONArray board_line;
            JSONObject obj_board = (JSONObject) ((JSONObject) obj).get("board");
            //numRows = Math.toIntExact((long)(obj_board.get("numRows")));
            //numColumns = Math.toIntExact((long)(obj_board.get("numColumns")));
            boardTiles = new Tile[numRows][numColumns];
            JSONArray array_board = (JSONArray)obj_board.get("matrix");

            for(int i = 0; i < numRows; i++){
                board_line = (JSONArray) array_board.get(i);
                for(int j = 0; j < numColumns; j++){
                    if(board_line.get(j).toString().equals("1")){
                        boardTiles[i][j] = Tile.Empty;
                    } else {
                        boardTiles[i][j] = Tile.Invalid;
                    }
                    //boardTiles[i][j] = Tile.valueOf((array_board.get(i*numRows + j)).toString());
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

    @Override
/*public String toString() {
    return "Board{" +
            "boardTiles=" + Arrays.toString(boardTiles) +
            ", tilesToDraw=" + tilesToDraw +
            ", numPlayers=" + numPlayers +
            ", goalsFactory=" + goalsFactory +
            ", goals=" + goals +
            '}';
}*/

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
        return numPlayers == board.numPlayers && Arrays.equals(boardTiles, board.boardTiles) && tilesToDraw.equals(board.tilesToDraw) && goalsFactory.equals(board.goalsFactory) && goals.equals(board.goals);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(tilesToDraw, numPlayers, goalsFactory, goals);
        result = 31 * result + Arrays.deepHashCode(boardTiles);
        return result;
    }



    //get tile in position pos
    public Tile getTile(Position pos) {
        return boardTiles[pos.getRow()][pos.getColumn()];
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
