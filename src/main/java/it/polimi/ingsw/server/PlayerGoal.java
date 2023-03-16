package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Shelf;
import it.polimi.ingsw.shared.Tile;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class loads a player's goal from a json file and
 * implements the method to calculate the amount of
 * points achieved by the player.
 */
public class PlayerGoal {
    private final List<GoalPosition> positionList;
    private final JSONObject pointsMap;
    private final int goalId;

    /**
     * This constructor is used when the goalId is known
     * @param jsonPath Path to the json file
     * @param goalId id of the goal to load
     * @throws PlayerGoalLoadingException when a parsing error happens
     */
    public PlayerGoal(String jsonPath, int goalId) throws PlayerGoalLoadingException {
        this.goalId = goalId;
        JSONParser jsonParser = new JSONParser();
        try {
            Object obj = jsonParser.parse(new FileReader(jsonPath));
            this.pointsMap = (JSONObject) ((JSONObject) obj).get("points");
            if (pointsMap == null) {
                throw new PlayerGoalLoadingException("Error while parsing json: points not found");}

            JSONObject goals = (JSONObject) ((JSONObject) obj).get("goals");
            if (goals == null) {
                throw new PlayerGoalLoadingException("Error while parsing json: goals not found");}

            JSONArray goal = (JSONArray) goals.get(String.valueOf(goalId));
            if (goal == null) {
                throw new PlayerGoalLoadingException("Error while parsing json: goal by id not found");}

            this.positionList = (List<GoalPosition>) goal
                    .stream()
                    .map(g -> parsePosition((JSONObject) g))
                    .collect(Collectors.toList());

        } catch (FileNotFoundException e) {
            throw new PlayerGoalLoadingException("Error while loading json: file not found");
        } catch (ParseException | IOException | ClassCastException | NullPointerException e) {
            throw new PlayerGoalLoadingException("Error while parsing json");
        }
    }

    /**
     * This constructor is used when the goalId needs to be chosen randomly
     * @param jsonPath Path to the json file
     * @throws PlayerGoalLoadingException when a parsing error happens
     */
    public PlayerGoal(String jsonPath) throws PlayerGoalLoadingException {
        JSONParser jsonParser = new JSONParser();
        Random rand = new Random();
        try {
            Object obj = jsonParser.parse(new FileReader(jsonPath));
            this.pointsMap = (JSONObject) ((JSONObject) obj).get("points");
            if (pointsMap == null) {
                throw new PlayerGoalLoadingException("Error while parsing json: points not found");}

            JSONObject goals = (JSONObject) ((JSONObject) obj).get("goals");
            if (goals == null) {
                throw new PlayerGoalLoadingException("Error while parsing json: goals not found");}

            goalId = rand.nextInt(goals.size());  // Choosing a random objective

            JSONArray goal = (JSONArray) goals.get(String.valueOf(goalId));
            if (goal == null) {
                throw new PlayerGoalLoadingException("Error while parsing json: goal by id not found");}

            this.positionList = (List<GoalPosition>) goal
                    .stream()
                    .map(g -> parsePosition((JSONObject) g))
                    .collect(Collectors.toList());

        } catch (FileNotFoundException e) {
            throw new PlayerGoalLoadingException("Error while loading json: file not found");
        } catch (ParseException | IOException | ClassCastException e) {
            throw new PlayerGoalLoadingException("Error while parsing json");
        }

    }

    /**
     * This function parses a single json object and returns a GoalPosition
     * @param jsonBlock json object to be parsed
     * @return GoalPosition, parsing result
     * @throws PlayerGoalLoadingException when a parsing error happens
     */
    private GoalPosition parsePosition(JSONObject jsonBlock) throws PlayerGoalLoadingException {
        try {
            String tileStr = jsonBlock.get("tileName").toString();
            Tile tile = Tile.valueOf(tileStr);

            int posRow = Math.toIntExact((Long) jsonBlock.get("posRow"));
            int posCol = Math.toIntExact((Long) jsonBlock.get("posCol"));
            return new GoalPosition(new Position(posRow, posCol), tile);
        } catch (ClassCastException | IllegalArgumentException e) {
            throw new PlayerGoalLoadingException("Error while parsing json: wrong position attributes");
        }
    }

    /**
     * This predicate checks if a single tile is in the correct position
     * @param shelf Player's shelf
     * @param pos GoalPosition to be checked
     * @return true/false
     */
    private boolean checkSinglePosition(Shelf shelf, GoalPosition pos) {
        return pos.getTile()
                .equals(
                        shelf.getTile(pos.getPos())
                );
    }

    /**
     * This function maps the number of correct positions to the point achieved
     * @param c Correct position counted
     * @return Points achieved
     * @throws PlayerGoalLoadingException when a parsing error happens
     */
    private int countToPoints(int c) throws PlayerGoalLoadingException {
        if (c==0) return 0;
        try {
            return Math.toIntExact(
                    (Long) pointsMap.get(String.valueOf(c))
            );
        } catch (Exception e) {
            throw new PlayerGoalLoadingException("Error while parsing json: wrong point map");
        }
    }

    /**
     * This function uses the player's shelf and the properties of the
     * goal to calculate the amount of points achieved.
     * @param shelf player's shelf
     * @return points achieved
     * @throws PlayerGoalLoadingException when a parsing error happens
     */
    public int check(Shelf shelf) throws PlayerGoalLoadingException {
        int count = positionList
                .stream()
                .mapToInt( p -> checkSinglePosition(shelf, p) ? 1 : 0)
                .sum();
            return countToPoints(count);
    }

    public int getGoalId() { return goalId; }
}

class GoalPosition {
    private final Position pos;
    private final Tile tile;


    public Position getPos() {
        return pos;
    }

    public Tile getTile() {
        return tile;
    }

    public GoalPosition(Position pos, Tile tile) {
        this.pos = pos;
        this.tile = tile;
    }
    @Override
    public String toString() {
        return "GoalPosition{" +
                "pos=" + pos +
                ", tile=" + tile +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoalPosition that = (GoalPosition) o;
        return Objects.equals(pos, that.getPos()) && tile == that.getTile();
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, tile);
    }
}
