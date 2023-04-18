package it.polimi.ingsw.shared.RemoteInterfaces;

import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Move;

import java.rmi.Remote;

public interface ServerLobbyInterface extends Remote {
    void postToLiveChat(String playerName, String message) throws Exception;
    public void postSecretToLiveChat(String sender, String receiver, String message) throws Exception;
    void quitGame(String player) throws Exception;
    boolean matchHasStarted() throws Exception;
    void postMove(String player,Move move) throws Exception;
    boolean startGame(String player) throws Exception;
    boolean isLobbyAdmin(String player) throws Exception;
    int getID() throws Exception;
}
