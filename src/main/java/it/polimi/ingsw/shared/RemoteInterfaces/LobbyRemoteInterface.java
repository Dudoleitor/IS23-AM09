package it.polimi.ingsw.shared.RemoteInterfaces;

import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Move;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LobbyRemoteInterface extends Remote {
    void postToLiveChat(String playerName, String message) throws Exception;
    public void postSecretToLiveChat(String sender, String receiver, String message) throws Exception;
    public Chat updateLiveChat() throws RemoteException;
    void quitGame(String player, LobbyRemoteInterface stub) throws RemoteException;
    boolean matchHasStarted() throws RemoteException;
    void postMove(String player,Move move) throws RemoteException;
    boolean startGame(String player) throws RemoteException;
    boolean isLobbyAdmin(String player) throws RemoteException;
    int getID() throws RemoteException;
}
