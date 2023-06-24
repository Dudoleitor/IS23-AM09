package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.connection.LobbyException;
import it.polimi.ingsw.client.connection.Server;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.cli.CLI_IO;
import it.polimi.ingsw.shared.*;
import it.polimi.ingsw.shared.RemoteInterfaces.ClientRemote;
import it.polimi.ingsw.shared.model.*;
import org.json.simple.JSONObject;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * For the general behaviour please refer to the javadoc of ClientController.
 * This object contains a copy of the model and uses the CLI
 * to print the entire model each time.
 */
public class ClientModelCLI extends UnicastRemoteObject implements ClientModel, ClientRemote {

    private final String playerName;
    private Chat chat;
    private Board board;
    private final Map<String, Shelf> playersShelves;
    private String firstPlayer;
    private PlayerGoal playerGoal;
    private final List<CommonGoal> commonGoalList;
    private final CLI_IO cliIO;
    private boolean gameStarted;
    private boolean itsMyTurn;
    private final LinkedList<Runnable> actionsQueue;
    private boolean gameEnded;
    private Thread pingListener;
    private final Object pingLock;
    private Server server;  // Needed to terminate the connection

    public ClientModelCLI(String playerName, CLI_IO cliIO) throws RemoteException {
        super();
        this.playerName = playerName;
        this.chat = new Chat();
        this.board = null;
        this.playersShelves = new HashMap<>();
        this.firstPlayer = "";
        this.playerGoal = null;
        this.commonGoalList = new ArrayList<>();
        this.cliIO = cliIO;
        this.gameStarted = false;
        this.itsMyTurn = false;
        this.actionsQueue = new LinkedList<>();
        this.gameEnded = false;

        this.pingLock = new Object();
        this.pingListener = ClientController.getThread(pingLock);
    }

    /**
     * This method is used to set the pointer to the server interface.
     * @param server Server object
     */
    public void setServer(Server server) {
        this.server = server;
    }

    /**
     * This method is used to return the name of
     * the players using this client.
     *
     * @return String, player's name.
     */
    @Override
    public String getPlayerName() {
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
    public void pickedFromBoard(JSONObject position) {
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
     *
     * @param board JSONObject
     */
    @Override
    public void refreshBoard(JSONObject board) {
        try {
            this.board = new Board(board, new ArrayList<>());
        } catch (JsonBadParsingException e) {
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
    public void putIntoShelf(String player, int column, Tile tile) {
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
     * @param shelf  JSONObject
     */
    @Override
    public void refreshShelf(String player, JSONObject shelf) {
        if (firstPlayer.equals("")) {  // The first player of whom we receive the shelf is the first player
            firstPlayer = player;
            cliIO.setFirstPlayer(firstPlayer.equals(playerName));
        }
        playersShelves.remove(player);
        try {
            this.playersShelves.put(player, new Shelf(shelf));
        } catch (JsonBadParsingException e) {
            throw new RuntimeException("Received invalid shelf from server: " + e.getMessage());
        }
    }

    /**
     * This method is used to send a chat message to clients.
     * @param sender Player's name
     * @param message String message
     */
    public void postChatMessage(String sender, String message) {
        chat.addMessage(sender, message);
        if (!Objects.equals(sender, playerName))
            enqueueTask(() -> cliIO.showChatMessage(chat.getLast()));
        executeTasks();
    }

    /**
     * This method is used to send the whole chat to the client,
     * it is used when a refresh is needed.
     * @param chat Chat object
     */
    public void refreshChat(Chat chat) {
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
     * @param newMatch true if the game is new,
     *        false if it was loaded from a save or the player
     *        reconnected.
     */
    @Override
    public void gameStarted(boolean newMatch) {
        ensureModelIsSet();

        enqueueTask(() -> cliIO.showGameStatus(board,commonGoalList,playersShelves,playerGoal));
        gameStarted = true;

        final String message = newMatch ? "New match started" :
                "Match started, loaded from previous save or you reconnected";

        enqueueTask(() -> cliIO.message(message));

        executeTasks();
    }

    /**
     * This method is used to observe if the game has started.
     * @return Boolean, true if the game has started.
     */
    @Override
    public boolean gameIsStarted(){
        return gameStarted;
    }

    /**
     * This function is used when the turn of a player ends.
     * It is also used at beginning of the match, before
     * the model is completely set.
     *
     * @param player Name of the player that will play next.
     */
    @Override
    public void nextTurn(String player) {
        if (gameStarted) {
            ensureModelIsSet();
            enqueueTask(() -> cliIO.showGameStatus(board,commonGoalList,playersShelves,playerGoal));
        }

        if (player.equals(this.playerName)) {
            enqueueTask(() -> cliIO.message("It's your turn"));
           itsMyTurn=true;
        } else {
            enqueueTask(() -> cliIO.message("It's "+player+" turn"));
            itsMyTurn=false;
        }
        executeTasks();
    }

    /**
     * @return True if the player need to play in the current turn
     */
    @Override
    public boolean isItMyTurn(){
        return itsMyTurn;
    }

    /**
     * This method is used to find out if the client is the first player
     * @return true if the client is the first player
     */
    @Override
    public boolean amIFirstPlayer() {
        return firstPlayer.equals(playerName);
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
    public void refreshCommonGoal(int id, List<Integer> points) {
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
    public void setPlayerGoal(int id) {
        try {
            this.playerGoal = new PlayerGoal(JSONFilePath.PlayerGoals, id);
        } catch (JsonBadParsingException e) {
            throw new RuntimeException("JsonBadParsing exception while loading player goal: " + e.getMessage());
        }
    }

    /**
     * This method is used at the end of the game to
     * send the leaderboard to the client.
     * @param leaderBoard Map: player's name - points
     */
    public void endGame(Map<String, Integer> leaderBoard){
        cliIO.endGame(leaderBoard, playerName,playersShelves,board);
        this.gameEnded = true;

        if(this.server!=null)
            try {
                server.quitGame(playerName);
            } catch (LobbyException ignored) {
            }
        System.exit(0);
    }

    @Override
    public boolean gameEnded() throws RemoteException {
        return gameEnded;
    }

    /**
     * This function is used to ensure the client is still connected.
     * Expected return value is "pong".
     */
    @Override
    public String ping() {
        if(pingListener.getState().equals(Thread.State.NEW)) {
            pingListener.start();
        }
        synchronized (pingLock) {
            pingLock.notifyAll();
        }
        return "pong";
    }

    /**
     * Add a runnable to a FIFO queue of tasks to perform
     * This is used not to lock the server on client syncronized events
     * @param task
     */
    private void enqueueTask(Runnable task){
        actionsQueue.addLast(task);
    }

    /**
     * Execute all tasks on a new thread
     */
    private void executeTasks(){
        new Thread(
                () -> {
                    while(!actionsQueue.isEmpty()){
                        actionsQueue.pollFirst().run();
                    }
                }
        ).start();
    }
}