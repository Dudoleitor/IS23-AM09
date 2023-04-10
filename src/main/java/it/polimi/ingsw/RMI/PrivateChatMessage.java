package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.Color;

public class PrivateChatMessage extends ChatMessage{
    private String receiver;

    public PrivateChatMessage(String sender, String receiver, String message, Color color ){
        super(sender, message, color);
        this.receiver = receiver;
    }
    @Override
    public String toString(){
        return Color.coloredString("<PRIVAT&>", Color.Green) + Color.coloredString(getSender(),Color.Green) +
                "#: " + getMessage();
    }
    public String getReciever(){
        return receiver;
    }

}
