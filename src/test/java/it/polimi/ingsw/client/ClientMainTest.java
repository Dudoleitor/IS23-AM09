package it.polimi.ingsw.client;

import it.polimi.ingsw.client.Connection.ConnectionStub;
import it.polimi.ingsw.client.View.LobbyCommand;
import it.polimi.ingsw.client.View.LobbySelectionCommand;
import it.polimi.ingsw.client.View.ViewDriver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;
import java.util.Queue;

import static it.polimi.ingsw.client.Client_Settings.Connection.STUB;
import static it.polimi.ingsw.client.Client_Settings.UI.DRIVER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientMainTest {
    @BeforeAll
    static void setTestSettings(){
        Client_Settings cs = new Client_Settings();
        cs.setConnection(STUB);
        cs.setUI(DRIVER);
    }

    @Test
    void test1(){
        ViewDriver viewDriver = new ViewDriver();
        ConnectionStub connectionStub = new ConnectionStub();

        //Set command sequence to execute
        LobbySelectionCommand lc = LobbySelectionCommand.Number;
        lc.setId(0);
        viewDriver.putLobbySelectionCommand(lc);
        //viewDriver.putCommand(LobbyCommand.Start); //buggy
        //viewDriver.putCommand(LobbyCommand.Message); //buggy
        viewDriver.putCommand(LobbyCommand.Exit);

        //execute client
        ClientMain.startClient();

        //check for correct order
        Queue<String> calls = connectionStub.getCallsToStub();

        Queue<String> callsDouble = new PriorityQueue<>();
        callsDouble.add("frigioggi login");
        callsDouble.add("joined lobbies");
        callsDouble.add("available lobbies");
        callsDouble.add("frigioggi specific lobby 0");
        callsDouble.add("frigioggi quit");

        assertTrue(calls.containsAll(callsDouble));
        assertTrue(callsDouble.containsAll(calls));
        assertEquals(callsDouble.toString(),calls.toString());
    }
}