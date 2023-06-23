package it.polimi.ingsw.client.model;

import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.RemoteInterfaces.ClientRemote;
import it.polimi.ingsw.shared.model.Board;
import it.polimi.ingsw.shared.model.Shelf;

import java.rmi.RemoteException;
import java.util.Map;

/**
 * This object is used to receive updates coming from
 * the server, it resides on the client. When RMI is
 * used, the object is wrapped inside ClientRMI and
 * methods are called directly from the server.
 * With TCP, proper adapters parse messages coming
 * from the network and call the methods.
 */
public interface ClientModel extends ClientRemote{
    /**
     * @return True if the player need to play in the current turn
     */
    public boolean isItMyTurn() throws RemoteException;
    public boolean amIFirstPlayer() throws RemoteException;
    public Board getBoard() throws RemoteException;
    public Map<String, Shelf> getPlayersShelves() throws RemoteException;
    public Chat getChat() throws RemoteException;

    public boolean gameEnded() throws RemoteException;
    public boolean gameIsStarted() throws RemoteException;
}
