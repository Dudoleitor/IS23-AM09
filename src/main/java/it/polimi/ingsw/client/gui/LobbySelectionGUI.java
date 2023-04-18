package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.LobbySelection.LobbySelectionCommand;
import it.polimi.ingsw.client.LobbySelection.LobbySelectionView;

import java.util.List;
import java.util.Map;

public class LobbySelectionGUI extends LobbySelectionView {

    @Override
    public String askUserName() {
        return null;
    }

    @Override
    public LobbySelectionCommand askLobby() {
        return null;
    }

    @Override
    public void showLobbies(Map availableLobbies, String description) {

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
    public void greet(String playerName) {

    }
}
