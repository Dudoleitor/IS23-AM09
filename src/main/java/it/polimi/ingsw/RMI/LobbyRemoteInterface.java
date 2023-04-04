package it.polimi.ingsw.RMI;

import it.polimi.ingsw.server.Move;
import it.polimi.ingsw.shared.JsonBadParsingException;
import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LobbyRemoteInterface extends Remote {
    void sendShelf(JSONObject s) throws RemoteException, JsonBadParsingException;
    void postToLiveChat(String playerName, String message) throws Exception;
    public void postSecretToLiveChat(String sender, String receiver, String message) throws Exception;
    List<ChatMessage> updateLiveChat(String player, int alreadyReceived) throws RemoteException;
    void quitGame(String player, LobbyRemoteInterface stub) throws RemoteException;
    boolean matchHasStarted() throws RemoteException;
    boolean isMyTurn(String player) throws RemoteException;
    List<Move> getValidMoves(String player) throws RemoteException;
    void postMove(String player,int moveCode) throws RemoteException;
    boolean startGame(String player) throws RemoteException;
}
