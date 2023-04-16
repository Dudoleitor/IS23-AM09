package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Move;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Constants;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyInterface;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

public class Lobby implements LobbyInterface {
    private final int id;
    private final List<Client> players = new ArrayList<>();
    private boolean ready = false;
    private boolean started = false;
    private boolean full = false;
    private final Chat chat;
    private Controller controller;

    public Lobby(Client firstPlayer, int id){
        this.players.add(firstPlayer);
        this.id = id;
        this.chat = new Chat();
        chat.addPlayer(firstPlayer);
    }

    /**
     * add a player to lobby
     * @param client is the player object to add to the lobby
     */
    public void addPlayer(Client client){
        if(players.contains(client)) //if player logged in previously
            return;
        if (players.size() < Constants.maxSupportedPlayers) { //checks lobby isn't already full
            players.add(client);
            chat.addPlayer(client);
        }else
            throw new RuntimeException("Lobby already full");

        //if the lobby has enough players it's ready to start
        if(players.size() >= Constants.minSupportedPlayers)
            ready = true;
        if(players.size() >= Constants.maxSupportedPlayers)
            full = true;
    }

    /**
     * @return true is the lobby is full of players for it's capacity
     */
    public boolean isReady(){
        return ready;
    }
    public boolean hasStarted(){
        return started;
    }
    public boolean isFull(){
        return full;
    }
    /**
     * @return list of players in this lobby
     */
    public ArrayList<Client> getPlayers(){
        return new ArrayList<>(players); //TODO this is by reference
    }
    public List<String> getPlayerNames(){
        return players.stream().
                map(Client::getPlayerName).
                collect(Collectors.toList());
    }
    /**
     * @return every message in that lobby
     */
    public Chat getChat(){
        return new Chat(chat);
    }

    public boolean isEmpty(){
        return players.size() == 0;
    }
    public String getLobbyAdmin() throws Exception {
        if(players.size() == 0){
            throw new Exception("No Players");
        }
        else{
            return players.get(0).getPlayerName();
        }
    }

    @Override
    public boolean matchHasStarted(){
        return started;
    }
    /**
     * start the lobby when it's full of players
     */
    @Override
    public boolean startGame(String player){
        try {
            if(!ready  || !getLobbyAdmin().equals(player))
                return false;
        }
        catch (Exception e){
            return false;
        }

        started = true;
        controller = new Controller(players);
        System.out.println("MATCH STARTED IN LOBBY #"+id);
        return true;
    }

    @Override
    public boolean isLobbyAdmin(String playerName) throws RemoteException {
        if(isEmpty()){
            return false;
        }
        else{
            return players.get(0).getPlayerName().equals(playerName);
        }
    }

    /**
     *
     * @param playerName is the player that is sending a message
     * @param message is the content
     * @throws Exception when message format is wrong
     */
    @Override
    public void postToLiveChat(String playerName, String message) throws Exception {
        if(playerName == null || message == null){
            throw new Exception("Wrong format of message");
        }
        chat.addMessage(playerName,message);
    }

    /**
     *
     * @param sender is the player that is sending a message
     * @param receiver is the player that is sending a message
     * @param message is the content
     * @throws Exception when message format is wrong
     */
    @Override
    public void postSecretToLiveChat(String sender, String receiver, String message) throws Exception {
        if(sender == null || receiver == null || message == null){
            throw new Exception("Wrong format of message");
        }
        chat.addSecret(sender,receiver,message);
    }


    @Override
    public Chat updateLiveChat() throws RemoteException {
        return new Chat(chat);
    }

    @Override
    public void quitGame(String player) throws RemoteException {

    }

    @Override
    public void postMove(String player, Move move) throws RemoteException {
        Client playerInput = null;
        try{
            playerInput = players.stream().filter(x -> x.getPlayerName().equals(player)).findFirst().orElse(null);
            if(playerInput != null) {
                controller.moveTiles(player, move);
            }
        } catch (ControllerGenericException e){
            if(playerInput != null)
                playerInput.postChatMessage(e.getMessage());
        }

    }

    @Override
    public int getID(){
        return this.id;
    }
}
