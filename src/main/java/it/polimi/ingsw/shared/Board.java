package it.polimi.ingsw.shared;

import java.util.List;

public class Board {

    private Tile tiles[][];
    private List<Tile> tilesToDraw;

    public Board(int numRows, int numColumns) {
        tiles = new Tile[numRows][numColumns];
    }

    public Tile getTile(Position pos) {
        return tiles[pos.getRow()][pos.getColumn()];
    }

    //complete when CommonGoals are implemented
    /*public List<CommonGoals> getCommonGoals() {

    }*/

    //complete when PartialMove is implemented
    /*public List<PartialMove> getValidMoves(List<PartialMove> moves) {

    }*/



}
