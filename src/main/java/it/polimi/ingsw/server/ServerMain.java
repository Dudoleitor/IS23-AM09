package it.polimi.ingsw.server;

import it.polimi.ingsw.client.InputSanitizer;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientSocket;
import it.polimi.ingsw.shared.ChatMessage;
import it.polimi.ingsw.shared.Color;
import it.polimi.ingsw.shared.GameSettings;
import it.polimi.ingsw.shared.NetworkSettings;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyRemoteCouple;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyInterface;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

public class ServerMain implements ServerInterface{
    private static final int RMIport = NetworkSettings.RMIport;
    private static final int TCPport = NetworkSettings.TCPport;

    /**
     * This list contains the clients connected to the
     * server that did not join a lobby yet.
     */
    private final List<Client> clientsWithoutLobby = new ArrayList<>();
    private final Map<Lobby, LobbyRemoteCouple> lobbies = new HashMap<>(); //this hashmap is to remember every couple of Lobby <-> RemoteInterface to communicate with
    private static Registry registry = null;
    private static InputSanitizer inputSanitizer;

    public static void startServer(){ //in the main it initializes the serverTCP and the Remote stub
        ServerMain server = new ServerMain();

        RMIIni(server); //creates server stub and the registry to get it
        System.out.println("Server is on");
        SocketIni(server);
        while (true) {
            Thread.onSpinWait(); //is used to suspend the process and make it wait
        } //to keep it online
        //TODO on exit we have to kill the socket and unbind the remote interface
    }

    /**
     * This method is used to create the Server stub
     * @param server is the object used to create the stub
     */

    private static void RMIIni(ServerMain server){
        ServerInterface stub;
        try {
            stub = (ServerInterface) UnicastRemoteObject.exportObject(server, RMIport); //create an interface to export
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        try {
            registry = LocateRegistry.createRegistry(RMIport); //create a registry that accepts request on a defined port
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

    }

    private static void SocketIni(ServerMain server){ //TODO WIP
        try {
            ServerSocket serverSocket = new ServerSocket(TCPport);
            while(true){
                Socket client = serverSocket.accept();
                new LoginTcpThread(server, new ClientSocket(client)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     *
     * @param client is ths object used as interface
     * @return true is login is successful
     * @throws RemoteException is there are problems with connection
     */
    @Override
    public boolean login(Client client) throws RemoteException {
        clientsWithoutLobby.add(client);
        System.out.println(client.getPlayerName() + " has just logged in");
        client.postChatMessage(new ChatMessage("Server", "You joined", Color.Green));
        return true;
    }

    /**
     *
     * @param nick is the player name
     * @return list of lobby id of matches joined by the player
     */
    @Override
    public Map<Integer,Integer> getJoinedLobbies(String nick){
        Map<Integer,Integer> lobbyList = new HashMap<>();//get all lobbies already joined by the client
                lobbies.keySet()
                        .stream()
                        .filter(x -> x.getPlayerNames().contains(nick))
                        .forEach(x -> lobbyList.put(x.getID(),x.getPlayerNames().size()));
        return lobbyList;
    }

    /**
     * @param client requesting to join the lobby
     * @return id of assigned lobby
     */
    @Override
    public LobbyRemoteCouple joinRandomLobby(Client client){
        LobbyRemoteCouple lobbyCouple;
        Map<Integer,Integer> alreadyJoined = getJoinedLobbies(client.getPlayerName());
        if(alreadyJoined.size() > 0){
            int alreadyJoinedID = alreadyJoined.
                    keySet().
                    stream().
                    findFirst().
                    orElse(-1);
            lobbyCouple = joinSelectedLobby(client,alreadyJoinedID);
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
    @Override
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
    @Override
    public LobbyRemoteCouple createLobby(Client client){
        int minFreeKey;
        int lobbyPort = NetworkSettings.RMIport; //TODO to remove after implementation of TODO below
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
        //int lobbyPort =  startLobbyServer //TODO
        try {
            LobbyInterface lobbyStub = (LobbyInterface) UnicastRemoteObject.exportObject(lobby, RMIport); //create stub of adapter of lobby
            LobbyRemoteCouple lobbyCouple = new LobbyRemoteCouple(lobbyStub, lobbyPort);
            lobbies.put(lobby, lobbyCouple);
            return lobbyCouple;

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
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
