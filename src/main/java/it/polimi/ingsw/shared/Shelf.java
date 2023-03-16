package it.polimi.ingsw.shared;

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
    public Shelf(Shelf s){
        if(s != null){
            rows = s.getRows();
            columns = s.getColumns();
            tiles = new Tile[rows][columns];
            for(int i = 0; i<rows; i++){
                for(int j = 0; j<columns; j++){
                    tiles[i][j] = s.getTile(i,j);
                }
            }
        } else {
            rows = 0;
            columns = 0;
            tiles = null;
        }
    }
    public Tile getTile(Position pos){
        return getTile(pos.getRow(), pos.getColumn());
    }
    public Tile getTile(int row, int column){
        return tiles[row][column];
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
    public void insertTile(Tile tile, int column){
        boolean is_empty = false;
        for(int i = tiles.length-1; !is_empty && i>=0; i--){
            if(tiles[i][column] == Tile.Empty){
                tiles[i][column] = tile;
                is_empty = true;
            }
        }

    }

    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }
}
