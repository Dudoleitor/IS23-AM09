package it.polimi.ingsw.server;

import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.model.Tile;
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

    /**
     * This function is used when the turn of a player ends.
     *
     * @param player Name of the player that will play next.
     */
    @Override
    public void updateTurn(String player) {
       this.updated = true;
    }

    /**
     * This method is used at the beginning of the game to let
     * the client know its personal goal
     *
     * @param id Int ID of the goal
     */
    @Override
    public void setPlayerGoal(int id) {
       this.updated=true;
    }

    /**
     * This method is used when a player achieves
     * a common goal and pops points from
     * its stack.
     * It is also used to init the common goal.
     *
     * @param id     ID of the common goal
     * @param points Copy of the stack with points that
     *               can still be achieved
     */
    @Override
    public void refreshCommonGoal(int id, List<Integer> points) {
        this.updated = true;
    }

    public boolean wasUpdated() {
        return updated;
    }
    public void reset() {
        this.updated = false;
    }
}
