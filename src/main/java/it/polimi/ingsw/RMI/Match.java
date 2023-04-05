package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.Shelf;

import java.rmi.RemoteException;

public class Match extends ClientThread{
    Match(String playerName, LobbyRemoteInterface stub) {
        super(playerName, stub);
    }
    @Override
    public void run() {

    }
}
