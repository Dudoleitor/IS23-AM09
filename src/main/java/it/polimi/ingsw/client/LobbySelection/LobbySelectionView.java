package it.polimi.ingsw.client.LobbySelection;

import java.util.List;
import java.util.Map;

public abstract class LobbySelectionView {
    public abstract String askUserName();
    public abstract LobbySelectionCommand askLobby();
    public abstract void showJoinedLobbies(List<Integer> previousSessions);
    public abstract void showLobbyList(Map availableLobbies);
    public abstract boolean playAgain();
    public abstract void errorMessage(String message);
    public abstract void message(String message);
}
