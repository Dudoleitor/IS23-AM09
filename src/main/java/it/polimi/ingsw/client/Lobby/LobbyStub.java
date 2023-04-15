package it.polimi.ingsw.client.Lobby;

import it.polimi.ingsw.shared.Move;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyRemoteInterface;


public interface LobbyStub {
    void postToLiveChat(String playerName, String message);
    public void postSecretToLiveChat(String sender, String receiver, String message);
    public Chat updateLiveChat();
    void quitGame(String player, LobbyRemoteInterface stub);
    boolean matchHasStarted();
    void postMove(String player, Move move);
    boolean startGame(String player);
    boolean isLobbyAdmin(String player);
    int getID();
}
