package it.polimi.ingsw.shared.virtualview;

import it.polimi.ingsw.shared.Client;
import it.polimi.ingsw.shared.Position;

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
            cl.pickedFromBoard(position);
        }
    }

    /**
     * This method is used when there is the need to
     * send the whole board to clients. It overrides
     * every client copy of the board.
     * @param board JSObject.toJsonString
     */
    public void refresh(String board) {
        for (Client cl : getClientList()) {
            cl.refreshBoard(board);
        }
    }
}
