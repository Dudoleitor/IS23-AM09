package it.polimi.ingsw.client.JoinLobby;

import it.polimi.ingsw.client.Lobby.LobbyRMIStub;
import it.polimi.ingsw.client.Lobby.LobbyStub;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;
import it.polimi.ingsw.shared.ClientRemoteObject;
import it.polimi.ingsw.shared.GameSettings;
import it.polimi.ingsw.shared.IpAddressV4;
import it.polimi.ingsw.shared.NetworkSettings;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyInterface;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;

public class ServerStubRMI extends ServerStub{
    private ServerInterface server;
    public ServerStubRMI(IpAddressV4 ip, int port){
        super(ip,port);
    }

    @Override
    public boolean login(Client client) {
        try {
            if(server == null){
                //get remote registry that points to 127.0.0.1:port
                Registry registry = LocateRegistry.getRegistry(NetworkSettings.serverIp.toString(), NetworkSettings.RMIport);
                //get interface from remote registry
                server = (ServerInterface) registry.lookup("interface");
                //try to log in
            }
            server.login((ClientRMI) client);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Integer> getJoinedLobbies(String nick) {
        List<Integer> lobbies = null;
        try {
            lobbies = server.getJoinedLobbies(nick);
        } catch (Exception e) {
            //TODO
        }
        return lobbies;
    }

    @Override
    public LobbyStub joinRandomLobby(Client client) {
        LobbyInterface lobbyRMI = null;
        try{
            lobbyRMI = server.joinRandomLobby(client).getStub();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //TODO
        }
        return new LobbyRMIStub(lobbyRMI);
    }

    @Override
    public LobbyStub joinSelectedLobby(Client client, int id) {
        LobbyInterface lobbyRMI = null;
        try{
            lobbyRMI = server.joinSelectedLobby(client,id).getStub();
        } catch (Exception e) {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LobbyStub createLobby(Client client) {
        LobbyInterface lobbyRMI = null;
        try{
            lobbyRMI = server.createLobby(client).getStub();
        } catch (Exception e) {
            //TODO
        }
        return new LobbyRMIStub(lobbyRMI);
    }

    @Override
    public Map<Integer, Integer> showAvailableLobbbies() {
        Map<Integer, Integer> availableLobbies = null;
        try{
            availableLobbies = server.showAvailableLobbies();
        } catch (Exception e) {
            //TODO
        }
        return availableLobbies;
    }
}
