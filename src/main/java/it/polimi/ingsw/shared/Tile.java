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
    Tile(String s){
        label = s;
    }
    public static Tile valueOfLabel(String label) { //gets a label and returns a Tile
        for (Tile e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        throw new TileGenericException("Tile label not found");
    }

    @Override
    public String toString(){
        return this.label;
    }
}

