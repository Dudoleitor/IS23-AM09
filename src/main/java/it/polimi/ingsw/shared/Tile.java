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
     * @param t is the tile to check validity to
     * @return true if t type does eists
     */
    public static boolean existsValue(Tile t){
        if(t == null){
            return false;
        }
        for (Tile e : values()){
            if (e.equals(t))
                return true;
        }
        return false;
    }

    /**
     * @return label corresponding to Tile type
     */
    @Override
    public String toString(){
        return this.label;
    }

    public String toColorFulString(){
        return this.color + this.label + Colors.ANSI_RESET;
    }

    private class Colors {
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_BLACK = "\u001B[30m";
        public static final String ANSI_RED = "\u001B[31m";
        public static final String ANSI_GREEN = "\u001B[32m";
        public static final String ANSI_YELLOW = "\u001B[33m";
        public static final String ANSI_BLUE = "\u001B[34m";
        public static final String ANSI_PURPLE = "\u001B[35m";
        public static final String ANSI_CYAN = "\u001B[36m";
        public static final String ANSI_WHITE = "\u001B[37m";
    }
}

