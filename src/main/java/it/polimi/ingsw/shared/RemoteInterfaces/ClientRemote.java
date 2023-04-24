package it.polimi.ingsw.shared.RemoteInterfaces;

import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Tile;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This object is used on the client to receive updates
 * sent by the model. This object will be wrapped inside
 * a ClientRMI object.
 * The server, inside ClientRMI, will invoke methods on
 * this object and the remote execution will be
 * handled by RMI.
 * Please note that the interface is superfluous, but it's
 * needed by RMI to work properly.
 */
public interface ClientRemote extends Remote, Serializable {

    /**
     * This method is used to return the name of
     * the players using this client.
     * @return String, player's name.
     */
    public String getPlayerName() throws RemoteException;

    /**
     * This method is used when a player picks a tile
     * from the board. It sends the message
     * to the remote view to remove the tile
     * from the board.
     *
     * @param position position
     */
    public void pickedFromBoard(Position position) throws RemoteException;

    /**
     * This method is used to transfer the whole board
     * to the remote view,
     * it uses a json string.
     *
     * @param board JSONObject.toJsonString
     */
    public void refreshBoard(String board) throws RemoteException;

    /**
     * This method is used when a player inserts a single
     * tile into his shelf.
     * It is used to send the message to the remote view
     * of the client in order to insert the tile
     * into the shelf.
     *
     * @param player String name of the player that moved the tile
     * @param column destination column of the shelf
     * @param tile Tile to insert
     */
    public void putIntoShelf(String player, int column, Tile tile) throws RemoteException;

    /**
     * This method is used to transfer the whole shelf
     * of a player to the remote view of the client,
     * it uses a json string.
     *
     * @param player name of the player
     * @param shelf JSONObject.toJsonString
     */
    public void refreshShelf(String player, String shelf) throws RemoteException;

    /**
     * This method is used to send a chat message to clients.
     * @param sender Player's name
     * @param message String message
     */
    public void postChatMessage(String sender, String message) throws RemoteException;

    /**
     * This method is used to send the whole chat to the client,
     * it is used when a refresh is needed.
     * @param chat Chat object
     */
    public void refreshChat(Chat chat) throws RemoteException;
}
