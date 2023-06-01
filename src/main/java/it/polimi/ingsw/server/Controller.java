package it.polimi.ingsw.server;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.*;
import it.polimi.ingsw.shared.model.*;
import it.polimi.ingsw.shared.virtualview.VirtualView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static it.polimi.ingsw.shared.JSONFilePath.PlayerGoals;


public class Controller implements Jsonable {
    private final Board board;

    // This list MUST always have the same size
    // When a player disconnects it's marked as inactive
    private final List<Player> players;
    private final List<Client> clients;
    private final List<VirtualView> virtualViews;
    private int turn;

    /**
     * This method is used to randomly generate a list
     * of PlayerGoal objects.
     * @return List of PlayerGoal objects
     */
    private List<PlayerGoal> initPlayersGoals() throws JsonBadParsingException {
        List<PlayerGoal> playerGoals = new ArrayList<>();
        PlayerGoal playerGoal;

        // Generating players' goals
        if (PlayerGoal.playerGoalsAmount(PlayerGoals) < clients.size()) {
            throw new ControllerGenericException("Unable to generate players goals: not enough goals in file!");
        }
        for (int goalToGen = 0; goalToGen < clients.size(); goalToGen++) {
            playerGoal = new PlayerGoal(PlayerGoals);
            while (playerGoals.contains(playerGoal)) {
                playerGoal = new PlayerGoal(PlayerGoals);
            }
            playerGoals.add(playerGoal);
        }
        return playerGoals;
    }
    /**
     * This method is used to init the list of players,
     * it creates the player, assigns him a shelf and a goal,
     * update the list of virtual views.
     * @param playerGoals List of PlayerGoal objects
     */
    private List<Player> initPlayers(List<PlayerGoal> playerGoals) {
        List<Player> players = new ArrayList<>();
        PlayerGoal playerGoal;
        Player player;
        for (Client client : clients) {
            playerGoal = playerGoals.get(0);
            playerGoals.remove(playerGoal);

            player = new Player(client.getPlayerName(),
                    new Shelf(GameSettings.shelfRows, GameSettings.shelfColumns),
                    playerGoal);

            players.add(player);
            virtualViews.add(player.getVirtualShelf());
        }
        return players;
    }

    /**
     * This method is used to init the board.
     * @return Board object
     */
    private Board initBoard() throws JsonBadParsingException {
        final Board board;
        board = new Board(clients.size());
        try {
            board.fill();
        } catch (OutOfTilesException e) {
            throw new RuntimeException("A new board should never be out of tiles");
        }
        virtualViews.add(board.getVirtualBoard());
        virtualViews.addAll(board.getCommonGoals().stream().map(CommonGoal::getVirtualCommonGoal).collect(Collectors.toList()));
        return board;
    }
    /**
     * Constructor used to initialize the controller
     * @param clients is a List of Client objects
     */
    public Controller(List<Client> clients){
        if(clients == null){
            throw new ControllerGenericException("Error while creating the Controller: clients list was null");
        }

        this.virtualViews = new ArrayList<>();
        this.clients = clients;
        this.turn = ThreadLocalRandom.current().nextInt(0, clients.size());

        try {
            this.board = initBoard();
            this.players = initPlayers(initPlayersGoals());
        } catch (NullPointerException e) {
            throw new ControllerGenericException("Error while creating the Controller : Json file doesn't exists");
        } catch (ClassCastException | JsonBadParsingException e) {
            throw new ControllerGenericException("Error while creating the Controller : bad Json parsing");
        }

        for (VirtualView virtualView : virtualViews) virtualView.setClientList(clients);
        // Sending all data to clients
        for (Client client : clients) {
            refreshClient(client);
        }
    }

    private boolean gameFinished(){
        return getShelves()
                .values()
                .stream()
                .anyMatch(s -> s.isFull());
    }

    /**
     * This method is used to load a list of players
     * from a JSONArray.
     * @param playersJson JSONArray with the players
     * @return List of Player objects
     */
    private List<Player> loadPlayers(JSONArray playersJson) throws JsonBadParsingException {
        List<Player> players = new ArrayList<>();
        JSONObject jsonPlayer;
        Player player;
        for (Object objPlayer : playersJson) {
            jsonPlayer = (JSONObject) objPlayer;
            player = new Player(jsonPlayer);
            players.add(player);
            virtualViews.add(player.getVirtualShelf());

            if (nameAbsentInClients(player.getName(), clients)) {
                throw new JsonBadParsingException("No client with playername " + player.getName() + " provided");
            }
        }
        return players;
    }

