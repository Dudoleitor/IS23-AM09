package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.cli.CLI_IO;
import it.polimi.ingsw.shared.GameSettings;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.model.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static it.polimi.ingsw.shared.JSONFilePath.PlayerGoals;
import static org.junit.Assert.fail;

public class CLIIOTest {

    static final boolean verbose = false;
    @Test
    void shelves() {
        if (verbose) {
            Map<String, Shelf> playerShelves = new HashMap<>();
            playerShelves.put("fridgeieri", new Shelf(GameSettings.shelfRows, GameSettings.shelfColumns));
            playerShelves.put("dge", new Shelf(GameSettings.shelfRows, GameSettings.shelfColumns));
            playerShelves.put("ieri", new Shelf(GameSettings.shelfRows, GameSettings.shelfColumns));
            CLI_IO cliIO = new CLI_IO();

            cliIO.showShelves(playerShelves);
        }
    }
    @Test
    void personalGoals(){
        if(verbose){
            List<PlayerGoal> playerGoals = new ArrayList<>();
            PlayerGoal playerGoal;
            try{
                for (int goalToGen = 0; goalToGen < 4; goalToGen++) {
                    playerGoal = new PlayerGoal(PlayerGoals);
                    while (playerGoals.contains(playerGoal)) {
                        playerGoal = new PlayerGoal(PlayerGoals);
                    }
                    playerGoals.add(playerGoal);
                }
            }
            catch (Exception e){
                fail();
            }
            for(PlayerGoal pg : playerGoals){
                System.out.println(pg);
            }
        }
    }

    @Test
    void board() throws JsonBadParsingException, OutOfTilesException {
        if(verbose){
            Board board = new Board(4);
            board.fill();
            System.out.println(board);
        }
    }

    @Test
    void CommonGoal() throws JsonBadParsingException {
        if(verbose){
            for(CommonGoalStrategy strategy : CommonGoalStrategy.values()){
                System.out.println(new CommonGoal(strategy,4));
                System.out.println("\n\n");
            }
        }
    }

    @Test
    void endGame() throws JsonBadParsingException {
        if(verbose){
            /*Map<String,Integer> leaderboard = new HashMap<>();
            leaderboard.put("frigieri",200);
            leaderboard.put("firgioggi",30);
            leaderboard.put("frigdopodonmani",55);
            leaderboard.put("friegdomani",25);
            CLI_IO cliIO = new CLI_IO();
            Board board = new Board(4);
            Map<String,Shelf> shelves = new HashMap<>();
            for(String p: leaderboard.keySet()){
                shelves.put(p,new Shelf(6,5));
            }
            cliIO.endGame(leaderboard,"frigieri",shelves,board);*/
        }
    }
}
