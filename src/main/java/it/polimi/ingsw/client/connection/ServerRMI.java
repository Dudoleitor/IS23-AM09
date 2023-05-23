package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;
import it.polimi.ingsw.shared.IpAddressV4;
import it.polimi.ingsw.shared.model.Move;
import it.polimi.ingsw.shared.NetworkSettings;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerLobbyInterface;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class ServerRMI extends Server {
    private ServerInterface server;
    public ServerRMI(IpAddressV4 ip, int port){
        super(ip,port);
    }
    private ServerLobbyInterface lobby;
    public void initLobby(ServerLobbyInterface lobby){
        this.lobby = lobby;
    }

    @Override
    public boolean login(Client client) throws ServerException {
        try {
            if(server == null){
                //get remote registry that points to 127.0.0.1:port
                Registry registry = LocateRegistry.getRegistry(NetworkSettings.serverIp.toString(), NetworkSettings.RMIport);
                //get interface from remote registry
                server = (ServerInterface) registry.lookup("interface");
                //try to log in
            }
            return server.login((ClientRMI) client);
        } catch (NotBoundException | RemoteException e) {
            throw new ServerException("Error while connecting to server, " + e.getMessage());
        }
    }

    @Override
    public int getJoinedLobby(String playerName) throws ServerException{
        int lobbyId;
        try {
            lobbyId = server.getJoinedLobby(playerName);
        } catch (RemoteException e) {
            throw new ServerException("Error in Server");
        }
        return lobbyId;
    }

    @Override
    public void joinRandomLobby(Client client) throws ServerException{
        try{
            lobby = server.joinRandomLobby(client);
        } catch (RemoteException e) {
            throw new ServerException("Error in Server");
        }
    }

    @Override
    public void joinSelectedLobby(Client client, int id) throws ServerException{
        try{
            lobby = server.joinSelectedLobby(client,id);
        } catch (RemoteException e) {
            throw new ServerException("Error in Server");
        }
    }

    @Override
    public void createLobby(Client client) throws ServerException{
        try{
            lobby = server.createLobby(client);
        } catch (RemoteException e) {
            throw new ServerException("Error in Server");
        }
    }

    @Override
    public Map<Integer, Integer> getAvailableLobbies() throws ServerException{
        Map<Integer, Integer> availableLobbies;
        try{
            availableLobbies = server.showAvailableLobbies();
        } catch (RemoteException e) {
            throw new ServerException("Error in Server");
        }
        if (availableLobbies!=null)
            return availableLobbies;
        return new HashMap<>();
    }
    @Override
    public void postToLiveChat(String playerName, String message) throws LobbyException {
        try {
            lobby.postToLiveChat(playerName,message);
        } catch (RemoteException e) {
            throw new LobbyException("Error in Lobby");
        }
    }

    @Override
    public void postSecretToLiveChat(String sender, String receiver, String message) throws LobbyException{
        try{
            lobby.postSecretToLiveChat(sender,receiver,message);
        }
        catch (RemoteException e){
            throw new LobbyException("Error in Lobby");
        }
    }

    @Override
    public void quitGame(String player){
        try{
            lobby.quitGame(player);
        } catch (RemoteException ignored) {
        }
    }

    @Override
    public boolean matchHasStarted() throws LobbyException{
        boolean started = false;
        try{
            started=  lobby.matchHasStarted();
        } catch (RemoteException e) {
            throw new LobbyException("Error in Lobby");
        }
        return started;
    }

    @Override
    public void postMove(String player, Move move) throws LobbyException{
        try {
            lobby.postMove(player,move.toJson());
        } catch (RemoteException e) {
            throw new LobbyException("Error in Lobby");
        }
    }

    @Override
    public boolean startGame(String player, boolean erasePreviousMatches) throws LobbyException{
        boolean hasStarted = false;
        try{
            hasStarted = lobby.startGame(player, erasePreviousMatches);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new LobbyException("Error in Lobby");
        }
        return hasStarted;
    }

    @Override
    public boolean isLobbyAdmin(String player) throws LobbyException{
        boolean result = false;
        try{
            result = lobby.isLobbyAdmin(player);
        } catch (RemoteException e) {
            throw new LobbyException("Error in Lobby");
        }
        return result;
    }

    @Override
    public int getLobbyID() throws LobbyException{
        int id = 0;
        try {
            id = lobby.getID();
        } catch (RemoteException | NullPointerException e) {
            throw new LobbyException("Error in Lobby");
        }
        return id;
    }

    /**
     * This method is used to observe the player supposed
     * to play in the current turn.
     *
     * @return String name of the player
     */
    @Override
    public String getCurrentPlayer() throws LobbyException {
        try {
            return lobby.getCurrentPlayer();
        } catch (RemoteException e) {
            throw new LobbyException(e.getMessage());
        }
    }

    /**
     * This function is used to check if the client was already connected to
     * a lobby and was previously disconnected.
     *
     * @param playerName String name of the player
     * @return Int id of the lobby if the player was previously connected,
     * -1 if not.
     */
    @Override
    public int disconnectedFromLobby(String playerName) throws ServerException {
        try {
            return server.disconnectedFromLobby(playerName);
        } catch (RemoteException e) {
            throw new ServerException(e.getMessage());
        }
    }
}
