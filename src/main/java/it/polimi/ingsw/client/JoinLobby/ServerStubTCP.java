package it.polimi.ingsw.client.JoinLobby;

import it.polimi.ingsw.client.Lobby.LobbyStub;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.IpAddressV4;

import java.util.List;
import java.util.Map;

public class ServerStubTCP extends ServerStub{
    public ServerStubTCP(IpAddressV4 ip, int port){
        super(ip,port);
    }
    @Override
    boolean login(Client client) {
        return false;
    }

    @Override
    List<Integer> getJoinedLobbies(String nick) {
        return null;
    }

    @Override
    LobbyStub joinRandomLobby(Client client) {
        return null;
    }

    @Override
    LobbyStub createLobby(Client client) {
        return null;
    }

    @Override
    Map<Integer, Integer> showAvailableLobbbies() {
        return null;
    }

    @Override
    LobbyStub joinSelectedLobby(Client client, int id) {
        return null;
    }

    @Override
    Client generateClient(String playerName) {
        return null;
    }
}