    /**
     * This constructor is used to load the state of the game from a jsonfile.
     * @param gameStatus JSONObject with the status
     * @throws JsonBadParsingException when a parsing error happens
     */
    public Controller(JSONObject gameStatus, List<Client> clients) throws JsonBadParsingException {
        virtualViews = new ArrayList<>();
        try {
            this.clients = clients;
            JSONArray commonGoalsJson = (JSONArray) gameStatus.get("commonGoals");

            this.board = new Board((JSONObject) gameStatus.get("board"), new ArrayList<>(commonGoalsJson));

            virtualViews.add(board.getVirtualBoard());
            virtualViews.addAll(board.getCommonGoals().stream().map(CommonGoal::getVirtualCommonGoal).collect(Collectors.toList()));

            this.turn = Math.toIntExact((Long) gameStatus.get("turn"));

            // Loading players
            JSONArray playersJson = (JSONArray) gameStatus.get("players");
            if (playersJson==null) {throw new JsonBadParsingException("Error while loading game status from json: players field not found");}
            this.players = loadPlayers(playersJson);

        } catch (ClassCastException | JsonBadParsingException e) {
            throw new JsonBadParsingException("Error while loading game status from json: " + e.getMessage());
        }

        for (VirtualView virtualView : virtualViews) virtualView.setClientList(clients);
        // Sending all data to clients
        for (Client client : clients) {
            refreshClient(client);
        }
    }

    /**
     * This method is used when there is the need to completely
     * refresh a client.
     * It is used on game loading.
     * @param client Client object to update
     */
    public void refreshClient(Client client) {
        final Player player = players.stream()
                .filter(x ->
                        x.getName().equals(client.getPlayerName())
                )
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No player found with specified client name in refreshClient"));

        client.refreshBoard(this.board.toJson());

        for(CommonGoal commonGoal : this.board.getCommonGoals()) {
            client.refreshCommonGoal(commonGoal.getID(), commonGoal.showPointsStack());
        }

        for (Player pl : players) {
            client.refreshShelf(pl.getName(), pl.getShelf().toJson());
        }

        client.setPlayerGoal(player.getPersonalGoalId());
        client.updateTurn(getCurrentPlayerName());
    }

    /**
     * Checking if there is a client with the given player name.
     * @param name String, name to search for.
     * @param clients List of Client objects
     * @return True if the name is NOT present in the list.
     */
    private boolean nameAbsentInClients(String name, List<Client> clients) {
        return clients.stream().map(Client::getPlayerName).filter(x -> x.equals(name)).findAny().isEmpty();
    }

    /**
     * @return the board
     */
    public Board getBoard() throws JsonBadParsingException {
        return new Board(board);
    }

    /**
     * @return the commonGoals we instantiated in the constructor
     */
    public List<CommonGoal> getCommonGoals() {
        List<CommonGoal> commonGoals = new ArrayList<>();
        commonGoals.addAll(board.getCommonGoals());
        return commonGoals;
    }

    /**
     * @return the players of the match
     */
    public List<Player> getPlayers() {
        return players.stream().
                map(p-> {
                    try {
                        return new Player(p);
                    } catch (JsonBadParsingException e) {
                        throw new RuntimeException(e);
                    }
                }).
                collect(Collectors.toList());
    }

    protected void setActivity(String playerName, boolean value) throws ControllerGenericException{
        if(playerName == null){
            throw new ControllerGenericException("No player found with that name");
        }
        else{
            Optional<Player> target =  players.stream().
                    filter(p -> p.getName().equals(playerName)).
                    findFirst();
            if(!target.isPresent()){
                throw new ControllerGenericException("No player found with that name");
            }
            else{
                target.get().setActive(value);
            }
        }
    }

    /**
     * increments the turn, if the current player is not active goes on
     */
    public void nextTurn() {
        turn++;
        if(!players.get( getTurn() % players.size() ) .isActive()) {
            nextTurn();
        }
    }

    /**
     * @return the current player
     */
    public String getCurrentPlayerName() {

        return players.get(getTurn() % players.size()).getName();
    }

    /**
     * @return the current turn
     */
    public int getTurn(){
        return turn;
    }

