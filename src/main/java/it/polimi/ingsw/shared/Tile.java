package it.polimi.ingsw.shared;

public enum Tile {
    Cat("C", Color.Green),
    Book("B",Color.White),
    Game("G",Color.Yellow),
    Frame("F",Color.Blue),
    Trophy("T",Color.Cyan),
    Plant("P",Color.Purple),
    Empty("E",Color.Black),
    Invalid("I",Color.Red);

    public final String label;
    public final Color color;

    /**
     * Initialize label of tile
     * @param s is the label attached to the tile
     */
    Tile(String s,Color c){
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
        return Color.coloredString(this.label,this.color);
    }
}

