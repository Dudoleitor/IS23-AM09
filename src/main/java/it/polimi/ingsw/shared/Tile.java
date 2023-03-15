package it.polimi.ingsw.shared;

public enum Tile {
    Cat("cat"),
    Book("book"),
    Game("game"),
    Frame("frame"),
    Trophie("trophie"),
    Plant("plant"),
    Empty,
    Invalid;

    private final String name;
    private String getName() { return this.name; }

    Tile() {
        this.name = new String("not_applicable");
    }
    Tile(String name) {
        this.name = new String(name);
    }

}
