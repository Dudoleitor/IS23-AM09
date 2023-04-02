package it.polimi.ingsw.RMI;

import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.shared.Color;

import java.util.*;

public class Lobby{
    private final List<String> players = new ArrayList<>();
    private final int numPlayers;
    private boolean ready;
    private final Map<String, Match> matches = new HashMap<>();
    private static final Map<String, Color> colorPlayer = new HashMap<>(); //memorize player color on login
    private Controller controller; //TODO to initialize
    private  List<ChatMessage> chatMessages = Collections.synchronizedList(new ArrayList<>());
    public Lobby(String firstPlayer, RemoteCall stub,  int numPlayers){
        players.add(firstPlayer);
        this.numPlayers = numPlayers;
        ready = false;
        matches.put(firstPlayer, new Match(firstPlayer, stub));
        colorPlayer.put(firstPlayer, Color.getRandomColor());
    }

    /**
     * add a player to lobby
     * @param player is the player to add to lobby
     * @param stub is he's stub
     */
    public void addPlayer(String player, RemoteCall stub) {
        if (players.size() < numPlayers) { //checks lobby isn't already full
            players.add(player);
            matches.put(player, new Match(player, stub));
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
    public void start(){ //TODO will initialize the controller
        matches.values().forEach(Match::start);
    }

    public void remove(String player){ //to implement better, for now it's useful for tests
        try {
            matches.get(player).join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


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
