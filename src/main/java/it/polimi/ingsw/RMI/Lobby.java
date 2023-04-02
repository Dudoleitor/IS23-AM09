package it.polimi.ingsw.RMI;

import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.server.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby{
    private final List<String> players = new ArrayList<>();
    private final int numPlayers;
    private boolean ready;
    private final Map<String, LiveChat> chat= new HashMap<>();
    private final Map<String, Match> matches = new HashMap<>();
    private Controller controller;
    public Lobby(String firstPlayer, RemoteCall stub,  int numPlayers){
        players.add(firstPlayer);
        this.numPlayers = numPlayers;
        ready = false;
        chat.put(firstPlayer, new LiveChat(firstPlayer, stub));
        chat.get(firstPlayer).start();
        matches.put(firstPlayer, new Match(firstPlayer, stub));
    }
    public void addPlayer(String player, RemoteCall stub) {
        if (players.size() < numPlayers) {
            players.add(player);
            chat.put(player, new LiveChat(player, stub));
            chat.get(player).start();
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
            chat.get(player).join();
            matches.get(player).join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    public ArrayList<String> getPlayers(){
        return new ArrayList<String>(players);
    }



}
