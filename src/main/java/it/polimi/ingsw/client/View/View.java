package it.polimi.ingsw.client.View;

import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.model.Board;
import it.polimi.ingsw.shared.model.Move;
import it.polimi.ingsw.shared.model.Shelf;

import java.util.Map;

public abstract class View {

    /**
     * Ask the user for a command
     * @return the selected LobbyCommand
     */
    public abstract LobbyCommand askCommand();
    /**
     * Notify the user that they entered an ivalid command
     */
    public abstract void notifyInvalidCommand();

    /**
     * Notify the player that he quit
     */
    public abstract void notifyExit();

    /**
     * Show the user all chat messages
     * @param chat the Chat to print
     */
    public abstract void showAllMessages(Chat chat);

    /**
     * Ask the player to input a message
     * @return the Map from message fields to fields String values
     */
    public abstract Map<String,String> getMessageFromUser();

    /**
     * Ask the player to input a private message
     * @return the Map from private message fields to fields String values
     */
    public abstract Map<String,String> getPrivateMessageFromUser();

    /**
     * Ask the user for a move
     * @return the chosen move
     */
    public abstract Move getMoveFromUser(Board board, Shelf shelf);

    /**
     * Show the list of commands that the client can execute
     */
    public abstract void showHelp();
    /**
     * Ask the player for his username
     * @return the username
     */
    public abstract String askUserName();

    /**
     * Ask the player what lobby they want to join
     * @return a LobbySelection LobbyCommand corresponding to the user request
     */
    public abstract LobbySelectionCommand askLobby();

    /**
     * Show the user the lobbies passed as parameter
     * @param lobbies the list of lobbies
     */
    public abstract void showLobbies(Map<Integer,Integer> lobbies, String description);

    /**
     * Ask the player if they want to play again
     * @return
     */

    public abstract boolean playAgain();

    /**
     * Show the user an error message
     * @param message
     */
    public abstract void errorMessage(String message);

    /**
     * Show a message to user
     * @param message
     */
    public abstract void message(String message);

    /**
     * Set isLobbyAdmin variable in View
     */
    public abstract void setLobbyAdmin(boolean isAdmin);

    /**
     * Print the end game leaderboard
     */
    public abstract void endGame(Map<String, Integer> leaderBoard, String playername, Map<String, Shelf> playerShelves, Board board);
}
