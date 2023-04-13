package it.polimi.ingsw.shared;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This object is used on the client to receive updates
 * sent by the model. This object will be wrapped inside
 * a ClientRMI object.
 * The server, inside ClientRMI will invoke methods on
 * this object and the remote execution will be
 * handled by RMI.
 */
public class ClientRemoteObject extends UnicastRemoteObject implements ClientRemote {

    // TODO add board and shelves
    private final String playerName;
    public ClientRemoteObject(String playerName) throws RemoteException {
        super();
        this.playerName = playerName;
    }

    /**
     * This method is used to return the name of
     * the players using this client.
     *
     * @return String, player's name.
     */
    @Override
    public String getPlayerName() throws RemoteException {
        return playerName;
    }

    /**
     * This method is used when a player picks a tile
     * from the board. It sends the message
     * to the remote view to remove the tile
     * from the board.
     *
     * @param position position
     */
    @Override
    public void pickedFromBoard(Position position) throws RemoteException {
        //TODO
    }

    /**
     * This method is used to transfer the whole board
     * to the remote view,
     * it uses a json string.
     *
     * @param board JSONObject.toJsonString
     */
    @Override
    public void refreshBoard(String board) throws RemoteException {
        // TODO
    }

    /**
     * This method is used when a player inserts a single
     * tile into his shelf.
     * It is used to send the message to the remote view
     * of the client in order to insert the tile
     * into the shelf.
     *
     * @param player String name of the player that moved the tile
     * @param column destination column of the shelf
     * @param tile   Tile to insert
     */
    @Override
    public void putIntoShelf(String player, int column, Tile tile) throws RemoteException {
        // TODO
    }

    /**
     * This method is used to transfer the whole shelf
     * of a player to the remote view of the client,
     * it uses a json string.
     *
     * @param player name of the player
     * @param shelf  JSONObject.toJsonString
     */
    @Override
    public void refreshShelf(String player, String shelf) throws RemoteException {
        // TODO
    }

    /**
     * This method is used to send a chat message to clients.
     * THIS IS TEMPORARY, we'll be updated
     * @param message
     */
    // TODO
    public void postChatMessage(String message) throws RemoteException {
        System.out.println(message);
    }
}
