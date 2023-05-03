package it.polimi.ingsw.shared;

import it.polimi.ingsw.shared.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatMessage implements Serializable, Jsonable{
    private String playerName;
    private String message;
    private Timestamp timestamp;
    private Color color;

    public ChatMessage(String playerName, String message, Color color){
        this.message = message;
        this.playerName = playerName;
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
        if(color!=null)
            this.color = color;
        else
            this.color = Color.getRandomColor();
    }
    public ChatMessage(JSONObject jsonChat){
        playerName = jsonChat.get("sender").toString();
        message = jsonChat.get("message").toString();
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


    @Override
    public JSONObject toJson() {
        JSONObject chatMessage = new JSONObject();  // Object to return

        // Saving parameters
        chatMessage.put("sender", playerName);
        chatMessage.put("message", message);



        return chatMessage;
    }
}
