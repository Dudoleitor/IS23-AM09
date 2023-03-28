package it.polimi.ingsw.shared;

public enum Tile {
    Cat("C", Colors.ANSI_GREEN),
    Book("B",Colors.ANSI_WHITE),
    Game("G",Colors.ANSI_YELLOW),
    Frame("F",Colors.ANSI_BLUE),
    Trophy("T",Colors.ANSI_CYAN),
    Plant("P",Colors.ANSI_PURPLE),
    Empty("E",Colors.ANSI_BLACK),
    Invalid("I",Colors.ANSI_RED);

    public final String label;
    public final String color;

    /**
     * Initialize label of tile
     * @param s is the label attached to the tile
     */
    Tile(String s,String c){
        label = s;
        color = c;
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
        return this.color + this.label + Colors.ANSI_RESET;
    }
}

