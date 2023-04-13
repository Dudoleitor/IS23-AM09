package it.polimi.ingsw.shared.virtualview;

import it.polimi.ingsw.server.ClientStub;
import it.polimi.ingsw.server.Player;
import it.polimi.ingsw.server.PlayerGoal;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VirtualShelfTest {
    @Test
    void insertTest() throws JsonBadParsingException, BadPositionException {
        String jsonPath = getClass().getClassLoader().getResource("PlayerGoalTests").getPath() + "/TestGoal.json";
        String name = "fridgeieri";
        Shelf shelf = new Shelf(3, 3);
        PlayerGoal goal = new PlayerGoal(jsonPath);
        Player testpl = new Player(name, shelf, goal);

        List<Client> clients = new ArrayList<>();
        clients.add(new ClientStub("fridgeieri"));
        clients.add(new ClientStub("fridgeoggi"));
        testpl.getVirtualShelf().setClientList(clients);

        for (Client client : clients) {
            assertFalse(((ClientStub) client).wasUpdated());
        }

        testpl.insertTile(Tile.Cat, 0);
        for (Client client : clients) {
            assertTrue(((ClientStub) client).wasUpdated());
            ((ClientStub) client).reset();
        }

        testpl.insertTile(Tile.Plant, 0);
        for (Client client : clients) {
            assertTrue(((ClientStub) client).wasUpdated());
            ((ClientStub) client).reset();
        }

        testpl.insertTile(Tile.Trophy, 1);
        for (Client client : clients) {
            assertTrue(((ClientStub) client).wasUpdated());
            ((ClientStub) client).reset();
        }
        testpl.insertTile(Tile.Empty, 2);
        for (Client client : clients) {
            assertFalse(((ClientStub) client).wasUpdated());
        }
    }
}
