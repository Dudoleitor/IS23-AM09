package it.polimi.ingsw.RMI;
import it.polimi.ingsw.server.Move;
import it.polimi.ingsw.shared.JsonBadParsingException;
import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerRemoteInterface extends Remote {
    boolean login(String nick) throws RemoteException;
    int joinLobby(String player, ServerRemoteInterface stub) throws RemoteException;
    int createLobby(String player, ServerRemoteInterface stub, int numPlayers) throws RemoteException;
}
