package it.polimi.ingsw.client.Lobby;

import it.polimi.ingsw.shared.Move;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyRemoteInterface;


public abstract class LobbyStub {
    abstract void postToLiveChat(String playerName, String message);
    abstract public void postSecretToLiveChat(String sender, String receiver, String message);
    abstract public Chat updateLiveChat();
    abstract public void quitGame(String player, LobbyRemoteInterface stub);
    abstract public boolean matchHasStarted();
    abstract public void postMove(String player, Move move);
    abstract public boolean startGame(String player);
    abstract public boolean isLobbyAdmin(String player);
    abstract public int getID();
}
