package it.polimi.ingsw.shared;

import it.polimi.ingsw.shared.ChatMessage;
import it.polimi.ingsw.shared.Color;
import org.json.simple.JSONObject;

public class PrivateChatMessage extends ChatMessage {
    private String receiver;

    public PrivateChatMessage(String sender, String receiver, String message, Color color ){
        super(sender, message, color);
        this.receiver = receiver;
    }
    public PrivateChatMessage(JSONObject jsonChat){
        super(jsonChat);
        receiver = jsonChat.get("receiver").toString();
    }
    @Override
    public String toString(){
        return Color.coloredString("<PRIVAT&>", Color.Green) + Color.coloredString(getSender(),Color.Green) +
                "#: " + getMessage();
    }
    public String getReciever(){
        return receiver;
    }

    @Override
    public JSONObject toJson(){
        JSONObject secretMesssage = super.toJson();
        secretMesssage.put("receiver",receiver);
        return secretMesssage;
    }

}
