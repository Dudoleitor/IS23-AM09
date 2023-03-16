package it.polimi.ingsw.shared;

import it.polimi.ingsw.server.AbstractCommonGoal;
import it.polimi.ingsw.server.CommonGoalsFactory;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private Tile boardTiles[][];
    private List<Tile> tilesToDraw;
    private int numPlayers;
    private CommonGoalsFactory goalsFactory = new CommonGoalsFactory();
    private ArrayList<AbstractCommonGoal> goals = new ArrayList<>();


    public Board(int numRows, int numColumns, int numPlayers) {
        boardTiles = new Tile[numRows][numColumns];
        switch(numPlayers){

            //0 is the uppest row
            //numRow is the lowest row

            case 2:
                for(int i = 0; i < numColumns; i++) {
                    switch (i) {
                        case 0:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Invalid;
                            boardTiles[3][i] = Tile.Invalid;
                            boardTiles[4][i] = Tile.Invalid;
                            boardTiles[5][i] = Tile.Invalid;
                            boardTiles[6][i] = Tile.Invalid;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 1:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Invalid;
                            boardTiles[3][i] = Tile.Invalid;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Invalid;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 2:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Invalid;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Invalid;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 3:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Empty;
                            boardTiles[2][i] = Tile.Empty;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Empty;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 4:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Empty;
                            boardTiles[2][i] = Tile.Empty;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Empty;
                            boardTiles[7][i] = Tile.Empty;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 5:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Empty;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Empty;
                            boardTiles[7][i] = Tile.Empty;
                            boardTiles[8][i] = Tile.Invalid;
                        case 6:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Invalid;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Invalid;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 7:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Invalid;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Invalid;
                            boardTiles[6][i] = Tile.Invalid;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                        case 8:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Invalid;
                            boardTiles[3][i] = Tile.Invalid;
                            boardTiles[4][i] = Tile.Invalid;
                            boardTiles[5][i] = Tile.Invalid;
                            boardTiles[6][i] = Tile.Invalid;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                        //default:
                            //throw new IllegalStateException("Unexpected value: " + i);
                    }
                }

            case 3:

                for(int i = 0; i < numColumns; i++) {
                    switch (i) {
                        case 0:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Invalid;
                            boardTiles[3][i] = Tile.Invalid;
                            boardTiles[4][i] = Tile.Invalid;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Invalid;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 2:
                        case 6:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Empty;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Empty;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 1:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Invalid;
                            boardTiles[3][i] = Tile.Invalid;

                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;

                            boardTiles[6][i] = Tile.Invalid;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 3:
                            boardTiles[0][i] = Tile.Empty;
                            boardTiles[1][i] = Tile.Empty;
                            boardTiles[2][i] = Tile.Empty;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Empty;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 4:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Empty;
                            boardTiles[2][i] = Tile.Empty;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Empty;
                            boardTiles[7][i] = Tile.Empty;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 5:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Empty;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Empty;
                            boardTiles[7][i] = Tile.Empty;
                            boardTiles[8][i] = Tile.Empty;
                            break;
                        case 7:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Invalid;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Invalid;
                            boardTiles[6][i] = Tile.Invalid;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 8:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Invalid;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Invalid;
                            boardTiles[5][i] = Tile.Invalid;
                            boardTiles[6][i] = Tile.Invalid;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + i);
                    }
                }

            case 4:
                for(int i = 0; i < numColumns; i++) {
                    switch (i) {
                        case 0:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Invalid;
                            boardTiles[3][i] = Tile.Invalid;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Invalid;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 2:
                        case 6:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Empty;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Empty;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 1:
                        case 7:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Invalid;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Invalid;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 3:
                            boardTiles[0][i] = Tile.Empty;
                            boardTiles[1][i] = Tile.Empty;
                            boardTiles[2][i] = Tile.Empty;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Empty;
                            boardTiles[7][i] = Tile.Empty;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        case 4:
                            boardTiles[0][i] = Tile.Empty;
                            boardTiles[1][i] = Tile.Empty;
                            boardTiles[2][i] = Tile.Empty;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Empty;
                            boardTiles[7][i] = Tile.Empty;
                            boardTiles[8][i] = Tile.Empty;
                            break;
                        case 5:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Empty;
                            boardTiles[2][i] = Tile.Empty;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Empty;
                            boardTiles[6][i] = Tile.Empty;
                            boardTiles[7][i] = Tile.Empty;
                            boardTiles[8][i] = Tile.Empty;
                            break;
                        case 8:
                            boardTiles[0][i] = Tile.Invalid;
                            boardTiles[1][i] = Tile.Invalid;
                            boardTiles[2][i] = Tile.Invalid;
                            boardTiles[3][i] = Tile.Empty;
                            boardTiles[4][i] = Tile.Empty;
                            boardTiles[5][i] = Tile.Invalid;
                            boardTiles[6][i] = Tile.Invalid;
                            boardTiles[7][i] = Tile.Invalid;
                            boardTiles[8][i] = Tile.Invalid;
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + i);
                    }
                }

        }

    }


    //get tile in position pos
    public Tile getTile(Position pos) {
        return boardTiles[pos.getRow()][pos.getColumn()];
    }

    /* public void fill() {

    } */

    //Pick a tile from the board --> when you pick it,
    //it gets eliminated by the board
    public Tile pickTile(Position pos) {
        Tile selectedTile;
        selectedTile = boardTiles[pos.getRow()][pos.getColumn()];
        boardTiles[pos.getRow()][pos.getColumn()] = Tile.Empty;
        return selectedTile;
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
