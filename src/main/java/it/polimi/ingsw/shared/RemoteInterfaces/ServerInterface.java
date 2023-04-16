package it.polimi.ingsw.shared.RemoteInterfaces;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;

import java.rmi.Remote;
import java.util.List;
import java.util.Map;

public interface ServerInterface extends Remote {
    boolean login(ClientRMI clientRMI) throws Exception;
    List<Integer> getJoinedLobbies(String nick) throws Exception;
    LobbyInterface joinRandomLobby(Client client) throws Exception;
    LobbyInterface createLobby(Client client) throws Exception;
    Map<Integer,Integer> showAvailableLobbbies() throws Exception;
    LobbyInterface joinSelectedLobby(Client client, int id) throws Exception;

}
