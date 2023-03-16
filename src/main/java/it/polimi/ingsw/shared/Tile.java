package it.polimi.ingsw.shared;

public enum Tile {
    Cat,
    Book,
    Game,
    Frame,
    Trophy,
    Plant,
    Empty,
    Invalid;

    @Override
    public String toString(){
        switch(this) {
            case Cat:
                return "C";
            case Book:
                return "B";
            case Game:
                return "G";
            case Frame:
                return "F";
            case Trophy:
                return "T";
            case Plant:
                return "P";
            case Empty:
                return "E";
            case Invalid:
                return "I";
            default:
                return "-";
        }
    }
}

