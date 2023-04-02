package it.polimi.ingsw.RMI;
import it.polimi.ingsw.shared.Color;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Shelf;
import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteCall extends Remote {
    boolean login(String nick) throws RemoteException;
    void sendShelf(JSONObject s) throws RemoteException, JsonBadParsingException;
    void postToLiveChat(String playerName, String message) throws Exception;
    List<ChatMessage> updateLiveChat(String player, int alreadyReceived) throws RemoteException;

    void joinLobby(String player, RemoteCall stub) throws RemoteException;
    void createLobby(String player, RemoteCall stub,  int numPlayers) throws RemoteException;
    void quitGame(String player, RemoteCall stub) throws RemoteException;
}
