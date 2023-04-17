package it.polimi.ingsw.client.Lobby;

import it.polimi.ingsw.client.Command;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Move;

import java.util.Map;

public abstract class MatchView {
    protected abstract Command askCommand();
    protected abstract void notifyExit();
    public abstract void printAllMessages(Chat chat);
    public abstract void greet(String playerName);
    public abstract Map<String,String> getMessageFromUser();
    public abstract Map<String,String> getPrivateMessageFromUser();
    protected abstract Move getMoveFromUser();

    protected abstract void showElement();

    protected abstract void notifyInvalidCommand();
}
