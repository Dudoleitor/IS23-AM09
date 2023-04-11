package it.polimi.ingsw.server;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.shared.Constants.jsonPathForPlayerGoals;


public class Controller implements Jsonable {
    private final Board board;
    private final List<Player> players;
    private int turn;


    /**
     * Constructor used to initialize the controller
     * @param namePlayers is a List of the (unique) names of the players
     */
    public Controller(List<String> namePlayers){
        if(namePlayers == null){
            throw new ControllerGenericException("Error while creating the Controller: namePlayers was null");
        }
        try {
            players = new ArrayList<>();
            turn = 0;
            for (String name : namePlayers) {
                players.add(new Player(name, new Shelf(Constants.shelfRows,Constants.shelfColumns),
                        new PlayerGoal(jsonPathForPlayerGoals)));
            }
            board = new Board(players.size());
        } catch (NullPointerException e) {
            throw new ControllerGenericException("Error while creating the Controller : Json file doesn't exists");
        } catch (ClassCastException | JsonBadParsingException e) {
            throw new ControllerGenericException("Error while creating the Controller : bad Json parsing");
        }
    }

    /**
     * This constructor is used to load the state of the game from a jsonfile.
     * @param gameStatus JSONObject with the status
     * @throws JsonBadParsingException when a parsing error happens
     */
    public Controller(JSONObject gameStatus) throws JsonBadParsingException {
        try {
            JSONArray commonGoalsJson = (JSONArray) gameStatus.get("commonGoals");
            this.board = new Board((JSONObject) gameStatus.get("board"), new ArrayList<>(commonGoalsJson));
            this.turn = Math.toIntExact((Long) gameStatus.get("turn"));

            // Loading players
            this.players = new ArrayList<>();
            JSONArray playersJson = (JSONArray) gameStatus.get("players");
            if (playersJson==null) {throw new JsonBadParsingException("Error while loading game status from json: players field not found");}

            JSONObject jsonPlayer;
            for (Object objPlayer : playersJson) {
                jsonPlayer = (JSONObject) objPlayer;
                this.players.add(new Player(jsonPlayer));
            }
        } catch (Exception e) {
            throw new JsonBadParsingException("Error while loading game status from json: " + e.getMessage());
        }
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

    /**
     * @return active players of the match
     */
    public List<Player> getActivePlayers() {
            return players.stream().
                    filter(p -> p.isActive()).
                    map(p-> {
                        try {
                            return new Player(p);
                        } catch (JsonBadParsingException e) {
                            throw new RuntimeException(e);
                        }
                    }).
                    collect(Collectors.toList());
    }

    public void setActivity(String playerName, boolean value) throws ControllerGenericException{
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
        List<Player> activePlayers = getActivePlayers();

        if(!activePlayers.get((getTurn()) % activePlayers.size()).isActive()) {
            nextTurn();
        }
    }

    /**
     * @return the current player
     */
    public String getCurrentPlayerName() {

        return getPlayers().get(getTurn() % getActivePlayers().size()).getName();
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
     * insert Tiles in the player shelf according to move coordinates
     * @param playerName is the current player
     * @param move is the move that player wants to do
     * @throws ControllerGenericException if the selected tile is not allowed, if the player is not the current player
     * or if the shelf hasn't enough empty cells
     */
    public void moveTiles(String playerName, Move move) throws ControllerGenericException {
        Player player; //doesnt work as an Optional TODO for @Jack
        if(playerName == null){
            throw new ControllerGenericException("No player found with that name");
        }
        else if(move == null){
            throw new ControllerGenericException("Null move");
        }
        else {
            player = players.stream().
                    filter(p -> p.getName().equals(playerName)).
                    findFirst().orElse(null);
            if (player == null) {
                throw new ControllerGenericException("No player found with that name");
            }
        }

        try {
            if (!player.getName().equals(getCurrentPlayerName())) { //if player is not the current player we throw an exception
                throw new ControllerGenericException("Player is not the current player");
            }

            Shelf playerShelf = player.getShelf();
            int freeSpaceInColumn = (int) playerShelf.
                    allTilesInColumn(move.getColumn()).stream().
                    filter(x -> x.equals(Tile.Empty)).
                    count();
            if (move.getBoardPositions().size() > freeSpaceInColumn) { //if the size of the move coordinates is greater than empty cells in the self we throw an exception
                throw new InvalidMoveException("Number of tiles selected greater than empty fields in shelf");
            }
            if (!checkValidMove(move))
                throw new InvalidMoveException("Tiles selection is not allowed");

            List<Position> positions = move.getBoardPositions();
            for (Position p : positions) { //for all the positions we insert the tile in the playerShelf
                player.insertTile(board.pickTile(p), move.getColumn());
            }
            prepareForNextPlayer(); //Fill if necessary
            nextTurn(); //increment turn
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
                //do nothing
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
}