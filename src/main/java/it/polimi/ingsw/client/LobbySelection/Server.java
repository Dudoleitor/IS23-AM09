package it.polimi.ingsw.client.LobbySelection;

import it.polimi.ingsw.client.Lobby.Lobby;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.IpAddressV4;

import java.util.List;
import java.util.Map;

public abstract class Server {
    IpAddressV4 ip;
    int port;
    Server(IpAddressV4 ip, int port){
        this.ip = ip;
        this.port = port;
    }
    abstract boolean login(Client client);
    abstract List<Integer> getJoinedLobbies(String nick);
    abstract Lobby joinRandomLobby(Client client);
    abstract Lobby createLobby(Client client);
    abstract Map<Integer,Integer> showAvailableLobbbies();
    abstract Lobby joinSelectedLobby(Client client, int id);
    abstract Client generateClient(String playerName);
}
