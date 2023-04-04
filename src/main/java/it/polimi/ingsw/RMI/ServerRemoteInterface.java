package it.polimi.ingsw.RMI;
import it.polimi.ingsw.server.Move;
import it.polimi.ingsw.shared.JsonBadParsingException;
import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ServerRemoteInterface extends Remote {
    boolean login(String nick) throws RemoteException;
    List<Integer> getJoinedLobbies(String nick) throws RemoteException;
    int joinRandomLobby(String player) throws RemoteException;
    int createLobby(String player, int numPlayers) throws RemoteException;
    Map<Integer,Integer> showAvailableLobbbies() throws RemoteException;
    boolean joinSelectedLobby(String player, int id) throws RemoteException;

}
