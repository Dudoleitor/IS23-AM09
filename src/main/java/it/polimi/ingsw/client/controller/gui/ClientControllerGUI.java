package it.polimi.ingsw.client.controller.gui;

import it.polimi.ingsw.client.controller.cli.LobbyCommand;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.cli.LobbySelectionCommand;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.model.Board;
import it.polimi.ingsw.shared.model.Move;
import it.polimi.ingsw.shared.model.Shelf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientControllerGUI extends ClientController {
    @Override
    public LobbyCommand askCommand() {
        return null;
    }

    @Override
    public void notifyExit() {

    }

    @Override
    public void showAllMessages(Chat chat) {
        List<String> messages = new ArrayList<>();
        messages.addAll(chat.getAllMessages().stream().map(mes -> mes.toString()).collect(Collectors.toList()));
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
    public Move getMoveFromUser(Board board, Shelf shelf) {
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
    public void endGame(Map<String, Integer> leaderBoard, String playername, Map<String, Shelf> playerShelves, Board board) {

    }

    @Override
    public void notifyInvalidCommand() {

    }
}
