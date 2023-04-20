package it.polimi.ingsw.client.Connection;

import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.IpAddressV4;
import it.polimi.ingsw.shared.Move;

import java.util.Map;

public abstract class Server {
    IpAddressV4 ip;
    int port;
    public Server(IpAddressV4 ip, int port){
        this.ip = ip;
        this.port = port;
    }

    /**
     * Login the client
     * @param client
     * @return true if login was successful
     */
    public abstract boolean login(Client client);

    /**
     * Join the first available Lobby
     * @param client
     * @return the Lobby object that will handle the connection with
     * the joined lobby
     */
    public abstract void joinRandomLobby(Client client) throws ServerException;

    /**
     * Create a lobby on server
     * @param client
     * @return the Lobby object that will handle the connection with
     * the joined lobby
     */
    public abstract void createLobby(Client client) throws ServerException;

    /**
     * Get all the lobbies in which the player can log
     * @return a map of LobbyID - Number of Players in lobby
     */
    public abstract Map<Integer,Integer> getAvailableLobbies() throws ServerException;

    /**
     * Get all lobbies in which the client is present
     * @param playerName playerName of client
     * @return the lobbies that the player has joined
     */
    public abstract Map<Integer,Integer> getJoinedLobbies(String playerName) throws ServerException;

    /**
     * Join a specific lobby
     * @param client
     * @param id
     * @return the Lobby object that will handle the connection with
     * the joined lobby
     */
    public abstract void joinSelectedLobby(Client client, int id) throws ServerException;
    /**
     * Posts message to LiveChat
     * @param playerName
     * @param message
     */
    public abstract void postToLiveChat(String playerName, String message) throws LobbyException;

    /**
     * Post to private chat
     * @param sender
     * @param receiver
     * @param message
     */
    abstract public void postSecretToLiveChat(String sender, String receiver, String message) throws LobbyException;

    /**
     * Disconnect the player
     * @param player
     */
    abstract public void quitGame(String player) throws LobbyException;

    /**
     * Ask the server if the match has started
     * @return true if started
     */
    abstract public boolean matchHasStarted() throws LobbyException;

    /**
     * Post Move to Server
     * @param player
     * @param move
     */
    abstract public void postMove(String player, Move move) throws LobbyException;

    /**
     * Start the game on Server
     * @param player
     * @return true if match has actually started (the player is the admin)
     */
    abstract public boolean startGame(String player) throws LobbyException;

    /**
     * Ask Server if player is admin. Admin can Change if old admin disconnects
     * @param player
     * @return true if admin
     */
    abstract public boolean isLobbyAdmin(String player) throws LobbyException;

    /**
     * Obtain lobby ID from server
     * @return lobby id
     */
    abstract public int getLobbyID() throws LobbyException;
}
