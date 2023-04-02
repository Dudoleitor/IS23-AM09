package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.Color;
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

public class ServerMain implements RemoteCall{
    private static volatile boolean keepOn = true;
    private static int port = 1234;
    private static List<ChatMessage> messages;
    private static final Map<String, Color> colorPlayer = new HashMap<>(); //memorize player color on login
    private List<Lobby> lobbies = new ArrayList<>();

    public static void main(String argv[]){
        ServerMain obj = new ServerMain();
        RemoteCall stub;
        try {
            stub = (RemoteCall) UnicastRemoteObject.exportObject(obj, port); //create an interface to export
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

        //init livechat
        messages = Collections.synchronizedList(new ArrayList<>());

        System.out.println("Server is on");
        while (keepOn) {
            Thread.onSpinWait(); //is used to suspend the process and make it wait
        } //to keep it online
        System.out.println("Server is shutting down :D, don't forget to save... oh no too late");
        System.exit(0); //to shut down the server, maybe it doesn't shut down spontaneously because fo the interface it gave away
    }

    @Override
    public boolean login(String nick) throws RemoteException {
        System.out.println(nick + " has just logged in");
        if(!colorPlayer.containsKey(nick))
            colorPlayer.put(nick, Color.getRandomColor());
        return true;
    }

    @Override
    public void sendShelf(JSONObject s) throws JsonBadParsingException {
        System.out.println("Here's your shelf bro:\n" + new Shelf(s));
    }

    @Override
    public void postToLiveChat(String playerName, String message) throws Exception {
        if(playerName == null || message == null){
            throw new Exception("Wrong format of message");
        }
        messages.add(new ChatMessage(playerName,message, colorPlayer.get(playerName)));
        System.out.println("Posted:" + messages.get(messages.size()-1));
    }

    @Override
    public List<ChatMessage> updateLiveChat(int alreadyReceived) throws RemoteException {
        List<ChatMessage> livechatUpdate = new ArrayList<>();
        for(int i = alreadyReceived; i < messages.size(); i++){
            livechatUpdate.add(messages.get(i));
        }
        System.out.println("updated client chat");
        return livechatUpdate;
    }
    @Override
    public void joinLobby(String player, RemoteCall stub){ //TODO to handle a rejoin of the same player possibility
        Lobby lobby = lobbies.stream()
                    .filter(l -> !l.isReady()) //keep only not full lobbies
                    .findFirst() //find first lobby matched
                    .orElse(null);
        if(lobby != null){
            lobby.addPlayer(player, stub); //if exists then add player
            if(lobby.isReady())
                lobby.start(); //TODO one day will start a lobby thread
        }else {
            createLobby(player, stub,Constants.maxSupportedPlayers); //otherwise create new lobby
        }



    }
    @Override
    public void createLobby(String player, RemoteCall stub, int numPlayers){
        Lobby lobby = new Lobby(player, stub, numPlayers);
        lobbies.add(lobby);
    }

    @Override
    public void quitGame(String player, RemoteCall stub) {
        lobbies.stream()
                .filter(l -> l.getPlayers().contains(player))
                .forEach(l -> l.remove(player));
    }
}