    /**
     * @return a map that contains the shelf of each player
     */
    public Map<String, Shelf> getShelves() {
        Map<String,Shelf> shelves = new HashMap<>();
        for(Player p : getPlayers()) {
            shelves.put(p.getName(), p.getShelf());
        }
        return shelves;
    }

    /**
     * This function builds the name of the file used to save the game
     * @param players List of players' names
     * @return String, name of the file
     */
    public static String getFileName(List<String> players) {
        return "SavedGame-" + players.stream()
                .map(String::toLowerCase)
                .sorted()
                .reduce("", (prev, next) ->
                        ( prev.equals("") ? next : prev + "_" + next))
                + ".json";
    }

    /**
     * This function is used to save the status of the game on file
     */
    private void saveGame() {
        try (
                final Writer writer = new BufferedWriter(
                        new FileWriter(
                                "./" +  getFileName(
                                        players.stream()
                                                .map(Player::getName)
                                                .collect(Collectors.toList()))))
        ) {
            this.toJson().writeJSONString(writer);
        } catch (IOException e) {
            System.err.println("Error while saving game: " + e.getMessage());
        }
    }

    private void deleteSavedGame() {
        try {
            Files.deleteIfExists(Path.of(
                    "./" + getFileName(
                            players.stream()
                                    .map(Player::getName)
                                    .collect(Collectors.toList())))
            );
        }catch (IOException e) {
            System.err.println("Error while deleting saved game: " + e.getMessage());
        }
    }

    /**
     * This function is used to notify each
     * client the game has ended.
     */
    private void handleGameEnd() {
        Map<String, Integer> leaderBoard = new HashMap<>();
        for(Player p : players){
            int points = 0;

            System.out.println(p.getName());
            System.out.println("From commonGoals: "+p.getCommonGoalPoints());
            System.out.println("From adjacent tyles: "+p.getCommonGoalPoints());
            System.out.println("From personal goal: "+p.getPersonalGoalPoints());
            points += p.getPersonalGoalPoints() + p.getAdjacentPoints() + p.getCommonGoalPoints();
            if(p.getName().equals(getCurrentPlayerName())){
                points += GameSettings.bonusPointsForLastMove;
                System.out.println("From bonus: "+ GameSettings.bonusPointsForLastMove);
            }
            System.out.println("Total: "+points);
            leaderBoard.put(p.getName(),points);
        }

        for (Client client : clients)
            client.endGame(leaderBoard);
        deleteSavedGame();
    }

    /**
     * This method is used to load a Player object
     * given its name.
     * @param playerName String, name of the player
     * @return Player object
     */
    private Player getPlayerFromName(String playerName) throws ControllerGenericException {
        final Player player = players.stream().
                filter(p -> p.getName().equals(playerName)).
                findFirst().orElse(null);
        if (player == null) {
            throw new ControllerGenericException("No player found with that name");
        }
        return player;
    }

    /**
     * This method is the core functionality of moveTiles.
     * It checks if the move is valid and then updates the model.
     * @param player Player object
     * @param move Move object
     */
    private void doMoveTiles(Player player, Move move) throws InvalidMoveException, BadPositionException {
        final Shelf playerShelf = player.getShelf();
        final int freeSpaceInColumn = (int) playerShelf.
                allTilesInColumn(move.getColumn()).stream().
                filter(x -> x.equals(Tile.Empty)).
                count();
        if (move.getBoardPositions().size() > freeSpaceInColumn) { //if the size of the move coordinates is greater than empty cells in the self we throw an exception
            throw new InvalidMoveException("Number of tiles selected greater than empty fields in shelf");
        }
        if (!checkValidMove(move))
            throw new InvalidMoveException("Tiles selection is not allowed");

        final List<Position> positions = move.getBoardPositions();
        for (Position p : positions) { //for all the positions we insert the tile in the playerShelf
            player.insertTile(board.pickTile(p), move.getColumn());

        }
        player.checkCommonGoals(board.getCommonGoals());
    }

