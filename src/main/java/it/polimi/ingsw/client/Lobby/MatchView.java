package it.polimi.ingsw.client.Lobby;

import it.polimi.ingsw.client.Command;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Move;

import java.util.Map;

public abstract class MatchView {
    /**
     * Ask the user for a command
     * @return the selected Command
     */
    protected abstract Command askCommand();
    /**
     * Notify the user that they entered an ivalid command
     */
    protected abstract void notifyInvalidCommand();

    /**
     * Notify the player that he quit
     */
    protected abstract void notifyExit();

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
    protected abstract Move getMoveFromUser();

    /**
     * Show the user an object from the model
     */
    protected abstract void showElement();

    /**
     * Show the list of commands that the client can execute
     */
    protected abstract void showHelp();

}
