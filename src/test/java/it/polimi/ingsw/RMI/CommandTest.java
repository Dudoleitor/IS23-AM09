package it.polimi.ingsw.RMI;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {
    @Test
    void match(){
        assertEquals(Command.Peek,Command.stringToCommand("PEEK"));
        assertEquals(Command.Refresh,Command.stringToCommand(""));
        assertEquals(Command.Invalid,Command.stringToCommand("Peeka"));
    }
}