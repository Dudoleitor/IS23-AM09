package it.polimi.ingsw.RMI;

import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.server.Player;
import it.polimi.ingsw.shared.Color;

import java.util.*;

public class Lobby{
    private final List<String> players = new ArrayList<>();
    private final int numPlayers;
    private boolean ready;
    private final Map<String, Match> matches = new HashMap<>();
    private Controller controller;
    private  List<ChatMessage> messages = Collections.synchronizedList(new ArrayList<>());
    public Lobby(String firstPlayer, RemoteCall stub,  int numPlayers){
        players.add(firstPlayer);
        this.numPlayers = numPlayers;
        ready = false;
        matches.put(firstPlayer, new Match(firstPlayer, stub));
    }
    public void addPlayer(String player, RemoteCall stub) {
        if (players.size() < numPlayers) {
            players.add(player);
            matches.put(player, new Match(player, stub));

        }else
            throw new RuntimeException("Lobby already full");

        if(players.size() == numPlayers)
            ready = true;
    }

    public boolean isReady(){
        return ready;
    }

    public void start(){ //TODO will initialize the controller
        matches.values().forEach(Match::start);
    }
    public void remove(String player){
        try {
            matches.get(player).join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    public ArrayList<String> getPlayers(){
        return new ArrayList<String>(players);
    }

    public void addChatMessage(String player, String message, Color color){
        messages.add(new ChatMessage(player,message, color));
        System.out.println("Posted:" + messages.get(messages.size()-1));

    }
    public List<ChatMessage> getChat(){
        return new ArrayList<>(messages);
    }



}
