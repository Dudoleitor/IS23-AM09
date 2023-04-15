package it.polimi.ingsw.server.adapters;

import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyRemoteInterface;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerRemoteInterface;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ServerRMIAdapter implements ServerRemoteInterface {
    private final ServerMain server;
    public ServerRMIAdapter(ServerMain server){
        this.server = server;
    }
    @Override
    public boolean login(ClientRMI clientRMI) throws RemoteException {
        return server.login(clientRMI);
    }

    @Override
    public List<Integer> getJoinedLobbies(String nick) throws RemoteException {
        return server.getJoinedLobbies(nick);
    }

    @Override
    public LobbyRemoteInterface joinRandomLobby(Client client) throws RemoteException {
        return server.joinRandomLobby(client).getStub();
    }

    @Override
    public LobbyRemoteInterface createLobby(Client client) throws RemoteException {
        return server.createLobby(client).getStub();
    }

    @Override
    public Map<Integer, Integer> showAvailableLobbbies() throws RemoteException {
        return server.showAvailableLobbies();
    }

    @Override
    public LobbyRemoteInterface joinSelectedLobby(Client client, int id) throws RemoteException {
        return server.joinSelectedLobby(client, id).getStub();
    }
}
