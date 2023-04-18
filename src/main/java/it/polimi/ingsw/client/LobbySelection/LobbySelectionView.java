package it.polimi.ingsw.client.LobbySelection;

import java.util.List;
import java.util.Map;

public abstract class LobbySelectionView {
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
     * Greet the player when he joins. It's nice to do so :-)
     * @param playerName
     */
    public abstract void greet(String playerName);
}
