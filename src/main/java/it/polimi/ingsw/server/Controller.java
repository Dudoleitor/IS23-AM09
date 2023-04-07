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
        List<Player> matchPlayers = new ArrayList<>();
        matchPlayers.addAll(players);
        return matchPlayers;
    }

    /**
     * turn is the actual turn of the match, every turn has an increment of 1, and we calculate (turn + 1) mod the size of players array
     * in order to get the actual player
     * @return the current player
     */

    //TODO: spezzare la funzione --> devo poter vedere il currentPlayer senza modificare il turno
    public String getCurrentPlayerName() {
        final int initialSize = getPlayers().size();
        int currentPlayerTurn = turn;

        List<ActivePlayer> activePlayers = new ArrayList<>();
        for(Player p : getPlayers()) {
            ActivePlayer p1 = new ActivePlayer(p, true);
            activePlayers.add(p1);
        }

        while(!activePlayers.get((currentPlayerTurn) % (initialSize)).isActive()) {
            next(currentPlayerTurn);
        }

        //At the end the actual turn could be incremented. In fact, if the currentPlayer
        //is not active, then the turn is skipped
        turn = currentPlayerTurn;
        return activePlayers.get((currentPlayerTurn) % (initialSize)).getPlayer().getName();
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
     * @param player is the current player
     * @param move is the move that player wants to do
     * @throws ControllerGenericException if the selected tile is not allowed, if the player is not the current player
     * or if the shelf hasn't enough empty cells
     */
    public void moveTiles(Player player, Move move) throws ControllerGenericException {

        try {
            if (!player.getName().equals(getCurrentPlayerName())) { //if player is not the current player we throw an exception
                throw new ControllerGenericException("Player is not the current player");
            }

            Shelf playerShelf = player.getShelf();

            if (move.getBoardPositions().size() >
                    playerShelf.allTilesInColumn(move.getColumn()).stream().filter(x -> x.equals(Tile.Empty)).count()
            ) { //if the size of the move coordinates is greater than empty cells in the self we throw an exception
                throw new InvalidMoveException("Number of tiles selected greater than empty fields in shelf");
            }
            if (checkValidMove(move))
                throw new InvalidMoveException("Tiles selection is not allowed");

            List<Position> positions = move.getBoardPositions();
            for (Position p : positions) { //for all the positions we insert the tile in the playerShelf
                Tile t = board.pickTile(p);
                playerShelf.insertTile(t, move.getColumn());
            }
            incrementTurn();
        } catch (InvalidMoveException e) {
            throw new ControllerGenericException("Error invalid move");
        } catch (BadPositionException e) {
            throw new ControllerGenericException("Error invalid get");
        }
    }
    /**
     * increment the private variable turn
     */
    public void incrementTurn() {
        turn += 1;
    }

    private void next(int actualTurn) {
        actualTurn += 1;
    }

    /**
     * check if a given move can be done
     * @param move is the move the player do
     * @return true if the move is valid
     */
    private boolean checkValidMove(Move move) {
        List<Position> positions = move.getBoardPositions();

        //the number of coordinates is greater than the maximum possible ==> false
        if (positions.size() > move.getMaxNumMoves())
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

    private class ActivePlayer {
        Player player;
        boolean isActive;

        public ActivePlayer(Player player, boolean isActive) {
            this.player = player;
            this.isActive = isActive;
        }

        public Player getPlayer() {
            return player;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }
    }

}