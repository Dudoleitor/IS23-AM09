package it.polimi.ingsw.shared;

import it.polimi.ingsw.server.clientonserver.Client;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
    public Chat(JSONObject jsonChat){
        chatMessages = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) jsonChat.get("messageList");
        for(Object c : jsonArray){
            chatMessages.add(new ChatMessage((JSONObject) c));
        }
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

    public void addMessage(ChatMessage message) {
        this.addMessage(message.getSender(), message.getMessage());
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
    public ChatMessage getLast(){
        if(chatMessages == null || chatMessages.isEmpty()){
            return null;
        }
        else {
            return chatMessages.get(chatMessages.size()-1);
        }
    }
    public List<ChatMessage> getAllMessages(){
        List<ChatMessage> allmessages = new ArrayList<>();
        allmessages.addAll(chatMessages);
        return allmessages;
    }

    public JSONObject toJson(){
        JSONObject jsonChat = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(ChatMessage c : chatMessages){
            jsonArray.add(c.toJson());
        }
        jsonChat.put("messageList", jsonArray);
        return jsonChat;
    }
}
