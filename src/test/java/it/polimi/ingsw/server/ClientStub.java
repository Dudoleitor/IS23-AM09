package it.polimi.ingsw.server;

import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.model.Tile;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

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
    public void refreshBoard(JSONObject board) {
        this.updated = true;
    }

    @Override
    public void putIntoShelf(String player, int column, Tile tile) {
        this.updated = true;
    }

    @Override
    public void refreshShelf(String player, JSONObject shelf) {
        this.updated = true;
    }

    @Override
    public void postChatMessage(String sender, String message) {

    }

    @Override
    public void refreshChat(Chat chat) {

    }


    @Override
    public void gameStarted(boolean loadedFromSave) {

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

    /**
     * This function is used to set the ExceptionHandler the client
     * will notify when a network exception happens.
     *
     * @param e Exception handler
     */
    @Override
    public void setExceptionHandler(NetworkExceptionHandler e) {

    }

    /**
     * This function is used to close the connection with the client
     */
    @Override
    public void disconnect() {

    }

    @Override
    public void endGame(Map<String, Integer> leaderBoard) {

    }

    

    /**
     * This function is used to ensure the client is still connected.
     * Expected return value is "pong".
     */
    @Override
    public String ping() {
        return "pong";
    }

    public boolean wasUpdated() {
        return updated;
    }
    public void reset() {
        this.updated = false;
    }
}
