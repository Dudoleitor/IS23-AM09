package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Board;
import it.polimi.ingsw.shared.ShelfGenericException;
import org.json.simple.JSONObject;

import java.util.*;

public class MatchState {
    private JSONObject board;
    private List<JSONObject> commonGoals;
    private Map<String,JSONObject> Shelves;
    private List<JSONObject> players;
    int turn;
    public MatchState(Controller controller){
        this.board = controller.getBoard().toJson();

        this.Shelves = new HashMap<>();
        this.commonGoals = new ArrayList<>();
        this.players = new ArrayList<>();

        for(CommonGoal c : controller.getCommonGoals()){
            commonGoals.add(c.toJson());
        }
        for(Player player : controller.getPlayers()){
            try {
                //TODO uncomment as soon as player.toJson exists
                //players.add(player.toJson());
                Shelves.put(player.getName(),player.getShelf().toJson());
            } catch (ShelfGenericException e) {
                throw new RuntimeException(e);
            }
        }
        turn = controller.getTurn();
    }

    public JSONObject getBoardJson(){
        return board;
    }

    public List<JSONObject> getAllPlayersJson(){
        return players;
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
        else{ //TODO handle properly
            throw new ControllerGenericException("");
        }
    }

    public int getTurn(){
        return turn;
    }
}
