package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.View.cli.CLI;
import it.polimi.ingsw.shared.*;
import it.polimi.ingsw.shared.RemoteInterfaces.ClientRemote;
import it.polimi.ingsw.shared.model.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * For the general behaviour please refer to the javadoc of ClientController.
 * This object contains a copy of the model and uses the CLI
 * to print the entire model each time.
 */
public class ClientControllerCLI extends UnicastRemoteObject implements ClientController, ClientRemote {

    private final String playerName;
    private Chat chat;
    private Board board;
    private final Map<String, Shelf> playersShelves;
    private PlayerGoal playerGoal;
    private final List<CommonGoal> commonGoalList;
    private final List<String> players;
    private final CLI cli;
    private boolean gameStarted;
    private boolean itsMyTurn;

    public ClientControllerCLI(String playerName, CLI cli) throws RemoteException {
        super();
        this.playerName = playerName;
        this.chat = new Chat();
        this.board = null;
        this.playersShelves = new HashMap<>();
        this.playerGoal = null;
        this.commonGoalList = new ArrayList<>();
        this.players = new ArrayList<>();
        this.cli = cli;
        this.gameStarted = false;
        this.itsMyTurn = false;
    }

    /**
     * This method is used to return the name of
     * the players using this client.
     *
     * @return String, player's name.
     * @throws RemoteException never, needed by RMI
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
     * @throws RemoteException never, needed by RMI
     *
     * @param position position
     */
    @Override
    public void pickedFromBoard(JSONObject position) throws RemoteException {
        try {
            board.pickTile(new Position(position));
        } catch (BadPositionException e) {
            throw new RuntimeException("Received invalid position from server: " + e.getMessage());
        }
    }

    /**
     * This method is used to transfer the whole board
     * to the remote view,
     * it uses a json string.
     * @throws RemoteException never, needed by RMI
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
     * @throws RemoteException never, needed by RMI
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
     * @throws RemoteException never, needed by RMI
     */
    @Override
    public void refreshShelf(String player, String shelf) throws RemoteException {
        playersShelves.remove(player);
        try {
            JSONObject jsonShelf = (JSONObject) (new JSONParser()).parse(shelf);
            this.playersShelves.put(player, new Shelf(jsonShelf));
        } catch (ParseException | ClassCastException | JsonBadParsingException e) {
            throw new RuntimeException("Received invalid shelf from server: " + e.getMessage());
        }
    }

    /**
     * This method is used to send a chat message to clients.
     * @param sender Player's name
     * @param message String message
     * @throws RemoteException never, needed by RMI
     */
    public void postChatMessage(String sender, String message) throws RemoteException {
        chat.addMessage(sender, message);
        if (!Objects.equals(sender, playerName))
            cli.showChatMessage(sender, message);
    }

    /**
     * This method is used to send the whole chat to the client,
     * it is used when a refresh is needed.
     * @param chat Chat object
     * @throws RemoteException never, needed by RMI
     */
    public void refreshChat(Chat chat) throws RemoteException {
        this.chat = chat;
    }

    /**
     * This function throws a RuntimeException if any element
     * of the model is not set properly.
     */
    private void ensureModelIsSet() {
        if (board==null) throw new RuntimeException("Board null after the game has started");
        if(commonGoalList.isEmpty()) throw new RuntimeException("Common goals list empty after game has started");
        if(playersShelves.isEmpty()) throw new RuntimeException("Player shelves list empty after game has started");
        if(playerGoal==null) throw new RuntimeException("Personal goal null after game has started");
    }

    /**
     * This method is used when the lobby is ready and the
     * admin started the game.
     * @throws RemoteException never, needed by RMI
     */
    @Override
    public void gameStarted(List<String> players) throws RemoteException {
        ensureModelIsSet();
        cli.showBoard(board);
        cli.showCommonGoals(commonGoalList);
        cli.showShelves(playersShelves);
        cli.showPersonalGoal(playerGoal);

        gameStarted = true;
        cli.message("Match has started");
    }

    /**
     * This function is used when the turn of a player ends.
     * It is also used at beginning of the match, before
     * the model is completely set.
     *
     * @param player Name of the player that will play next.
     * @throws RemoteException never, needed by RMI
     */
    @Override
    public void nextTurn(String player) throws RemoteException {
        if (gameStarted) {
            ensureModelIsSet();
            cli.showBoard(board);
            cli.showCommonGoals(commonGoalList);
            cli.showShelves(playersShelves);
            cli.showPersonalGoal(playerGoal);
        }

        if (player.equals(this.playerName)) {
           cli.message("It's your turn");
           itsMyTurn=true;
        } else {
            itsMyTurn=false;
        }
    }

    /**
     * @return True if the player need to play in the current turn
     * @throws RemoteException never, needed by RMI
     */
    @Override
    public boolean isItMyTurn() throws RemoteException{
        return itsMyTurn;
    }

    /**
     * This method is used when a player achieves
     * a common goal and pops points from
     * its stack.
     * It is also used to init the common goal.
     *
     * @param id     ID of the common goal
     * @param points Copy of the stack with points that
     *               can still be achieved
     */
    @Override
    public void refreshCommonGoal(int id, List<Integer> points) throws RemoteException {
        final CommonGoal commonGoal = new CommonGoal(CommonGoalStrategy.findById(id), points);
        commonGoalList.remove(commonGoal);
        commonGoalList.add(commonGoal);
        commonGoalList.sort((x,y) -> x.getID() > y.getID() ? 1 : -1);
    }

    /**
     * This method is used at the beginning of the game to let
     * the client know its personal goal
     *
     * @param id Int ID of the goal
     */
    @Override
    public void setPlayerGoal(int id) throws RemoteException {
        try {
            this.playerGoal = new PlayerGoal(JSONFilePath.PlayerGoals, id);
        } catch (JsonBadParsingException e) {
            throw new RuntimeException("JsonBadParsing exception while loading player goal: " + e.getMessage());
        }
    }
}