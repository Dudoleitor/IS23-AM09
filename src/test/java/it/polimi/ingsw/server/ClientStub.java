package it.polimi.ingsw.server;

import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.ChatMessage;
import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Tile;
import org.json.simple.JSONObject;

import java.util.List;

public class ClientStub implements Client {
    private String name;
    private boolean updated;

    public ClientStub(String name) {
        this.name = name;
        this.updated = false;
    }

    @Override
    public String getPlayerName() {
        return name;
    }

    @Override
    public void pickedFromBoard(JSONObject position) {
        this.updated = true;
    }

    @Override
    public void refreshBoard(String board) {
        this.updated = true;
    }

    @Override
    public void putIntoShelf(String player, int column, Tile tile) {
        this.updated = true;
    }

    @Override
    public void refreshShelf(String player, String shelf) {
        this.updated = true;
    }

    @Override
    public void postChatMessage(String sender, String message) {

    }

    @Override
    public void refreshChat(Chat chat) {

    }

    /**
     * This method is used when the lobby is ready and the
     * admin started the game.
     *
     * @param players List of players, order is used to
     *                determine turns
     */
    @Override
    public void gameStarted(List<String> players) {

    }

    public boolean wasUpdated() {
        return updated;
    }
    public void reset() {
        this.updated = false;
    }
}
