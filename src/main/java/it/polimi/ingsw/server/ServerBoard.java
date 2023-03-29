package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Board;
import it.polimi.ingsw.shared.*;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class ServerBoard extends Board {
    public ServerBoard(int numPlayers) throws JsonBadParsingException {
        super(numPlayers);
    }
}
