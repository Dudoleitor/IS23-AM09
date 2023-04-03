package it.polimi.ingsw.RMI;

import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.shared.Color;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Lobby{
    private final List<String> players = new ArrayList<>();
    private final int numPlayers;
    private boolean ready;
    private static final Map<String, Color> colorPlayer = new HashMap<>(); //memorize player color on login
    private Lock niceLockBro;
    private Controller controller; //TODO to initialize
    private  List<ChatMessage> chatMessages = Collections.synchronizedList(new ArrayList<>());
    public Lobby(String firstPlayer, RemoteCall stub,  int numPlayers){
        players.add(firstPlayer);
        this.numPlayers = numPlayers;
        ready = false;
        colorPlayer.put(firstPlayer, Color.getRandomColor());
        this.niceLockBro = new ReentrantLock();
    }

    /**
     * add a player to lobby
     * @param player is the player to add to lobby
     * @param stub is he's stub
     */
    public void addPlayer(String player, RemoteCall stub) {
        if (players.size() < numPlayers) { //checks lobby isn't already full
            players.add(player);
            if(!colorPlayer.containsKey(player))
                colorPlayer.put(player, Color.getRandomColor());

        }else
            throw new RuntimeException("Lobby already full");

        if(players.size() == numPlayers) //if after adding the lobby it is full then it's set to be ready to start
            ready = true;
    }

    /**
     * @return true is the lobby is full of players for it's capacity
     */
    public boolean isReady(){
        return ready;
    }

    /**
     * start the lobby when it's full of players
     */
    public void start(){
        controller = new Controller(players);
    }

    public void remove(String player){ //actually does nothin'


    }

    /**
     * @return list of players in this lobby
     */
    public ArrayList<String> getPlayers(){
        return new ArrayList<>(players);
    }

    /**
     * @param player  is the sender of the message
     * @param message is the text content
     */
    public void addChatMessage(String player, String message){
        chatMessages.add(new ChatMessage(player,message, colorPlayer.get(player)));
        System.out.println("Posted:" + chatMessages.get(chatMessages.size()-1));

    }

    /**
     * @return every message in that lobby
     */
    public List<ChatMessage> getChat(){
        return new ArrayList<>(chatMessages);
    }



}
