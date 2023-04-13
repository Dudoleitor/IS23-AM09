package it.polimi.ingsw.RMI;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ServerRemoteInterface extends Remote {
    boolean login(ClientRMI clientRMI) throws RemoteException;
    List<Integer> getJoinedLobbies(String nick) throws RemoteException;
    int joinRandomLobby(Client client) throws RemoteException;
    int createLobby(Client client) throws RemoteException;
    Map<Integer,Integer> showAvailableLobbbies() throws RemoteException;
    boolean joinSelectedLobby(Client client, int id) throws RemoteException;

}
