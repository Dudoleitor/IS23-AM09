package it.polimi.ingsw.client.LobbySelection;

import it.polimi.ingsw.client.Lobby.LobbyRMI;
import it.polimi.ingsw.client.Lobby.Lobby;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;
import it.polimi.ingsw.client.controller.ClientControllerCLI;
import it.polimi.ingsw.shared.IpAddressV4;
import it.polimi.ingsw.shared.NetworkSettings;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerLobbyInterface;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

public class ServerRMI extends Server {
    private ServerInterface server;
    public ServerRMI(IpAddressV4 ip, int port){
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
    public Map<Integer,Integer> getJoinedLobbies(String playerName) throws ServerException{
        Map<Integer,Integer> lobbies = null;
        try {
            lobbies = server.getJoinedLobbies(playerName);
        } catch (Exception e) {
            throw new ServerException("Error in Server");
        }
        return lobbies;
    }

    @Override
    public Lobby joinRandomLobby(Client client) throws ServerException{
        ServerLobbyInterface lobbyRMI = null;
        try{
            lobbyRMI = server.joinRandomLobby(client).getStub();
        } catch (Exception e) {
            throw new ServerException("Error in Server");
        }
        return new LobbyRMI(lobbyRMI);
    }

    @Override
    public Lobby joinSelectedLobby(Client client, int id) throws ServerException{
        ServerLobbyInterface lobbyRMI = null;
        try{
            lobbyRMI = server.joinSelectedLobby(client,id).getStub();
        } catch (Exception e) {
            throw new ServerException("Error in Server");
        }
        return new LobbyRMI(lobbyRMI);
    }

    @Override
    public Lobby createLobby(Client client) throws ServerException{
        ServerLobbyInterface lobbyRMI = null;
        try{
            lobbyRMI = server.createLobby(client).getStub();
        } catch (Exception e) {
            throw new ServerException("Error in Server");
        }
        return new LobbyRMI(lobbyRMI);
    }

    @Override
    public Map<Integer, Integer> getAvailableLobbies() throws ServerException{
        Map<Integer, Integer> availableLobbies = null;
        try{
            availableLobbies = server.showAvailableLobbies();
        } catch (Exception e) {
            throw new ServerException("Error in Server");
        }
        return availableLobbies;
    }
}
