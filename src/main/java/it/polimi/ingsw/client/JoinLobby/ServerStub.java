package it.polimi.ingsw.client.JoinLobby;

import it.polimi.ingsw.client.Lobby.LobbyStub;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.IpAddressV4;

import java.util.List;
import java.util.Map;

public abstract class ServerStub {
    IpAddressV4 ip;
    int port;
    ServerStub(IpAddressV4 ip, int port){
        this.ip = ip;
        this.port = port;
    }
    abstract boolean login(Client client);
    abstract List<Integer> getJoinedLobbies(String nick);
    abstract LobbyStub joinRandomLobby(Client client);
    abstract LobbyStub createLobby(Client client);
    abstract Map<Integer,Integer> showAvailableLobbbies();
    abstract LobbyStub joinSelectedLobby(Client client, int id);
    abstract Client generateClient(String playerName);
}
