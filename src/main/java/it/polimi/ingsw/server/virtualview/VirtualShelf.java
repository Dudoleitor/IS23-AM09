package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.shared.Client;
import it.polimi.ingsw.shared.Tile;

/**
 * This object is used on the server to send
 * updates to clients when something changes
 * in a particualar shelf.
 * This VirtualShelf is linked to the shelf
 * of a player, calls to methods in this object
 * come only from that particular shelf in the model.
 */
public class VirtualShelf extends VirtualView {
    private final String player;
    public VirtualShelf(String player) {
        super();
        this.player = player;
    }

    public String getPlayer() { return player; }

    /**
     * This method is used to update the remote
     * copy of the shelf on every client.
     * Each client has a remote view of
     * the shelf belonging to this.player.
     * @param column destination column on the shelf
     * @param tile Tile to insert
     */
    public void onTileInsert(int column, Tile tile) {
        for (Client cl : getClientList()) {
            cl.putIntoShelf(this.player, column, tile);
        }
    }
}
