package it.polimi.ingsw.shared;

public enum Tile {
    Cat("C"),
    Book("B"),
    Game("G"),
    Frame("F"),
    Trophy("T"),
    Plant("P"),
    Empty("E"),
    Invalid("I");

    public final String label;

    /**
     * Initialize label of tile
     * @param s is the label attached to the tile
     */
    Tile(String s){
        label = s;
    }

    /**
     * @param label is the label to look for
     * @return the Tile corresponding to the label
     * @throws TileGenericException whether Tile type doesn't exist
     */
    public static Tile valueOfLabel(String label) throws TileGenericException { //gets a label and returns a Tile
        for (Tile e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        throw new TileGenericException("Tile label not found");
    }

    /**
     * @return label corresponding to Tile type
     */
    @Override
    public String toString(){
        return this.label;
    }
}

