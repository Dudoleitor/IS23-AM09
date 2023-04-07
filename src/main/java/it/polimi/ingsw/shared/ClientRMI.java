package it.polimi.ingsw.shared;

import java.rmi.Remote;
import java.util.Objects;

/**
 * This object is used to send updates to
 * a specific client.
 * This object will reside on the client,
 * remote calls will work thanks to RMI.
 */
public class ClientRMI implements Client, Remote {
    // TODO add board and shelves
    private final String playerName;

    public ClientRMI(String playerName) {
        this.playerName = playerName;
    }

    /**
     * This method is used to return the name of
     * the players using this client.
     * @return String, player's name.
     */
    public String getPlayerName() {return playerName;}

    /**
     * This method is used when a player picks a tile
     * from the board. It sends the message
     * to the remote view to remove the tile
     * from the board.
     *
     * @param position Position
     */
    @Override
    public void pickedFromBoard(Position position) {
        //TODO
    }

    /**
     * This method is used to transfer the whole board
     * to the remote view,
     * it uses a json string.
     * @param board JSONObject.toJsonString
     */
    @Override
    public void refreshBoard(String board) {
        //TODO
    }

    /**
     * This method is used when a player inserts a single
     * tile into his shelf.
     * It is used to send the message to the remote view
     * of the client in order to insert the tile
     * into the shelf.
     * @param player String name of the player that moved the tile
     * @param column destination column of the shelf
     * @param tile Tile to insert
     */
    @Override
    public void putIntoShelf(String player, int column, Tile tile) {
        //TODO
    }

    /**
     * This method is used to transfer the whole shelf
     * of a player to the remote view,
     * it uses a json string.
     * @param player name of the player
     * @param shelf  JSONObject.toJsonString
     */
    @Override
    public void refreshShelf(String player, String shelf) {
        //TODO
    }

    @Override
    public boolean equals(Object o) {  // Checking using LOWERCASE name
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientRMI clientRMI = (ClientRMI) o;
        return Objects.equals(playerName.toLowerCase(), clientRMI.playerName.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName.toLowerCase());
    }
}
