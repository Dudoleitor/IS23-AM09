package it.polimi.ingsw.shared;

import org.json.simple.JSONObject;

public class PlayerWithPoints extends Jsonable{
    private final String playerName;
    private final int points;

    public PlayerWithPoints(String playerName, int points) {
        this.playerName = playerName;
        this.points = points;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
