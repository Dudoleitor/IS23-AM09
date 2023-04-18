package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.shared.*;
import it.polimi.ingsw.shared.RemoteInterfaces.ClientRemote;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This object is used on the client to receive updates
 * sent by the model. This object will be wrapped inside
 * a ClientRMI object.
 * The server, inside ClientRMI, will invoke methods on
 * this object and the remote execution will be
 * handled by RMI.
 */
public class ClientControllerCLI extends UnicastRemoteObject implements ClientRemote, ClientController {

    private final String playerName;
    private Chat chat;
    private Board board;
    private final Map<String, Shelf> playersShelves;

    public ClientControllerCLI(String playerName) throws RemoteException {
        super();
        this.playerName = playerName;
        this.chat = new Chat();
        this.board = null;
        this.playersShelves = new HashMap<>();
    }

    /**
     * This method is used to return the name of
     * the players using this client.
     *
     * @return String, player's name.
     */
    @Override
    public String getPlayerName() throws RemoteException {
        return playerName;
    }

    /**
     * This method is used (by match) to get the board and
     * pass it to the view.
     * @return Board object, by copy
     */
    public Board getBoard() {
        if (board==null) {
            throw new RuntimeException("The client contains an invalid board: null");
        }
        try {
            return new Board(this.board);
        } catch (JsonBadParsingException e) {
            throw new RuntimeException("The client contains an invalid board: " + e.getMessage());
        }
    }

    /**
     * This method is used (by match) to get the map containing
     * players and their shelf.
     * @return Map (by copy): String is playerName, Shelf is a Shelf object
     */
    public Map<String, Shelf> getPlayersShelves() {
        return new HashMap<>(playersShelves);
    }

    /**
     * This method is used (by match) to get the chat and print it.
     * @return Chat object, by copy
     */
    public Chat getChat() {
        return new Chat(this.chat);
    }

    /**
     * This method is used when a player picks a tile
     * from the board. It sends the message
     * to the remote view to remove the tile
     * from the board.
     *
     * @param position position
     */
    @Override
    public void pickedFromBoard(Position position) throws RemoteException {
        try {
            board.pickTile(position);
        } catch (BadPositionException e) {
            throw new RuntimeException("Received invalid position from server: " + e.getMessage());
        }
    }

    /**
     * This method is used to transfer the whole board
     * to the remote view,
     * it uses a json string.
     *
     * @param board JSONObject.toJsonString
     */
    @Override
    public void refreshBoard(String board) throws RemoteException {
        try {
            JSONObject jsonBoard = (JSONObject) (new JSONParser()).parse(board);
            this.board = new Board(jsonBoard, new ArrayList<>());
        } catch (ParseException | ClassCastException | JsonBadParsingException e) {
            throw new RuntimeException("Received invalid position from server: " + e.getMessage());
        }
    }

    /**
     * This method is used when a player inserts a single
     * tile into his shelf.
     * It is used to send the message to the remote view
     * of the client in order to insert the tile
     * into the shelf.
     *
     * @param player String name of the player that moved the tile
     * @param column destination column of the shelf
     * @param tile   Tile to insert
     */
    @Override
    public void putIntoShelf(String player, int column, Tile tile) throws RemoteException {
        try{
            Shelf shelf = playersShelves.get(player);
            shelf.insertTile(tile, column);
            playersShelves.replace(player, shelf);
        } catch (BadPositionException e) {
            throw new RuntimeException("Received invalid position from server: " + e.getMessage());
        }
    }

    /**
     * This method is used to transfer the whole shelf
     * of a player to the remote view of the client,
     * it uses a json string.
     *
     * @param player name of the player
     * @param shelf  JSONObject.toJsonString
     */
    @Override
    public void refreshShelf(String player, String shelf) throws RemoteException {
        try {
            JSONObject jsonShelf = (JSONObject) (new JSONParser()).parse(shelf);
            this.playersShelves.replace(player, new Shelf(jsonShelf));
        } catch (ParseException | ClassCastException | JsonBadParsingException e) {
            throw new RuntimeException("Received invalid shelf from server: " + e.getMessage());
        }
    }

    /**
     * This method is used to send a chat message to clients.
     * @param sender Player's name
     * @param message String message
     */
    public void postChatMessage(String sender, String message) throws RemoteException {
        chat.addMessage(sender, message);
    }

    /**
     * This method is used to send the whole chat to the client,
     * it is used when a refresh is needed.
     * @param chat Chat object
     */
    public void refreshChat(Chat chat) throws RemoteException {
        this.chat = chat;
    }
}
