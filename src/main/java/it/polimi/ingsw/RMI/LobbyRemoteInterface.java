package it.polimi.ingsw.RMI;

import it.polimi.ingsw.server.Move;
import it.polimi.ingsw.shared.JsonBadParsingException;
import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LobbyRemoteInterface extends Remote {
    void postToLiveChat(String playerName, String message) throws Exception;
    public void postSecretToLiveChat(String sender, String receiver, String message) throws Exception;
    List<ChatMessage> updateLiveChat(String player, int alreadyReceived) throws RemoteException;
    void quitGame(String player, LobbyRemoteInterface stub) throws RemoteException;
    boolean matchHasStarted() throws RemoteException;
    void postMove(String player,Move move) throws RemoteException;
    boolean startGame(String player) throws RemoteException;
    boolean isLobbyAdmin(String player) throws RemoteException;
}
