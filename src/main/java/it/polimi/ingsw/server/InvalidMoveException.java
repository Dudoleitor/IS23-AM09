package it.polimi.ingsw.server;

public class InvalidMoveException extends RuntimeException{
    public InvalidMoveException(String msg){
        super(msg);
    }
}
