package it.polimi.ingsw.shared.RemoteInterfaces;

import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This object is used to expose methods related to
 * after-lobby-join operations and is used during the play,
 * it resides on the server.
 * Methods are invoked using RMI by RMI clients,
 * proper adapters invoke methods when using TCP.
 */
public interface ServerLobbyInterface extends Remote {
    void postToLiveChat(String playerName, String message) throws RemoteException;
    public void postSecretToLiveChat(String sender, String receiver, String message) throws RemoteException;
    void quitGame(String player) throws RemoteException;
    boolean matchHasStarted() throws RemoteException;
    void postMove(String player, JSONObject move) throws RemoteException;
    boolean startGame(String player, boolean erasePreviousMatches) throws RemoteException;
    boolean isLobbyAdmin(String player) throws RemoteException;
    int getID() throws RemoteException;
    String getCurrentPlayer() throws RemoteException;
}
