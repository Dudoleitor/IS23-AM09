package it.polimi.ingsw.shared.virtualview;

import it.polimi.ingsw.server.ClientStub;
import it.polimi.ingsw.server.InvalidMoveException;
import it.polimi.ingsw.shared.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VirtualBoardTest {
    @Test
    void pickTest() throws OutOfTilesException, JsonBadParsingException, BadPositionException, InvalidMoveException {
        //partialMove.addPosition(pos3);
        String jsonPath = "src/test/resources/BoardTests/BoardTestInsert.json";

        List<Client> clients = new ArrayList<>();
        clients.add(new ClientStub("fridgeieri"));
        clients.add(new ClientStub("fridgeoggi"));

        Board b = new Board(Jsonable.pathToJsonObject(jsonPath, Board.class), new ArrayList<>());
        b.getVirtualBoard().setClientList(clients);

        b.pickTile(0, 4);  // Empty tile
        for (Client client : clients) {
            assertFalse(((ClientStub) client).wasUpdated());
        }

        b.fill();
        for (Client client : clients) {
            assertTrue(((ClientStub) client).wasUpdated());
            ((ClientStub) client).reset();
        }

        b.pickTile(0, 3);
        for (Client client : clients) {
            assertTrue(((ClientStub) client).wasUpdated());
            ((ClientStub) client).reset();
        }

        Tile t = b.pickTile(0, 4);
        for (Client client : clients) {
            assertTrue(((ClientStub) client).wasUpdated());
            ((ClientStub) client).reset();
        }
    }
}
