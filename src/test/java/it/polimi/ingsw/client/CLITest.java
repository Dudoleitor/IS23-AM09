package it.polimi.ingsw.client;

import it.polimi.ingsw.client.View.cli.CLI;
import it.polimi.ingsw.shared.GameSettings;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.model.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static it.polimi.ingsw.shared.JSONFilePath.PlayerGoals;
import static org.junit.Assert.fail;

public class CLITest {

    static final boolean verbose = true;
    @Test
    void shelves() {
        if (verbose) {
            Map<String, Shelf> playerShelves = new HashMap<>();
            playerShelves.put("fridgeieri", new Shelf(GameSettings.shelfRows, GameSettings.shelfColumns));
            playerShelves.put("dge", new Shelf(GameSettings.shelfRows, GameSettings.shelfColumns));
            playerShelves.put("ieri", new Shelf(GameSettings.shelfRows, GameSettings.shelfColumns));
            CLI cli = new CLI();

            cli.showShelves(playerShelves);
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
}