    /**
     * insert Tiles in the player shelf according to move coordinates
     * @param playerName is the current player
     * @param move is the move that player wants to do
     * @throws ControllerGenericException if the selected tile is not allowed, if the player is not the current player
     * or if the shelf hasn't enough empty cells
     */
    public void moveTiles(String playerName, Move move) throws ControllerGenericException {
        if (clients.size()<GameSettings.minSupportedPlayers) {
            for (Client c : clients) {
                c.postChatMessage("Server", "You must wait for other players to reconnect before playing");
            }
            return;
        }

        if(move == null || move.getBoardPositions() == null || move.getBoardPositions().contains(null)){
            throw new ControllerGenericException("Invalid move");
        }
        if(playerName == null){
            throw new ControllerGenericException("No player found with that name");
        }
        if (!playerName.equals(getCurrentPlayerName())) { //if player is not the current player we throw an exception
            throw new ControllerGenericException("Player is not the current player");
        }

        final Player player = getPlayerFromName(playerName);
        try {
            doMoveTiles(player, move);

            if(gameFinished()){
                handleGameEnd();
            }
            else{
                prepareForNextPlayer(); //Fill if necessary
                nextTurn(); //increment turn
                for (Client client : clients)
                    client.updateTurn(getCurrentPlayerName());
                saveGame();
            }
        } catch (InvalidMoveException e) {
            throw new ControllerGenericException("Error invalid move");
        } catch (BadPositionException e) {
            throw new ControllerGenericException("Error invalid get");
        }
    }

    /**
     * Do all the actions to prepare the board for the next player (Fill the board)
     * It should be manually called before the first and it is called after every
     * player's move.
     */
    public void prepareForNextPlayer(){
        if(board.toFill()){
            try {
                board.fill();
            } catch (OutOfTilesException e) {
                handleGameEnd();
            }
        }
    }

    /**
     * check if a given move can be done
     * @param move is the move the player do
     * @return true if the move is valid
     */
    private boolean checkValidMove(Move move) {
        List<Position> positions = move.getBoardPositions();

        //the number of coordinates is greater than the maximum possible ==> false
        if (positions.size() > move.getMaxNumMoves() || positions.size() <= 0)
            return false;

        //duplicate positions are present ==> false
        if(Position.duplicates(positions)){
            return false;
        }

        //Position with no free side ==> false
        for (Position p : positions) {
            if (!board.hasFreeSide(p))
                return false;
        }

        //One tile is always correct if it has a free side ==> true
        if (positions.size() == 1)
            return true;

        //check if the positions are correctly aligned
        return Position.areAligned(positions);

    }

    public JSONObject toJson() {
        JSONObject gameStatus = new JSONObject();
        gameStatus.put("board", board.toJson());
        gameStatus.put("turn", Long.valueOf(turn));

        JSONArray playersJson = new JSONArray();
        playersJson.addAll(
                players.stream()
                        .map(Player::toJson)
                        .collect(Collectors.toList())
        );
        gameStatus.put("players", playersJson);

        JSONArray commonGoalsJson = new JSONArray();
        commonGoalsJson.addAll(board.getCommonGoals()
                .stream()
                .map(CommonGoal::toJson)
                .collect(Collectors.toList()));
        gameStatus.put("commonGoals", commonGoalsJson);

        return gameStatus;
    }

    /**
     * This function is called by the lobby when a client disconnected.
     * The function removes the client and sets the corresponding player
     * as inactive.
     * @param client Client object
     * @throws ControllerGenericException when there is no client or
     *          no player matching the provided object.
     */
    public void clientDisconnected(Client client) {
        if(!clients.remove(client)){
            throw new ControllerGenericException("Provided client did not exist");
        }
        setActivity(client.getPlayerName(), false);
        for (Client c: clients) {
            if (clients.size()<GameSettings.minSupportedPlayers) {
                c.postChatMessage("Server", "Not enough players online, waiting for somebody to reconnect");
                //TODO start timer to eventually declare the victory
            }
        }
    }

    /**
     * This function is called by the lobby when a client connects
     * after a crash.
     * The function adds the client and sets the corresponding player
     * as active.
     * @param client Client object
     * @throws ControllerGenericException when there is no player
     *          with the provided name.
     */
    public void clientReconnected(Client client) {
        if(
                players.stream()
                        .map(x->x.getName().toLowerCase())
                        .noneMatch(x->x.equals(client.getPlayerName()))
        )
            throw new RuntimeException("No player with the given name found while reconnecting client");

        setActivity(client.getPlayerName(), true);
        for(Client c : clients)
            c.postChatMessage("Server", "User " + client.getPlayerName() + " joined the game.");

        clients.add(client);
        refreshClient(client);
    }

    /**
     * WARNING: this method should be used only for testing purposes.
     * This method is used to manually set the turn.
     * @param turn int representing the turn
     */
    protected void setTurn_testing_only(int turn){
        this.turn = turn;
    }
}