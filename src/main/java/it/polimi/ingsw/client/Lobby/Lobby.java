package it.polimi.ingsw.client.Lobby;

import it.polimi.ingsw.shared.Move;
import it.polimi.ingsw.shared.Chat;

/**
 * This class is used to interface the Client with the lobby handler in the server
 */
public abstract class Lobby {
    /**
     * Posts message to LiveChat
     * @param playerName
     * @param message
     */
    abstract void postToLiveChat(String playerName, String message) throws LobbyException;

    /**
     * Post to private chat
     * @param sender
     * @param receiver
     * @param message
     */
    abstract public void postSecretToLiveChat(String sender, String receiver, String message) throws LobbyException;

    /**
     * Get updated livechat
     * @return the updated chat
     */
    abstract public Chat updateLiveChat() throws LobbyException;

    /**
     * Disconnect the player
     * @param player
     */
    abstract public void quitGame(String player) throws LobbyException;

    /**
     * Ask the server if the match has started
     * @return true if started
     */
    abstract public boolean matchHasStarted() throws LobbyException;

    /**
     * Post Move to Server
     * @param player
     * @param move
     */
    abstract public void postMove(String player, Move move) throws LobbyException;

    /**
     * Start the game on Server
     * @param player
     * @return true if match has actually started (the player is the admin)
     */
    abstract public boolean startGame(String player) throws LobbyException;

    /**
     * Ask Server if player is admin. Admin can Change if old admin disconnects
     * @param player
     * @return true if admin
     */
    abstract public boolean isLobbyAdmin(String player) throws LobbyException;

    /**
     * Obtain lobby ID from server
     * @return lobby id
     */
    abstract public int getID() throws LobbyException;
}
