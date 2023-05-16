package it.polimi.ingsw.RMI;

import it.polimi.ingsw.client.controller.cli.LobbyCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyCommandTest {
    @Test
    void match(){
        assertEquals(LobbyCommand.Help, LobbyCommand.stringToCommand("Help"));
        assertEquals(LobbyCommand.Invalid, LobbyCommand.stringToCommand("helpa"));
    }
}