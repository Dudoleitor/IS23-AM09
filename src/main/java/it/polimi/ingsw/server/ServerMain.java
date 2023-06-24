package it.polimi.ingsw.server;

import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientSocket;
import it.polimi.ingsw.shared.InputSanitizer;
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
import java.util.*;
import java.util.stream.Collectors;

public class ServerMain implements ServerInterface, NetworkExceptionHandler {
    private static final int RMIport = NetworkSettings.RMIport;
    private static final int TCPport = NetworkSettings.TCPport;
    private static Registry registry = null;

    /**
     * This list contains the clients connected to the
     * server that did not join a lobby yet.
     */
    private final List<Client> clientsWithoutLobby;
    private final List<Lobby> lobbies;


    /* Note: methods using the list clientsWithoutLobby need to be synchronized:
    the pings sent by the thread can result in the handleNetworkException method
    being called.
    In addition to that, multiple clients can send requests to the server and
    they must send commands one at a time. */
    private final ServerPingSender pingSender;

    private ServerMain() {
        clientsWithoutLobby = new ArrayList<>();
        lobbies = new ArrayList<>();
        this.pingSender = new ServerPingSender(this);
        pingSender.start();
    }

    public synchronized List<Client> getClientsWithoutLobby() {
        return new ArrayList<>(clientsWithoutLobby);
    }

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
    public synchronized boolean login(Client client) throws RemoteException {
        synchronized (client) {
            final String playerName = client.getPlayerName().toLowerCase();

            if (!InputSanitizer.isValidName(playerName))
                return false;

            if (clientsWithoutLobby.stream()
                    .anyMatch(x ->
                            x.getPlayerName().toLowerCase().equals(playerName))) {
                return false;
            }
            for (Lobby l : lobbies) {
                if (l.getPlayerNames().stream()
                        .anyMatch(x ->
                                x.toLowerCase().equals(playerName))) {
                    return false;
                }
            }

            clientsWithoutLobby.add(client);
            client.setExceptionHandler(this);
            System.out.println(client.getPlayerName() + " has just logged in");
            client.postChatMessage("Server", "You joined the server");
            return true;
        }
    }

    /**
     * This method is used to close empty lobbies and lobbies where
     * the match is terminated.
     */
    private synchronized void cleanupLobbies() {
        lobbies.removeIf(x -> x.getPlayerNames().isEmpty());
        lobbies.removeIf(Lobby::isTerminated);
    }

    /**
     *
     * @param nick is the player name
     * @return list of lobby id of matches joined by the player
     */
    public synchronized int getJoinedLobby(String nick){
        int lobby;//get lobby already joined by the client
                lobby = lobbies.stream()
                        .filter(x -> x.getPlayerNames().contains(nick))
                        .map(Lobby::getID)
                        .findFirst().orElse(-1);
        return lobby;
    }

    /**
     * @param client requesting to join the lobby
     * @return id of assigned lobby
     */
    public synchronized ServerLobbyInterface joinRandomLobby(Client client) throws RemoteException {
        cleanupLobbies();

        if (!clientsWithoutLobby.contains(client))
            throw new RemoteException("Client is not in the list of clients without lobby");

        ServerLobbyInterface lobbyInterface;
        int alreadyJoined = getJoinedLobby(client.getPlayerName());
        if(alreadyJoined > 0){
            lobbyInterface = joinSelectedLobby(client,alreadyJoined);
        } else {
            Lobby lobby = lobbies.stream()
                    .filter(l -> !l.isFull() && !l.matchHasStarted()) //keep only not full lobbies
                    .findFirst() //find first lobby matched
                    .orElse(null);
            if (lobby != null) { //if a lobby exists then add player
                lobby.addPlayer(client); //if exists then add player
                clientsWithoutLobby.remove(client);
                lobbyInterface = lobby;
            } else {
                lobbyInterface = createLobby(client); //otherwise creates new lobby
            }
        }
        try {
            System.out.println(client.getPlayerName() + " has just joined lobby " + lobbyInterface.getID());
        } catch (RemoteException ignored) {
        }
        return lobbyInterface;
    }

    /**
     * @param client requesting to join the lobby
     */
    public synchronized ServerLobbyInterface joinSelectedLobby(Client client, int id) throws RemoteException {
        if (!clientsWithoutLobby.contains(client))
            throw new RemoteException("Client is not in the list of clients without lobby");

        Lobby lobby = lobbies.stream()
                .filter(x -> x.getID() == id)  //verify lobby exists and is not full
                .findFirst().orElse(null);

        if(lobby == null)
            throw new RemoteException("Lobby does not exist");

        //if a lobby exists then add player
        lobby.addPlayer(client); //if exists then add player
        clientsWithoutLobby.remove(client);
        System.out.println(client.getPlayerName() + " has just joined lobby " + lobby.getID());
        return lobby;
    }
    /**
     * @param client requesting to create the lobby
     */
    public synchronized ServerLobbyInterface createLobby(Client client) throws RemoteException {
        if (!clientsWithoutLobby.contains(client))
            throw new RemoteException("Client is not in the list of clients without lobby");

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
            clientsWithoutLobby.remove(client);
            System.out.println(client.getPlayerName() + " created a new lobby with id " + minFreeKey);
            return lobby;

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Map<Integer, Integer> showAvailableLobbies() throws RemoteException {
        cleanupLobbies();

        Map<Integer, Integer> lobbyMap = new HashMap<>();
        lobbies.stream()
                .filter(x -> !x.isFull() && !x.matchHasStarted())
                .forEach(x -> lobbyMap.put(x.getID(), x.getClients().size())); //add id lobby + num of players currently in
        return lobbyMap;
    }

    /**
     * This function is used to handle network exceptions thrown by RMI or the socket.
     *
     * @param client Client object
     * @param e      Exception thrown
     */
    @Override
    public synchronized void handleNetworkException(Client client, Exception e) {
        System.err.println("Exception thrown (in server main) while trying to reach client " + client.getPlayerName() + ": " + e.getMessage());
        client.disconnect();
        if(clientsWithoutLobby.remove(client))
            System.out.println("Disconnected client " + client.getPlayerName() + ": " + e.getMessage());
        else
            System.err.println("Called handleNetworkException on client " + client.getPlayerName() + " that is not in the list of clients without lobby");
    }

    /**
     * This function is used to check if the client was already connected to
     * the specified lobby and was previously disconnected.
     * @param playerName String name of the player
     * @return Int id of the lobby if the player was previously connected,
     * -1 if not.
     */
    @Override
    public synchronized int disconnectedFromLobby(String playerName) {
        cleanupLobbies();

        Optional<Lobby> lobbyOpt = lobbies.stream().filter(x-> x.getDisconnectedClients().contains(playerName.toLowerCase())).findFirst();
        return lobbyOpt.map(Lobby::getID).orElse(-1);
    }
}

class ServerPingSender extends Thread {

    private final ServerMain server;  // Needed for proper synchronization

    public ServerPingSender(ServerMain server) {
        this.server = server;
    }

    /**
     * This Runnable is used to ping clients, if a client is not available
     * an exception is thrown and the exception handles kicks in.
     */
    @Override
    public void run() {
        while(true) {
            synchronized (server) {
                for (Client c : server.getClientsWithoutLobby())
                    c.ping();
            }
            try {
                Thread.sleep(NetworkSettings.serverPingIntervalSeconds * 1000);
            } catch (InterruptedException ignored) {
                return;
            }
        }
    };
}
