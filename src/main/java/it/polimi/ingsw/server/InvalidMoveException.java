package it.polimi.ingsw.server;

public class InvalidMoveException extends Exception{
    public InvalidMoveException(String msg){
        super(msg);
    }
}
