package it.polimi.ingsw.server.clientonserver;

import it.polimi.ingsw.server.NetworkExceptionHandler;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.PlayerWithPoints;
import it.polimi.ingsw.shared.model.Tile;
import org.json.simple.JSONObject;

import java.util.List;

/**
 * This object is used to send updates to
 * a specific client, it resides on the server.
 * ClientRMI and ClientTCP implement methods properly.
 */
public interface Client {

    /**
     * This method is used to return the name of
     * the players using this client.
     * @return String, player's name.
     */
    public String getPlayerName();

    /**
     * This function is used to set the ExceptionHandler the client
     * will notify when a network exception happens.
     * @param e Exception handler
     */
    public void setExceptionHandler(NetworkExceptionHandler e);

    /**
     * This method is used when a player picks a tile
     * from the board. It sends the message
     * to the remote view to remove the tile
     * from the board.
     *
     * @param position position
     */
    public void pickedFromBoard(JSONObject position);

    /**
     * This method is used to transfer the whole board
     * to the remote view,
     * it uses a json string.
     *
     * @param board JSONObject
     */
    public void refreshBoard(JSONObject board);

    /**
     * This method is used when a player inserts a single
     * tile into his shelf.
     * It is used to send the message to the remote view
     * of the client in order to insert the tile
     * into the shelf.
     *
     * @param player String name of the player that moved the tile
     * @param column destination column of the shelf
     * @param tile Tile to insert
     */
    public void putIntoShelf(String player, int column, Tile tile);

    /**
     * This method is used to transfer the whole shelf
     * of a player to the remote view of the client,
     * it uses a json string.
     *
     * @param player name of the player
     * @param shelf JSONObject
     */
    public void refreshShelf(String player, JSONObject shelf);

    /**
     * This method is used to send a chat message to clients.
     * @param sender Player's name
     * @param message String message
     */
    public void postChatMessage(String sender, String message);

    /**
     * This method is used to send the whole chat to the client,
     * it is used when a refresh is needed.
     * @param chat Chat object
     */
    public void refreshChat(Chat chat);

    /**
     * This method is used when the lobby is ready and the
     * admin started the game.
     * @param newMatch true if the game is new,
     *        false if it was loaded from a save or the player
     *        reconnected.
     */
    public void gameStarted(boolean newMatch);

    /**
     * This function is used when the turn of a player ends.
     * @param player Name of the player that will play next.
     */
    public void updateTurn(String player);

    /**
     * This method is used when a player achieves
     * a common goal and pops points from
     * its stack.
     * It is also used to init the common goal.
     * @param id ID of the common goal
     * @param points Copy of the stack with points that
     *               can still be achieved
     */
    public void refreshCommonGoal(int id, List<Integer> points);

    /**
     * This method is used at the beginning of the game to let
     * the client know its personal goal
     * @param id Int ID of the goal
     */
    public void setPlayerGoal(int id);

    /**
     * This method is used at the end of the game to
     * send the leaderboard to the client.
     * @param leaderBoard Map: player's name - points
     */
    public void endGame(List<PlayerWithPoints> leaderBoard);

    /**
     * This function is used to close the connection with the client
     */
    public void disconnect();

    /**
     * This function is used to ensure the client is still connected.
     * Expected return value is "pong".
     */
    public String ping();
}
