package it.polimi.ingsw.server;

import it.polimi.ingsw.client.InputSanitizer;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;
import it.polimi.ingsw.shared.Constants;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyRemoteInterface;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerRemoteInterface;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

public class ServerMain implements ServerRemoteInterface {
    private static boolean keepOn = true;
    private static final int port = Constants.port;

    /**
     * This list contains the clients connected to the
     * server that did not join a lobby yet.
     */
    private final List<Client> clientsWithoutLobby = new ArrayList<>();

    private final Map<Lobby, LobbyRemoteInterface> lobbies = new HashMap<>(); //this hashmap is to remember every couple of Lobby <-> RemoteInterface to communicate with

    private static Registry registry = null;
    private static InputSanitizer inputSanitizer;
    public static void main(String argv[]){
        ServerMain obj = new ServerMain();
        ServerRemoteInterface stub;
        try {
            stub = (ServerRemoteInterface) UnicastRemoteObject.exportObject(obj, port); //create an interface to export
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
     * @param clientRMI is ths object used as RMI interface
     * @return true is login is successful
     * @throws RemoteException is there are problems with connection
     */
    @Override
    public boolean login(ClientRMI clientRMI) throws RemoteException {

        clientsWithoutLobby.add(clientRMI);
        System.out.println(clientRMI.getPlayerName() + " has just logged in");
        clientRMI.postChatMessage("You joined");

        return true;
    }

    /**
     *
     * @param nick is the player name
     * @return list of lobby id of matches joined by the player
     */
    @Override
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
    @Override
    public LobbyRemoteInterface joinRandomLobby(Client client){
        LobbyRemoteInterface lobbyStub;
        List<Integer> alreadyJoined = getJoinedLobbies(client.getPlayerName());
        if(alreadyJoined.size() > 0){
            lobbyStub = joinSelectedLobby(client,alreadyJoined.get(0));
        } else {
            Lobby lobby = lobbies.keySet()
                    .stream()
                    .filter(l -> !l.isFull()) //keep only not full lobbies
                    .findFirst() //find first lobby matched
                    .orElse(null);
            if (lobby != null) { //if a lobby exists then add player
                lobby.addPlayer(client); //if exists then add player
                lobbyStub = lobbies.get(lobby);
            } else {
                lobbyStub = createLobby(client); //otherwise creates new lobby
            }
        }
        return lobbyStub;
    }

    /**
     * @param client requesting to join the lobby
     */
    @Override
    public LobbyRemoteInterface joinSelectedLobby(Client client, int id){
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
    @Override
    public LobbyRemoteInterface createLobby(Client client){
        int minFreeKey;
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
        Lobby lobby = new Lobby(client, minFreeKey);
        try {
            LobbyRemoteInterface lobbyStub = (LobbyRemoteInterface) UnicastRemoteObject.exportObject(lobby, port); //create an interface to export
            lobbies.put(lobby, lobbyStub);
            return lobbyStub;

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Integer, Integer> showAvailableLobbbies() throws RemoteException {
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
