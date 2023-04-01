package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.Color;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class ChatMessage implements Serializable {
    private String playerName;
    private String message;
    private Timestamp timestamp;

    ChatMessage(String playerName, String message){
        this.message = message;
        this.playerName = playerName;
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
    }
    @Override
    public String toString(){
            return ">" + Color.coloredString(playerName,Color.Green) +
                    ": " + message;
        }
}
