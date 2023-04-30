package it.polimi.ingsw.client;

import it.polimi.ingsw.client.View.cli.CLI;
import it.polimi.ingsw.shared.GameSettings;
import it.polimi.ingsw.shared.model.Shelf;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class CLITest {

    static final boolean verbose = false;
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
}
