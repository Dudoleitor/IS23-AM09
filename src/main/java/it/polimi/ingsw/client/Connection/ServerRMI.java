package it.polimi.ingsw.client.Connection;

import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;
import it.polimi.ingsw.shared.IpAddressV4;
import it.polimi.ingsw.shared.Move;
import it.polimi.ingsw.shared.NetworkSettings;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerLobbyInterface;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
    public boolean login(Client client) {
        try {
            if(server == null){
                //get remote registry that points to 127.0.0.1:port
                Registry registry = LocateRegistry.getRegistry(NetworkSettings.serverIp.toString(), NetworkSettings.RMIport);
                //get interface from remote registry
                server = (ServerInterface) registry.lookup("interface");
                //try to log in
            }
            server.login((ClientRMI) client);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<Integer,Integer> getJoinedLobbies(String playerName) throws ServerException{
        Map<Integer,Integer> lobbies = null;
        try {
            lobbies = server.getJoinedLobbies(playerName);
        } catch (Exception e) {
            throw new ServerException("Error in Server");
        }
        return lobbies;
    }

    @Override
    public void joinRandomLobby(Client client) throws ServerException{
        try{
            lobby = server.joinRandomLobby(client);
        } catch (Exception e) {
            throw new ServerException("Error in Server");
        }
    }

    @Override
    public void joinSelectedLobby(Client client, int id) throws ServerException{
        try{
            lobby = server.joinSelectedLobby(client,id);
        } catch (Exception e) {
            throw new ServerException("Error in Server");
        }
    }

    @Override
    public void createLobby(Client client) throws ServerException{
        try{
            lobby = server.createLobby(client);
        } catch (Exception e) {
            throw new ServerException("Error in Server");
        }
    }

    @Override
    public Map<Integer, Integer> getAvailableLobbies() throws ServerException{
        Map<Integer, Integer> availableLobbies = null;
        try{
            availableLobbies = server.showAvailableLobbies();
        } catch (Exception e) {
            throw new ServerException("Error in Server");
        }
        return availableLobbies;
    }
    @Override
    public void postToLiveChat(String playerName, String message) throws LobbyException {
        try {
            lobby.postToLiveChat(playerName,message);
        } catch (Exception e) {
            throw new LobbyException("Error in Lobby");
        }
    }

    @Override
    public void postSecretToLiveChat(String sender, String receiver, String message) throws LobbyException{
        try{
            lobby.postSecretToLiveChat(sender,receiver,message);
        }
        catch (Exception e){
            throw new LobbyException("Error in Lobby");
        }
    }

    @Override
    public void quitGame(String player){
        try{
            lobby.quitGame(player);
        } catch (Exception e) {
            //do nothing and quit anyway
        }
    }

    @Override
    public boolean matchHasStarted() throws LobbyException{
        boolean started = false;
        try{
            started=  lobby.matchHasStarted();
        } catch (Exception e) {
            throw new LobbyException("Error in Lobby");
        }
        return started;
    }

    @Override
    public void postMove(String player, Move move) throws LobbyException{
        try {
            lobby.postMove(player,move.toJson());
        } catch (Exception e) {
            throw new LobbyException("Error in Lobby");
        }
    }

    @Override
    public boolean startGame(String player) throws LobbyException{
        boolean hasStarted = false;
        try{
            hasStarted = lobby.startGame(player);
        } catch (Exception e) {
            throw new LobbyException("Error in Lobby");
        }
        return hasStarted;
    }

    @Override
    public boolean isLobbyAdmin(String player) throws LobbyException{
        boolean result = false;
        try{
            result = lobby.isLobbyAdmin(player);
        } catch (Exception e) {
            throw new LobbyException("Error in Lobby");
        }
        return result;
    }

    @Override
    public int getLobbyID() throws LobbyException{
        int id = 0;
        try {
            id = lobby.getID();
        } catch (Exception e) {
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
        } catch (Exception e) {
            throw new LobbyException(e.getMessage());
        }
    }
}
