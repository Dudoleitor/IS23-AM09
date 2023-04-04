package it.polimi.ingsw.RMI;
import it.polimi.ingsw.server.Move;
import it.polimi.ingsw.shared.JsonBadParsingException;
import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerRemoteInterface extends Remote {
    boolean login(String nick) throws RemoteException;
    void sendShelf(JSONObject s) throws RemoteException, JsonBadParsingException;
    void postToLiveChat(String playerName, String message) throws Exception;
    List<ChatMessage> updateLiveChat(String player, int alreadyReceived) throws RemoteException;

    void joinLobby(String player, ServerRemoteInterface stub) throws RemoteException;
    void createLobby(String player, ServerRemoteInterface stub, int numPlayers) throws RemoteException;
    void quitGame(String player, ServerRemoteInterface stub) throws RemoteException;
    boolean matchHasStarted(String player) throws RemoteException;
    boolean isMyTurn(String player) throws RemoteException;
    List<Move> getValidMoves(String player) throws RemoteException;
    void postMove(String player,int moveCode) throws RemoteException;
}
