package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.Color;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class ChatMessage implements Serializable {
    private String playerName;
    private String message;
    private Timestamp timestamp;
    private Color color;

    ChatMessage(String playerName, String message, Color color){
        this.message = message;
        this.playerName = playerName;
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
        this.color = color;
    }
    @Override
    public String toString(){
            return ">" + Color.coloredString(playerName,color) +
                    ": " + message;
        }

        public String getSender(){
            return playerName;
        }
        public String getMessage(){
            return message;
        }
}
