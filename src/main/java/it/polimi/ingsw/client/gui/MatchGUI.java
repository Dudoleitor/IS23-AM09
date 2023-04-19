package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Lobby.LobbyCommand;
import it.polimi.ingsw.client.Lobby.MatchView;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Move;

import java.util.Map;

public class MatchGUI extends MatchView {
    @Override
    public LobbyCommand askCommand() {
        return null;
    }

    @Override
    public void notifyExit() {

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
    public Move getMoveFromUser() {
        return null;
    }

    @Override
    public void showElement() {

    }

    @Override
    public void showHelp() {

    }

    @Override
    public void notifyInvalidCommand() {

    }
}
