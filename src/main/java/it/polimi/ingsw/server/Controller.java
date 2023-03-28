package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.*;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;


public class Controller {
    private final int shelfRows = 6;
    private final int shelfColumns = 5;
    private final String jsonPathForPlayerGoals = "src/main/resources/personalGoals.json";

    private final Board board;
    private final List<Player> players;
    private int turn;

    /**
     * Constructor used to initialize the controller
     * @param namePlayers is a List of the (unique) names of the players
     * @throws ControllerGenericException when parsing error occurs
     */
    public Controller(List<String> namePlayers) throws ControllerGenericException {
        try {
            players = new ArrayList<>();
            turn = 0;
            for (String s : namePlayers) {
                players.add(new Player(s, new ServerShelf(shelfRows, shelfColumns),
                        new PlayerGoal(jsonPathForPlayerGoals)));
            }
            board = new Board(players.size());
        } catch (ClassCastException | NullPointerException e) {
            throw new ControllerGenericException("Error while creating the Controller : bad json parsing");
        } catch (BoardGenericException e) {
            throw new ControllerGenericException("Problem while creating controller");
        } catch (PlayerGoalLoadingException e) {
            throw new ControllerGenericException(e.getMessage());
        } catch (ShelfGenericException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return the commonGoals we instantiated in the constructor
     */
    public List<CommonGoal> getCommonGoals() {
        return board.getCommonGoals();
    }

    /**
     * @return the players of the match
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * turn is the actual turn of the match, every turn has an increment of 1, and we calculate (turn + 1) mod the size of players array
     * in order to get the actual player
     * @return the current player
     */
    public String getCurrentPlayer() {
        return getPlayers().get((turn + 1) % (getPlayers().size())).getName();
    }

    /**
     * @return the list of the Shelf objects of each player
     */
    public List<Shelf> getShelves() throws ShelfGenericException {
        List<Shelf> shelves = new ArrayList<>();
        for(Player p : players) {
            shelves.add(p.getShelf());
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
    public void moveTiles(Player player, Move move) throws ControllerGenericException, InvalidMoveException {

        try {
            if (!player.getName().equals(getCurrentPlayer())) { //if player is not the current player we throw an exception
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
        } catch (BoardGenericException e){
            throw new InvalidMoveException("Error while moving tile");
        } catch (ShelfGenericException e) {
            throw new ControllerGenericException(e.getMessage());
        } catch (InvalidMoveException e) {
            throw new ControllerGenericException("Error invalid move");
        }
    }
    /**
     * increment the private variable turn
     */
    private void incrementTurn() {
        turn += 1;
    }

    /**
     * check if a given move can be done
     * @param move is the move the player do
     * @return true if the move is valid
     */
    private boolean checkValidMove(Move move) {
        try {
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

        } catch (BoardGenericException e){
            return false;
        }
    }
}