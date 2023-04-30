package it.polimi.ingsw.shared.model;

public class OutOfTilesException extends Exception{
    OutOfTilesException(String msg){
        super(msg);
    }
}