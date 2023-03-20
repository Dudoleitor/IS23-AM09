package it.polimi.ingsw.shared;

public class OutOfTilesException extends RuntimeException{
    OutOfTilesException(String msg){
        super(msg);
    }
}