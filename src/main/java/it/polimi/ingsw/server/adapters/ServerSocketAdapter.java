package it.polimi.ingsw.server.adapters;

import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyRemoteInterface;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ServerSocketAdapter { //TODO it will implement an interface
    private ServerMain server;
    public ServerSocketAdapter(ServerMain server){
        this.server = server;
    }

    public boolean login(ClientRMI clientRMI) {
        return false;
        //TODO
    }

    public List<Integer> getJoinedLobbies(String nick) throws RemoteException {
        return null;
        //TODO
    }

    public LobbyRemoteInterface joinRandomLobby(Client client) throws RemoteException {
        return null;
        //TODO
    }

    public LobbyRemoteInterface createLobby(Client client) throws RemoteException {
        return null;
        //TODO
    }

    public Map<Integer, Integer> showAvailableLobbbies() throws RemoteException {
        return null;
        //TODO
    }

    public LobbyRemoteInterface joinSelectedLobby(Client client, int id) throws RemoteException {
        return null;
        //TODO
    }
}
