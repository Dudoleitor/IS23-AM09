package it.polimi.ingsw.shared;

public class OutOfTilesException extends Exception{
    OutOfTilesException(String msg){
        super(msg);
    }
}