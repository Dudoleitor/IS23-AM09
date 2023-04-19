package it.polimi.ingsw.shared.RemoteInterfaces;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;

import java.rmi.Remote;
import java.util.List;
import java.util.Map;

public interface ServerInterface extends Remote {
    boolean login(Client client) throws Exception;
    Map<Integer,Integer> getJoinedLobbies(String nick) throws Exception;
    ServerLobbyInterface joinRandomLobby(Client client) throws Exception;
    ServerLobbyInterface createLobby(Client client) throws Exception;
    Map<Integer,Integer> showAvailableLobbies() throws Exception;
    ServerLobbyInterface joinSelectedLobby(Client client, int id) throws Exception;

}
