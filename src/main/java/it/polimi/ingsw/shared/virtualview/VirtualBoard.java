package it.polimi.ingsw.shared.virtualview;

import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.model.Position;
import org.json.simple.JSONObject;

/**
 * This object is used on the server to send
 * updates to clients when something changes
 * in the model.
 */
public class VirtualBoard extends VirtualView{

    public VirtualBoard() {
        super();
    }

    /**
     * This method is used when a player picks a tile
     * from the board. It updates every client copy of
     * the board by removing the tile from the board.
     * @param position position object
     */
    public void onPickTile(Position position) {
        for (Client cl : getClientList()) {
            cl.pickedFromBoard(position.toJson());
        }
    }

    /**
     * This method is used when there is the need to
     * send the whole board to clients. It overrides
     * every client copy of the board.
     * @param board JSObject
     */
    public void refresh(JSONObject board) {
        for (Client cl : getClientList()) {
            cl.refreshBoard(board);
        }
    }
}
