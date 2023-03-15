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

public class PlayerGoal {
    private final List<GoalPosition> positionList;
    private final JSONObject pointsMap;
    private final int goalId;

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

    private boolean checkSinglePosition(Shelf shelf, GoalPosition pos){
        return pos.getTile()
                .equals(
                        shelf.getTile(pos.getPos())
                );
    }

    private int countToPoints(int c) throws PlayerGoalLoadingException {
        try {
            return Math.toIntExact(
                    (Long) pointsMap.get(String.valueOf(c))
            );
        } catch (Exception e) {
            throw new PlayerGoalLoadingException("Error while parsing json: wrong point map");
        }
    }
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
        return Objects.equals(pos, that.pos) && tile == that.tile;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, tile);
    }
}
