package it.polimi.ingsw.shared;

public class Shelf {
    private Tile tiles[][];
    /*public Shelf() { //incomplete function

    }*/
    public Tile getTile(Position pos){
        return tiles[pos.getRow()][pos.getColumn()];
    }

    /*public int getHighestColumn(){ //incomplete function
    }*/

}
