package it.polimi.ingsw.shared.RemoteInterfaces;

import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.model.Tile;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

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
     * @see it.polimi.ingsw.server.clientonserver.Client
     */
    public String getPlayerName() throws RemoteException;

    /**
     * @see it.polimi.ingsw.server.clientonserver.Client
     */
    public void pickedFromBoard(JSONObject position) throws RemoteException;

    /**
     * @see it.polimi.ingsw.server.clientonserver.Client
     */
    public void refreshBoard(JSONObject board) throws RemoteException;

    /**
     * @see it.polimi.ingsw.server.clientonserver.Client
     */
    public void putIntoShelf(String player, int column, Tile tile) throws RemoteException;

    /**
     * @see it.polimi.ingsw.server.clientonserver.Client
     */
    public void refreshShelf(String player, JSONObject shelf) throws RemoteException;

    /**
     * @see it.polimi.ingsw.server.clientonserver.Client
     */
    public void postChatMessage(String sender, String message) throws RemoteException;

    /**
     * @see it.polimi.ingsw.server.clientonserver.Client
     */
    public void refreshChat(Chat chat) throws RemoteException;

    /**
     * @see it.polimi.ingsw.server.clientonserver.Client
     */
    public void gameStarted(boolean newMatch) throws RemoteException;

    /**
     * @see it.polimi.ingsw.server.clientonserver.Client
     */
    public void nextTurn(String player) throws RemoteException;

    /**
     * @see it.polimi.ingsw.server.clientonserver.Client
     */
    public void refreshCommonGoal(int id, List<Integer> points) throws RemoteException;

    /**
     * @see it.polimi.ingsw.server.clientonserver.Client
     */
    public void setPlayerGoal(int id) throws RemoteException;


    /**
     * @see it.polimi.ingsw.server.clientonserver.Client
     */
    public void endGame(Map<String, Integer> leaderBoard) throws RemoteException;

    /**
     * @see it.polimi.ingsw.server.clientonserver.Client
     */
    public String ping() throws RemoteException;
}
