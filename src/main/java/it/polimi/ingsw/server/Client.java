package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Tile;
import org.json.simple.JSONObject;

public interface Client {

    /**
     * This method is used when a player picks a tile
     * from the board. It sends the message
     * to the remote view to remove the tile
     * from the board.
     * @param position position
     */
    public void pickedFromBoard(Position position);

    /**
     * This method is used to transfer the whole board
     * to the remote view,
     * it uses a json string.
     * @param board JSONObject.toJsonString
     */
    public void refreshBoard(String board);

    /**
     * This method is used when a player inserts a single
     * tile into his shelf. It sends the message
     * to the remote view to remove insert the tile
     * into the shelf.
     * @param player String name of the player
     * @param column int
     * @param tile Tile to insert
     */
    public void putIntoShelf(String player, int column, Tile tile);

    /**
     * This method is used to transfer the whole shelf
     * of a player to the remote view,
     * it uses a json string.
     * @param player name of the player
     * @param shelf JSONObject.toJsonString
     */
    public void refreshShelf(String player, String shelf);
}
