package it.polimi.ingsw.client.LobbySelection;

import it.polimi.ingsw.client.Lobby.Lobby;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientSocket;
import it.polimi.ingsw.shared.IpAddressV4;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.MessageTcp;

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
        MessageTcp login = new MessageTcp();
        login.setCommand(MessageTcp.MessageCommand.Login); //set command
        login.setContent(Jsonable.string2json(client.getPlayerName())); //set playername as JsonObject
        out(login.toString());
        MessageTcp response = new MessageTcp(in()); //wait for response by server and create an object response

        return Jsonable.json2boolean(response.getContent());


    }


    @Override
    Map<Integer,Integer> getJoinedLobbies(String playerName) throws ServerException {
        return null;
    }

    @Override
    Lobby joinRandomLobby(Client client) throws ServerException {
        return null;
    }

    @Override
    Lobby createLobby(Client client) throws ServerException {
        return null;
    }

    @Override
    Map<Integer, Integer> getAvailableLobbies()throws ServerException {
        return null;
    }

    @Override
    Lobby joinSelectedLobby(Client client, int id)throws ServerException {
        return null;
    }

    @Override
    Client generateClient(String playerName) {
        ClientSocket client = new ClientSocket(); //I know it's bad, but for now it reamains as it is, because it is need locally
        client.setName(playerName);
        return client;
    }
}
