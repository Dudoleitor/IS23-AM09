package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.Client;
import it.polimi.ingsw.shared.Color;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Chat implements Serializable {
    private List<ChatMessage> chatMessages;
    private Map<String, Color> MapColorPlayer;
    public Chat(){
        chatMessages =  Collections.synchronizedList(new ArrayList<>());
        MapColorPlayer = new HashMap<>();
    }
    public Chat(Chat toClone){
        chatMessages =  Collections.synchronizedList(new ArrayList<>());
        chatMessages.addAll(toClone.getAllMessages());
    }
    public void add(ChatMessage newMessage){
        chatMessages.add(newMessage);
    }
    public void addMessage(String sender,String text){
        ChatMessage message = new ChatMessage(
                sender,
                text,
                MapColorPlayer.get(sender));
        chatMessages.add(message);
    }
    public void add(PrivateChatMessage newSecret){
        chatMessages.add(newSecret);
    }
    public void addSecret(String sender,String receiver,String text){
        PrivateChatMessage message = new PrivateChatMessage(
                sender,
                receiver,
                text,
                MapColorPlayer.get(sender));
        chatMessages.add(message);
    }
    public void addPlayer(Client client){
        //only if is a new login
        if(!MapColorPlayer.containsKey(client.getPlayerName())){
            //add a unique color for player in Map (if possible)
            Color playerColor = Color.getRandomColor();

            List<Color> alreadyPresent = getAllColors();

            int numberOfPlayers = MapColorPlayer.size();
            int numberOfColors = (int) Arrays.stream(Color.values()).count();

            if(numberOfPlayers <= numberOfColors){
                while(alreadyPresent.contains(playerColor)){
                    playerColor = Color.getRandomColor();

                }
            }
            MapColorPlayer.put(client.getPlayerName(), playerColor);
        }
    }

    private List<Color> getAllColors(){
        return  MapColorPlayer.values().
                stream().
                distinct().
                collect(Collectors.toList());
    }
    public int size(){
        return chatMessages.size();
    }
    public ChatMessage get(int index){
        return chatMessages.get(index);
    }
    public List<ChatMessage> getAllMessages(){
        List<ChatMessage> allmessages = new ArrayList<>();
        allmessages.addAll(chatMessages);
        return allmessages;
    }
}
