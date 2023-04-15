package it.polimi.ingsw.server;

import it.polimi.ingsw.client.InputSanitizer;
import it.polimi.ingsw.server.adapters.LobbyRMIAdapter;
import it.polimi.ingsw.server.adapters.ServerRMIAdapter;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.Constants;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyRemoteCouple;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyRemoteInterface;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerRemoteInterface;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

public class ServerMain{
    private static boolean keepOn = true;
    private static final int port = Constants.port;

    /**
     * This list contains the clients connected to the
     * server that did not join a lobby yet.
     */
    private final List<Client> clientsWithoutLobby = new ArrayList<>();
    private final Map<Lobby, LobbyRemoteCouple> lobbies = new HashMap<>(); //this hashmap is to remember every couple of Lobby <-> RemoteInterface to communicate with
    private static Registry registry = null;
    private static InputSanitizer inputSanitizer;

    public static void main(String argv[]){ //in the main it initializes the serverTCP and the Remote stub
        ServerRMIAdapter remoteServer = new ServerRMIAdapter(new ServerMain());
        ServerRemoteInterface stub;
        try {
            stub = (ServerRemoteInterface) UnicastRemoteObject.exportObject(remoteServer, port); //create an interface to export
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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
     * @param client is ths object used as interface
     * @return true is login is successful
     * @throws RemoteException is there are problems with connection
     */
    public boolean login(Client client) throws RemoteException {
        clientsWithoutLobby.add(client);
        System.out.println(client.getPlayerName() + " has just logged in");
        client.postChatMessage("You joined");
        return true;
    }

    /**
     *
     * @param nick is the player name
     * @return list of lobby id of matches joined by the player
     */
    public List<Integer> getJoinedLobbies(String nick){
        List<Integer> listLobbies = //get all lobbies already joined by the client
                lobbies.keySet()
                        .stream()
                        .filter(x -> x.getPlayerNames().contains(nick))
                        .map(Lobby::getID)
                        .collect(Collectors.toList());
        return listLobbies;
    }

    /**
     * @param client requesting to join the lobby
     * @return id of assigned lobby
     */
    public LobbyRemoteCouple joinRandomLobby(Client client){
        LobbyRemoteCouple lobbyCouple;
        List<Integer> alreadyJoined = getJoinedLobbies(client.getPlayerName());
        if(alreadyJoined.size() > 0){
            lobbyCouple = joinSelectedLobby(client,alreadyJoined.get(0));
        } else {
            Lobby lobby = lobbies.keySet()
                    .stream()
                    .filter(l -> !l.isFull()) //keep only not full lobbies
                    .findFirst() //find first lobby matched
                    .orElse(null);
            if (lobby != null) { //if a lobby exists then add player
                lobby.addPlayer(client); //if exists then add player
                lobbyCouple = lobbies.get(lobby);
            } else {
                lobbyCouple = createLobby(client); //otherwise creates new lobby
            }
        }
        return lobbyCouple;
    }

    /**
     * @param client requesting to join the lobby
     */
    public LobbyRemoteCouple joinSelectedLobby(Client client, int id){
        Lobby lobby = lobbies.keySet()
                .stream()
                .filter(x -> x.getID() == id) //verify lobby exists and is not full
                .findFirst().orElse(null);
        if(lobby == null) //if a lobby exists then add player
            return null;
        try {
            lobby.addPlayer(client); //if exists then add player
            return lobbies.get(lobby);
        } catch (RuntimeException e) {
            return null;
        }
    }
    /**
     * @param client requesting to create the lobby
     */
    public LobbyRemoteCouple createLobby(Client client){
        int minFreeKey;
        int lobbyPort = Constants.port; //TODO to remove after implementation of TODO below
        List<Integer> lobbyIDs= lobbies.keySet()
                .stream()
                .map(Lobby::getID)
                .collect(Collectors.toList()); // returns list of active lobby ids
        if(lobbyIDs.contains(1)) { //check if first lobby position is free
            minFreeKey = lobbyIDs.stream()
                    .map(x -> x + 1) //look ahead of one position to each lobby
                    .filter(x -> !lobbyIDs.contains(x)) //remove all IDs already assigned to a lobby -> to prevent subsequent IDs problems
                    .min(Integer::compareTo).orElse(1);//find the lowest key number available
        } else
            minFreeKey = 1;
        Lobby lobby = new Lobby(client, minFreeKey); //cretes new lobby
        LobbyRMIAdapter lobbyRMI = new LobbyRMIAdapter(lobby); //create adapter to lobby
        //int lobbyPort =  startLobbyServer //TODO
        try {
            LobbyRemoteInterface lobbyStub = (LobbyRemoteInterface) UnicastRemoteObject.exportObject(lobbyRMI, port); //create stub of adapter of lobby
            LobbyRemoteCouple lobbyCouple = new LobbyRemoteCouple(lobbyStub, lobbyPort);
            lobbies.put(lobby, lobbyCouple);
            return lobbyCouple;

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Integer, Integer> showAvailableLobbies() throws RemoteException {
        Map<Integer, Integer> lobbyMap = new HashMap<>();
        lobbies.keySet()
                .stream()
                .filter(x -> !x.isFull())
                .forEach(x -> lobbyMap.put(x.getID(), x.getPlayers().size())); //add id lobby + num of players currently in
        return lobbyMap;
    }

    public void removeLobby(){
        //TODO
    }


}
