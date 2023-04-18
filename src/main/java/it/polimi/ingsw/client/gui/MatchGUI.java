package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Lobby.LobbyCommand;
import it.polimi.ingsw.client.Lobby.MatchView;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Move;

import java.util.Map;

public class MatchGUI extends MatchView {
    @Override
    protected LobbyCommand askCommand() {
        return null;
    }

    @Override
    protected void notifyExit() {

    }

    @Override
    public void showAllMessages(Chat chat) {

    }

    @Override
    public Map<String, String> getMessageFromUser() {
        return null;
    }

    @Override
    public Map<String, String> getPrivateMessageFromUser() {
        return null;
    }

    @Override
    protected Move getMoveFromUser() {
        return null;
    }

    @Override
    protected void showElement() {

    }

    @Override
    protected void showHelp() {

    }

    @Override
    protected void notifyInvalidCommand() {

    }
}
