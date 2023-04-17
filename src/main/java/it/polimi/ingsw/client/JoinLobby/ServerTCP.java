package it.polimi.ingsw.client.JoinLobby;

import it.polimi.ingsw.client.Lobby.Lobby;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;
import it.polimi.ingsw.server.clientonserver.ClientSocket;
import it.polimi.ingsw.shared.IpAddressV4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ServerTCP extends Server {
    private PrintWriter serverOut;
    private BufferedReader serverIn;

    public ServerTCP(IpAddressV4 ip, int port) {
        super(ip, port);
        try {
            Socket server = new Socket(ip.toString(), port);
            serverOut = new PrintWriter(server.getOutputStream(), true);
            serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String in(){
        boolean ready = false;
        try {
            while(!ready){
                if(serverIn.ready())
                    ready = true;
            }
            return serverIn.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * send a message through socket connection
     * @param message is the message to send
     */
    public void out(String message){
        serverOut.println(message);
    }
    @Override
    boolean login(Client client) {
        out(client.getPlayerName());
        return Boolean.parseBoolean(in());
    }


    @Override
    List<Integer> getJoinedLobbies(String nick) {
        return null;
    }

    @Override
    Lobby joinRandomLobby(Client client) {
        return null;
    }

    @Override
    Lobby createLobby(Client client) {
        return null;
    }

    @Override
    Map<Integer, Integer> showAvailableLobbbies() {
        return null;
    }

    @Override
    Lobby joinSelectedLobby(Client client, int id) {
        return null;
    }

    @Override
    Client generateClient(String playerName) {
        ClientSocket client = new ClientSocket(null); //I know it's bad, but for now it reamains as it is, because it is need locally
        client.setName(playerName);
        return client;
    }
}
