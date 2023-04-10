package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.Color;

public class PrivateChatMessage extends ChatMessage{
    private String reciever;

    public PrivateChatMessage(String sender, String reciever, String message, Color color ){
        super(sender, message, color);
        this.reciever = reciever;
    }
    @Override
    public String toString(){
        return Color.coloredString("<PRIVAT&>", Color.Green) + Color.coloredString(getSender(),Color.Green) +
                "#: " + getMessage();
    }
    public String getReciever(){
        return reciever;
    }

}
