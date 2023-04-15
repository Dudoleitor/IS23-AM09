package it.polimi.ingsw.client.JoinLobby;

import it.polimi.ingsw.client.Lobby.LobbyRMIStub;
import it.polimi.ingsw.client.Lobby.LobbyStub;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;
import it.polimi.ingsw.shared.ClientRemoteObject;
import it.polimi.ingsw.shared.Constants;
import it.polimi.ingsw.shared.IpAddressV4;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyRemoteInterface;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerRemoteInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;

public class ServerStubRMI extends ServerStub{
    private ServerRemoteInterface server;
    public ServerStubRMI(IpAddressV4 ip, int port){
        super(ip,port);
    }

    @Override
    public boolean login(Client client) {
        try {
            if(server == null){
                //get remote registry that points to 127.0.0.1:port
                Registry registry = LocateRegistry.getRegistry(Constants.serverIp.toString(), Constants.port);
                //get interface from remote registry
                server = (ServerRemoteInterface) registry.lookup("interface");
                //try to log in
            }
            server.login((ClientRMI) client);
            return true;
        } catch (RemoteException | NotBoundException e) {
            return false;
        }
    }

    @Override
    public List<Integer> getJoinedLobbies(String nick) {
        List<Integer> lobbies = null;
        try {
            lobbies = server.getJoinedLobbies(nick);
        } catch (RemoteException e) {
            //TODO
        }
        return lobbies;
    }

    @Override
    public LobbyStub joinRandomLobby(Client client) {
        LobbyRemoteInterface lobbyRMI = null;
        try{
            lobbyRMI = server.joinRandomLobby(client);
        } catch (RemoteException e) {
            throw new RuntimeException();
            //TODO
        }
        return new LobbyRMIStub(lobbyRMI);
    }

    @Override
    public LobbyStub joinSelectedLobby(Client client, int id) {
        LobbyRemoteInterface lobbyRMI = null;
        try{
            lobbyRMI = server.joinSelectedLobby(client,id);
        } catch (RemoteException e) {
            //TODO
        }
        return new LobbyRMIStub(lobbyRMI);
    }

    @Override
    Client generateClient(String playerName) {
        try {
            ClientRemoteObject remoteObject = new ClientRemoteObject(playerName);
            ClientRMI client = new ClientRMI(remoteObject);
            return client;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LobbyStub createLobby(Client client) {
        LobbyRemoteInterface lobbyRMI = null;
        try{
            lobbyRMI = server.createLobby(client);
        } catch (RemoteException e) {
            //TODO
        }
        return new LobbyRMIStub(lobbyRMI);
    }

    @Override
    public Map<Integer, Integer> showAvailableLobbbies() {
        Map<Integer, Integer> availableLobbies = null;
        try{
            availableLobbies = server.showAvailableLobbbies();
        } catch (RemoteException e) {
            //TODO
        }
        return availableLobbies;
    }
}
