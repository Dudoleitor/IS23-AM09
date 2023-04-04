package it.polimi.ingsw.RMI;

import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.server.Move;
import it.polimi.ingsw.shared.Constants;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Shelf;
import org.json.simple.JSONObject;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerMain implements ServerRemoteInterface {
    private static volatile boolean keepOn = true;
    private static int port = 1234;
    private List<Lobby> lobbies = new ArrayList<>();
    private Controller controller = null;

    public static void main(String argv[]){
        ServerMain obj = new ServerMain();
        ServerRemoteInterface stub;
        try {
            stub = (ServerRemoteInterface) UnicastRemoteObject.exportObject(obj, port); //create an interface to export
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(port); //create a registry that accepts request on a defined port
        } catch (RemoteException e) {
            e.printStackTrace(); //TODO to handle correctly
        }
        try {
            registry.bind("interface", stub); //Binds a remote reference to the specified name in this registry
        } catch (RemoteException e) { //TODO to handle correctly
            throw new RuntimeException(e);
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Server is on");
        while (keepOn) {
            Thread.onSpinWait(); //is used to suspend the process and make it wait
        } //to keep it online
        System.out.println("Server is shutting down :D, don't forget to save... oh no too late");
        System.exit(0); //to shut down the server, maybe it doesn't shut down spontaneously because fo the interface it gave away
    }

    /**
     *
     * @param nick is login nickname
     * @return true is login is successful
     * @throws RemoteException is there are problems with connection
     */
    @Override
    public boolean login(String nick) throws RemoteException {
        System.out.println(nick + " has just logged in");
        return true;
    }

    @Override
    public void sendShelf(JSONObject s) throws JsonBadParsingException { //this method is for a test
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
        lobbies.stream()
                .filter(l -> l.getPlayers().contains(playerName)) //find lobbies that contain that player
                .forEach(l -> l.addChatMessage(playerName, message));
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
        List<ChatMessage> lobbyChat = lobbies.stream()
                .filter(l -> l.getPlayers().contains(playerName)) //find the lobby that contain this player
                .findFirst().get().getChat(); //get chat of that lobby

        List<ChatMessage> livechatUpdate = new ArrayList<>();
        for(int i = alreadyReceived; i < lobbyChat.size(); i++){
            livechatUpdate.add(lobbyChat.get(i));
        }
        System.out.println("updated client chat");
        return livechatUpdate;
    }

    /**
     * @param player requesting to join the lobby
     * @param stub is the player interface
     */
    @Override
    public void joinLobby(String player, ServerRemoteInterface stub){ //TODO to handle a re-join of the same player possibility
        Lobby lobby = lobbies.stream()
                    .filter(l -> !l.isReady()) //keep only not full lobbies
                    .findFirst() //find first lobby matched
                    .orElse(null);
        if(lobby != null){ //if a lobby exists then add player
            lobby.addPlayer(player, stub); //if exists then add player
            if(lobby.isReady())
                lobby.start(); //TODO one day will start a lobby thread
        }else {
            createLobby(player, stub,Constants.maxSupportedPlayers); //otherwise creates new lobby
        }



    }

    /**
     *
     * @param player requesting to create the lobby
     * @param stub is the player interface
     * @param numPlayers is the size of the lobby
     */
    @Override
    public void createLobby(String player, ServerRemoteInterface stub, int numPlayers){
        Lobby lobby = new Lobby(player, stub, numPlayers);
        lobbies.add(lobby);
    }

    @Override
    public void quitGame(String player, ServerRemoteInterface stub) { //TODO to refactor
        lobbies.stream()
                .filter(l -> l.getPlayers().contains(player))
                .forEach(l -> l.remove(player));
    }

    @Override
    public boolean matchHasStarted(String player){
        return lobbies.stream()
                .filter(l -> l.getPlayers().contains(player))
                .findFirst()
                .get().isReady();
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
