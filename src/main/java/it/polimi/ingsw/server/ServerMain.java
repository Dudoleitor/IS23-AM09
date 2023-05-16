package it.polimi.ingsw.server;

import it.polimi.ingsw.client.View.cli.InputSanitizer;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientSocket;
import it.polimi.ingsw.shared.NetworkSettings;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerLobbyInterface;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

public class ServerMain implements ServerInterface, NetworkExceptionHandler {
    private static final int RMIport = NetworkSettings.RMIport;
    private static final int TCPport = NetworkSettings.TCPport;

    /**
     * This list contains the clients connected to the
     * server that did not join a lobby yet.
     */
    private final List<Client> clientsWithoutLobby = new ArrayList<>();
    private final List<Lobby> lobbies = new ArrayList<>(); //this hashmap is to remember every couple of Lobby <-> RemoteInterface to communicate with
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
            while(true) {
                Socket client = serverSocket.accept();
                ClientSocket clientSocket = new ClientSocket();
                clientSocket.setClientSocket(client);
                new ServerTcpThread(server, clientSocket).start();

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
    public boolean login(Client client) throws RemoteException {
        clientsWithoutLobby.add(client);
        client.setExceptionHandler(this);
        System.out.println(client.getPlayerName() + " has just logged in");
        client.postChatMessage("Server", "You joined");
        return true;
    }

    /**
     *
     * @param nick is the player name
     * @return list of lobby id of matches joined by the player
     */
    public Map<Integer,Integer> getJoinedLobbies(String nick){
        Map<Integer,Integer> lobbyList = new HashMap<>();//get all lobbies already joined by the client
                lobbies.stream()
                        .filter(x -> x.getPlayerNames().contains(nick))
                        .forEach(x -> lobbyList.put(x.getID(),x.getPlayerNames().size()));
        return lobbyList;
    }

    /**
     * @param client requesting to join the lobby
     * @return id of assigned lobby
     */
    public ServerLobbyInterface joinRandomLobby(Client client){
        ServerLobbyInterface lobbyInterface;
        Map<Integer,Integer> alreadyJoined = getJoinedLobbies(client.getPlayerName());
        if(alreadyJoined.size() > 0){
            int alreadyJoinedID = alreadyJoined.
                    keySet().
                    stream().
                    findFirst().
                    orElse(-1);
            lobbyInterface = joinSelectedLobby(client,alreadyJoinedID);
        } else {
            Lobby lobby = lobbies.stream()
                    .filter(l -> !l.isFull() && !l.matchHasStarted()) //keep only not full lobbies
                    .findFirst() //find first lobby matched
                    .orElse(null);
            if (lobby != null) { //if a lobby exists then add player
                lobby.addPlayer(client); //if exists then add player
                lobbyInterface = lobby;
            } else {
                lobbyInterface = createLobby(client); //otherwise creates new lobby
            }
        }
        return lobbyInterface;
    }

    /**
     * @param client requesting to join the lobby
     */
    public ServerLobbyInterface joinSelectedLobby(Client client, int id){
        Lobby lobby = lobbies.stream()
                .filter(x -> x.getID() == id) //verify lobby exists and is not full
                .findFirst().orElse(null);
        if(lobby == null) //if a lobby exists then add player
            return null;
        try {
            lobby.addPlayer(client); //if exists then add player
            return lobby;
        } catch (RuntimeException e) {
            return null;
        }
    }
    /**
     * @param client requesting to create the lobby
     */
    public ServerLobbyInterface createLobby(Client client){
        int minFreeKey;
        List<Integer> lobbyIDs= lobbies.stream()
                .map(Lobby::getID)
                .collect(Collectors.toList()); // returns list of active lobby ids
        if(lobbyIDs.contains(1)) { //check if first lobby position is free
            minFreeKey = lobbyIDs.stream()
                    .map(x -> x + 1) //look ahead of one position to each lobby
                    .filter(x -> !lobbyIDs.contains(x)) //remove all IDs already assigned to a lobby -> to prevent subsequent IDs problems
                    .min(Integer::compareTo).orElse(1);//find the lowest key number available
        } else
            minFreeKey = 1;
        try {
            Lobby lobby = new Lobby(client, minFreeKey); //creates new lobby

            lobbies.add(lobby);

            return lobby;

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Integer, Integer> showAvailableLobbies() throws RemoteException {
        Map<Integer, Integer> lobbyMap = new HashMap<>();
        lobbies.stream()
                .filter(x -> !x.isFull() && !x.matchHasStarted())
                .forEach(x -> lobbyMap.put(x.getID(), x.getClients().size())); //add id lobby + num of players currently in
        return lobbyMap;
    }

    public void removeLobby(){
        //TODO
    }

    /**
     * This function is used to handle network exceptions thrown by RMI or the socket.
     *
     * @param client Client object
     * @param e      Exception thrown
     */
    @Override
    public void handleNetworkException(Client client, Exception e) {
        if(!clientsWithoutLobby.remove(client)) {
            throw new RuntimeException("Provided client was not connected");
        }
        System.err.println("Disconnected client " + client.getPlayerName() + ": " + e.getMessage());
    }

    /**
     * This function is used to check if the client was already connected to
     * the specified lobby and was previously disconnected.
     * @param playerName String name of the player
     * @return Int id of the lobby if the player was previously connected,
     * -1 if not.
     */
    @Override
    public int disconnectedFromLobby(String playerName) {
        Optional<Lobby> lobbyOpt = lobbies.stream().filter(x-> x.getDisconnectedClients().contains(playerName.toLowerCase())).findFirst();
        return lobbyOpt.map(Lobby::getID).orElse(-1);
    }
}
