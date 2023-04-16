package it.polimi.ingsw.server.adapters;

import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyInterface;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyRemoteCouple;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerInterface;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ServerSocketAdapter implements ServerInterface { //TODO it will implement an interface
    private ServerMain server;
    public ServerSocketAdapter(ServerMain server){
        this.server = server;
    }

    @Override
    public boolean login(ClientRMI clientRMI) throws Exception{
        return false;
        //TODO
    }
    @Override
    public List<Integer> getJoinedLobbies(String nick) throws Exception {
        return null;
        //TODO
    }
    @Override
    public LobbyRemoteCouple joinRandomLobby(Client client) throws Exception {
        return null;
        //TODO
    }
    @Override
    public LobbyRemoteCouple createLobby(Client client) throws Exception {
        return null;
        //TODO
    }
    @Override
    public Map<Integer, Integer> showAvailableLobbbies() throws Exception {
        return null;
        //TODO
    }
    @Override
    public LobbyRemoteCouple joinSelectedLobby(Client client, int id) throws Exception {
        return null;
        //TODO
    }
}
