package it.polimi.ingsw.client.View.gui;

import it.polimi.ingsw.client.View.LobbyCommand;
import it.polimi.ingsw.client.View.View;
import it.polimi.ingsw.client.View.LobbySelectionCommand;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.model.Move;

import java.util.Map;

public class GUI extends View {
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
    public void showHelp() {

    }

    @Override
    public String askUserName() {
        return null;
    }

    @Override
    public LobbySelectionCommand askLobby() {
        return null;
    }

    @Override
    public void showLobbies(Map<Integer, Integer> lobbies, String description) {

    }

    @Override
    public boolean playAgain() {
        return false;
    }

    @Override
    public void errorMessage(String message) {

    }

    @Override
    public void message(String message) {

    }

    @Override
    public void setLobbyAdmin(boolean isAdmin) {

    }

    @Override
    public void notifyInvalidCommand() {

    }
}
