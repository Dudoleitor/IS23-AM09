package it.polimi.ingsw.client.Connection;

import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.IpAddressV4;

import java.util.Map;

public abstract class Server {
    IpAddressV4 ip;
    int port;
    Server(IpAddressV4 ip, int port){
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
    public abstract Lobby joinRandomLobby(Client client) throws ServerException;

    /**
     * Create a lobby on server
     * @param client
     * @return the Lobby object that will handle the connection with
     * the joined lobby
     */
    abstract Lobby createLobby(Client client) throws ServerException;

    /**
     * Get all the lobbies in which the player can log
     * @return a map of LobbyID - Number of Players in lobby
     */
    public abstract Map<Integer,Integer> getAvailableLobbies() throws ServerException;

    /**
     * Get all lobbies in which the client is present
     * @param playerName playerName of client
     * @return
     */
    public abstract Map<Integer,Integer> getJoinedLobbies(String playerName) throws ServerException;

    /**
     * Join a specific lobby
     * @param client
     * @param id
     * @return the Lobby object that will handle the connection with
     * the joined lobby
     */
    public abstract Lobby joinSelectedLobby(Client client, int id) throws ServerException;
}
