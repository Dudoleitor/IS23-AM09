package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import static it.polimi.ingsw.shared.Constants.jsonPathForPlayerGoals;

/**
 * This class implements the player inside the model.
 */
public class Player implements Jsonable {
    private final String name;
    private int commonGoalPoints;
    private final List<Integer> checkedCommonGoals;
    private final Shelf shelf;
    private final PlayerGoal goal;


    /**
     * This constructor is used to initialize a new player, with no properties
     * @param name player's name;
     * @param shelf player's shelf;
     * @param goal player's personal goal.
     */
    public Player(String name, Shelf shelf, PlayerGoal goal) {
        this(name, shelf, goal, 0, new ArrayList<>());
    }

    /**
     * This constructor is used to initialize a player with known properties.
     * @param name player's name;
     * @param shelf player's shelf;
     * @param goal player's personal goal;
     * @param commonGoalPoints integer, previously achieved points;
     * @param checkedCommonGoals list of integers, id of common goals the player already achieved.
     */
    public Player(String name, Shelf shelf, PlayerGoal goal, int commonGoalPoints, List<Integer> checkedCommonGoals) {
        this.name = name;
        this.shelf = shelf;
        this.goal = goal;
        this.commonGoalPoints = commonGoalPoints;
        this.checkedCommonGoals = new ArrayList<>(checkedCommonGoals);
    }

    /**
     * This constructor is used to initialize a player from a JSONObject
     * @param playerJson JSONObject with the player's properties
     * @throws JsonBadParsingException when a parsing error happens
     */
    public Player(JSONObject playerJson) throws JsonBadParsingException {
        try {
            this.name = (String) playerJson.get("Name");
            // Loading shelf
            this.shelf = new Shelf((JSONObject) playerJson.get("Shelf"));

            // Loading player goal
            this.goal = new PlayerGoal(jsonPathForPlayerGoals,
                    Math.toIntExact((Long) playerJson.get("PersonalGoalId")));

            // Loading common goals points
            this.commonGoalPoints = Math.toIntExact((Long) playerJson.get("CommonGoalsPoints"));

            // Loading common goals achieved ids
            JSONArray jsonPointsAchieved = (JSONArray) playerJson.get("CommonGoalsAchieved");
            if (jsonPointsAchieved==null) {throw new JsonBadParsingException("Error while loading player from json: goals achieved not found");}

            this.checkedCommonGoals = new ArrayList<>(jsonPointsAchieved);

        } catch (Exception e) {
            throw new JsonBadParsingException("Error while loading player from json: " + e.getMessage());
        }
    }
    public String getName() { return name; }

    /**
     * @return copy of the shelf.
     */
    public Shelf getShelf() { return new ServerShelf(shelf); }

    /**
     * To check how many points the player currently has achieved with the personal goal.
     * Note: this method is pure and runs the check when called.
     * @return integer.
     */
    public int checkPersonalGoal() throws JsonBadParsingException {
        return goal.check(shelf);
    }

    /**
     * @return id of the personal goal
     */
    public int getPersonalGoalId() {
        return goal.getGoalId();
    }

    /**
     * To check how many points the player currently has achieved by
     * placing tiles of the same type in adjacent positions.
     * @return integer.
     */
    public int getAdjacentPoints()  {
        return shelf.countAdjacentPoints();
    }

    /**
     * To check how many points the player currently has achieved with the common goals.
     * @return integer, the value is stored in the player.
     */
    public int getCommonGoalPoints() { return commonGoalPoints; }

    /**
     * Should be called to save which goals the player already has achieved.
     * @return list of Integers.
     */
    public List<Integer> getCheckedCommonGoals() {
        return new ArrayList<>(checkedCommonGoals);
    }

    /**
     * Runs the checks with the provided personal goals:
     * passes the shelf to the goal; if the goal is achieved and was not
     * previously achieved, pops points from the goal and saves them.
     * @param goals list of AbstractCommonGoals to be checked.
     */
    public void checkCommonGoals(List<CommonGoal> goals) {
        for (CommonGoal goal : goals) {
            if (goal.check(shelf) &&
                    !checkedCommonGoals.contains(goal.getID())) {
                commonGoalPoints += goal.givePoints();
                checkedCommonGoals.add(goal.getID());
            }
        }
    }

    /**
     * To edit the shelf.
     * @param tile Tile enum,
     * @param column int.
     */
    public void insertTile(Tile tile, int column) throws BadPositionException {
        shelf.insertTile(tile, column);
    }

    /**
     * Uses getHighestColumn on the shelf to determine if
     * the player has completed the shelf.
     * @return true/false.
     */
    public boolean hasFinished() {
        return shelf.getHighestColumn() == 0;
    }

    /**
     * This method is used to save the status of the player with a json object.
     * @return JSONObject with status.
     */
    public JSONObject toJson() {
        JSONObject playerJson = new JSONObject();
        playerJson.put("Name", name);
        playerJson.put("Shelf", shelf.toJson());
        playerJson.put("PersonalGoalId", Long.valueOf(goal.getGoalId()));
        playerJson.put("CommonGoalsPoints", Long.valueOf(commonGoalPoints));

        // Saving common goals achieved ids
        JSONArray commonGoals = new JSONArray();
        commonGoals.addAll(checkedCommonGoals);
        playerJson.put("CommonGoalsAchieved", commonGoals);

        return playerJson;
    }

    @Override
    public boolean equals(Object o) {  // Checking using LOWERCASE name
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name.toLowerCase(), player.getName().toLowerCase());
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
