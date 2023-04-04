package it.polimi.ingsw.RMI;

import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.server.Move;
import it.polimi.ingsw.shared.Color;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Shelf;
import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Lobby implements LobbyRemoteInterface {
    private final int id;
    private final List<String> players = new ArrayList<>();
    private final int numPlayers;
    private boolean ready;
    private static final Map<String, Color> colorPlayer = new HashMap<>(); //memorize player color on login
    private Lock niceLockBro;
    private Controller controller; //TODO to initialize
    private  List<ChatMessage> chatMessages = Collections.synchronizedList(new ArrayList<>());
    public Lobby(String firstPlayer, int id, int numPlayers){
        players.add(firstPlayer);
        this.numPlayers = numPlayers;
        ready = false;
        colorPlayer.put(firstPlayer, Color.getRandomColor());
        this.niceLockBro = new ReentrantLock();
        this.id = id;
    }

    /**
     * add a player to lobby
     * @param player is the player to add to lobby
     */
    public void addPlayer(String player) {
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

    public int getId() {
        return id;
    }

    @Override
    public void sendShelf(JSONObject s) throws RemoteException, JsonBadParsingException {
        System.out.println("Here's your shelf bro:\n" + new Shelf(s));
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
        addChatMessage(playerName, message);
    }

    /**
     *
     * @param playerName is the player requesting updated chat
     * @param alreadyReceived are messages already in client chat
     * @return list of messages yet to be received
     * @throws RemoteException when there are connection errors
     */
    @Override
    public List<ChatMessage> updateLiveChat(String playerName, int alreadyReceived) throws RemoteException {
        List<ChatMessage> lobbyChat = getChat(); //get chat of that lobby

        List<ChatMessage> livechatUpdate = new ArrayList<>();
        for(int i = alreadyReceived; i < lobbyChat.size(); i++){
            livechatUpdate.add(lobbyChat.get(i));
        }
        System.out.println("updated client chat");
        return livechatUpdate;
    }

    @Override
    public void quitGame(String player, LobbyRemoteInterface stub) throws RemoteException {

    }

    @Override
    public boolean matchHasStarted(){
        return isReady();
    }

    @Override
    public boolean isMyTurn(String player) throws RemoteException {
        if(player == null){
            return false;
        }
        if(player.equals(controller.getCurrentPlayerName())){
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public List<Move> getValidMoves(String player) throws RemoteException {
        return null;
    }

    @Override
    public void postMove(String player, int moveCode) throws RemoteException {

    }
}
