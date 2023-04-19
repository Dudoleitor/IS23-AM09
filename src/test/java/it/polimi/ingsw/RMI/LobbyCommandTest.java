package it.polimi.ingsw.RMI;

import it.polimi.ingsw.client.View.LobbyCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyCommandTest {
    @Test
    void match(){
        assertEquals(LobbyCommand.Peek, LobbyCommand.stringToCommand("PEEK"));
        assertEquals(LobbyCommand.Invalid, LobbyCommand.stringToCommand("Peeka"));
    }
}