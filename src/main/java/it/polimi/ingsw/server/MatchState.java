package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Board;
import org.json.simple.JSONObject;

import java.util.*;

public class MatchState {
    private JSONObject board;
    private List<JSONObject> commonGoals;
    private Map<String,JSONObject> Shelves;

    //Remove comment as soon as toJSON are implemented for all classes
    /*
    public MatchState(Board board, List<Player> players){
        this.board = board.toJSON();
        this.commonGoals = board.getCommonGoals().toJSON();
        this.Shelves = new HashMap<>();
        for(Player player : players){
            Shelves.put(player.getName(),player.getShelf().toJSON());
        }
    }*/

    public JSONObject getBoard(){
        return board;
    }

    public List<JSONObject> getAllCommonGoalsJson(){
        ArrayList<JSONObject> result = new ArrayList<>();
        result.addAll(commonGoals);
        return result;
    }

    public JSONObject getPlayerShelfJson(String playerName){
        if(Shelves.containsKey(playerName)){
            return Shelves.get(playerName);
        }
        else{ //TODO should it throw exception?
            return new JSONObject();
        }
    }
}
