package it.polimi.ingsw.shared.RemoteInterfaces;

import org.json.simple.JSONObject;

import java.rmi.Remote;

/**
 * This object is used to expose methods related to
 * after-lobby-join operations and is used during the play,
 * it resides on the server.
 * Methods are invoked using RMI by RMI clients,
 * proper adapters invoke methods when using TCP.
 */
public interface ServerLobbyInterface extends Remote {
    void postToLiveChat(String playerName, String message) throws Exception;
    public void postSecretToLiveChat(String sender, String receiver, String message) throws Exception;
    void quitGame(String player) throws Exception;
    boolean matchHasStarted() throws Exception;
    void postMove(String player, JSONObject move) throws Exception;
    boolean startGame(String player) throws Exception;
    boolean isLobbyAdmin(String player) throws Exception;
    int getID() throws Exception;
    String getCurrentPlayer() throws Exception;
}
