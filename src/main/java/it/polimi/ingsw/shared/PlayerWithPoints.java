package it.polimi.ingsw.shared;

import org.json.simple.JSONObject;

import java.io.Serializable;

public class PlayerWithPoints implements Jsonable, Serializable {
    private final String playerName;
    private final int points;

    public PlayerWithPoints(String playerName, int points) {
        this.playerName = playerName;
        this.points = points;
    }
    public PlayerWithPoints(JSONObject jsonCouple){
        playerName = jsonCouple.get("user").toString();
        points = Integer.parseInt(jsonCouple.get("points").toString());
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public JSONObject toJson() {
        JSONObject leaderboard = new JSONObject();
        leaderboard.put("user", playerName);
        leaderboard.put("points", points);

        return leaderboard;
    }
}
