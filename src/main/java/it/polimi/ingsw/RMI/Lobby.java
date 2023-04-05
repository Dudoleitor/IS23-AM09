package it.polimi.ingsw.RMI;

import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.server.Move;
import it.polimi.ingsw.shared.Color;
import it.polimi.ingsw.shared.Constants;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

public class Lobby implements LobbyRemoteInterface {
    private final int id;
    private final List<String> players = new ArrayList<>();
    private boolean ready = false;
    private boolean started = false;
    private static final Map<String, Color> colorPlayer = new HashMap<>(); //memorize player color on login
    private Controller controller; //TODO to initialize
    private final List<ChatMessage> chatMessages = Collections.synchronizedList(new ArrayList<>());
    private final String lobbyAdmin; //admin
    public Lobby(String firstPlayer, int id){
        this.lobbyAdmin = firstPlayer;
        this.players.add(firstPlayer);
        this.colorPlayer.put(firstPlayer, Color.getRandomColor());
        this.id = id;
    }

    /**
     * add a player to lobby
     * @param player is the player to add to lobby
     */
    public void addPlayer(String player) {
        if(players.contains(player)) //if player logged in previously
            return;
        if (players.size() < Constants.maxSupportedPlayers) { //checks lobby isn't already full
            players.add(player);
            if(!colorPlayer.containsKey(player))
                colorPlayer.put(player, Color.getRandomColor());
        }else
            throw new RuntimeException("Lobby already full");

        //if the lobby has enough players it's ready to start
        if(players.size() >= Constants.minSupportedPlayers)
            ready = true;
    }

    /**
     * @return true is the lobby is full of players for it's capacity
     */

    public boolean isReady(){
        return ready;
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
        if(!ready  || !player.equals(lobbyAdmin))
            return false;
        started = true;
        controller = new Controller(players);
        return true;
    }

    @Override
    public boolean isLobbyAdmin(String player) throws RemoteException {
        return false;
    }

    @Override
    public String peekBoard() throws RemoteException {
        return controller.getBoard().toString();
    }

    @Override
    public String peekPlayerShelf(String playerName) throws RemoteException {
        return controller.getShelves().get(playerName).toString();
    }

    /**
     * @return list of players in this lobby
     */
    public ArrayList<String> getPlayers(){
        return new ArrayList<>(players);
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
    /**
     * @param player  is the sender of the message
     * @param message is the text content
     */
    public void addChatMessage(String player, String message){
        chatMessages.add(new ChatMessage(player,message, colorPlayer.get(player)));
        System.out.println("Posted:" + chatMessages.get(chatMessages.size()-1));
    }
    /**
     * @param sender is the sender of the message
     * @param receiver is the recipient of the message
     * @param message is the text content
     */
    public void addSecretChatMessage(String sender, String receiver, String message){
        chatMessages.add(new PrivateChatMessage(sender,receiver,message, colorPlayer.get(sender)));
        System.out.println("Posted:" + chatMessages.get(chatMessages.size()-1));
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
        addSecretChatMessage(sender, receiver, message);
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
        List<ChatMessage> lobbyChat = getChat().stream().filter(x->LiveChat.checkValidReceiver(x,playerName)).collect(Collectors.toList()); //get chat of that lobby

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
    public void postMove(String player, Move move) throws RemoteException {

    }
}
