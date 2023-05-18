package it.polimi.ingsw.client;

import it.polimi.ingsw.client.connection.ConnectionStub;
import it.polimi.ingsw.client.controller.cli.ClientControllerCLI;
import it.polimi.ingsw.client.controller.cli.LobbyCommand;
import it.polimi.ingsw.client.controller.cli.LobbySelectionCommand;
import it.polimi.ingsw.client.controller.ClientControllerDriver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static it.polimi.ingsw.client.Client_Settings.Connection.STUB;
import static it.polimi.ingsw.client.Client_Settings.UI.DRIVER;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
class ClientControllerCLITest {
    @BeforeAll
    static void setTestSettings(){
        Client_Settings cs = new Client_Settings();
        cs.setConnection(STUB);
        cs.setUI(DRIVER);
    }

    @Test
    void test1(){
        ClientControllerDriver viewDriver = new ClientControllerDriver();
        ConnectionStub connectionStub = new ConnectionStub();

        //Set command sequence to execute
        LobbySelectionCommand lc = LobbySelectionCommand.Create;
        viewDriver.putLobbySelectionCommand(lc);
        viewDriver.putCommand(LobbyCommand.Start);
        viewDriver.putCommand(LobbyCommand.Message);
        viewDriver.putCommand(LobbyCommand.Secret);
        viewDriver.putCommand(LobbyCommand.Message);
        viewDriver.putCommand(LobbyCommand.Message);
        viewDriver.putCommand(LobbyCommand.Secret);
        viewDriver.putCommand(LobbyCommand.Secret);
        viewDriver.putCommand(LobbyCommand.Exit);


        //execute client
        (new ClientControllerCLI()).startClient();

        //check for correct order
        LinkedList<String> calls = connectionStub.getCallsToStub();

        LinkedList<String> callsDouble = new LinkedList<>();
        callsDouble.addLast("frigioggi login");
        callsDouble.addLast("available lobbies");
        callsDouble.addLast("joined lobbies");
        callsDouble.addLast("frigioggi create lobby");
        callsDouble.addLast("frigioggi asked lobby admin");
        callsDouble.addLast("frigioggi asked lobby admin");
        callsDouble.addLast("frigioggi started game");
        callsDouble.addLast("frigioggi posted A really meaningful message");
        callsDouble.addLast("frigioggi posted secret A really secret message for frigieri");
        callsDouble.addLast("frigioggi posted A really meaningful message");
        callsDouble.addLast("frigioggi posted A really meaningful message");
        callsDouble.addLast("frigioggi posted secret A really secret message for frigieri");
        callsDouble.addLast("frigioggi posted secret A really secret message for frigieri");
        callsDouble.addLast("frigioggi quit");

        assertEquals(callsDouble.toString(),calls.toString());
    }
}