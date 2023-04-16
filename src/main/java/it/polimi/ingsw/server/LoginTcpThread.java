package it.polimi.ingsw.server;

import it.polimi.ingsw.client.InputSanitizer;
import it.polimi.ingsw.client.Lobby.LobbyStub;
import it.polimi.ingsw.server.clientonserver.ClientSocket;

import java.io.*;
import java.net.Socket;

public class LoginTcpThread extends Thread{ //TODO WIP
    private final ClientSocket client;
    private final ServerMain server;

    public LoginTcpThread(ServerMain server, ClientSocket client){
        this.server = server;
        this.client= client;


    }

    public void run(){
        if(!tryLogin(30)) {
            client.out("Error while trying to login");
            this.interrupt();
        }
        int lobbyPort = tryJoin(client);
        client.out(String.valueOf(lobbyPort));
    }

    private boolean tryLogin(int tries){
        boolean logged = false;
        String name = "";
        //Try to log in for 30s (1 try each second)
        for(int attempt = 0; attempt < tries && !logged; tries++) {
            try {
                name = client.in();
                synchronized (server) {
                    logged = server.login(client); //get previous sessions if present
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (logged){
            client.setName(name);
            synchronized (server) {
                client.out(server.getJoinedLobbies(name).toString()); //TODO to convert to JSON or others
            }
            }

        return logged;
    }
    private int tryJoin(ClientSocket client){
        boolean lobbySelected = false;
        int lobbyID;
        int lobbyPort = -1;
        while(!lobbySelected){
            String id;
            id = client.in();
            if(id.isEmpty()){
                synchronized (server) {
                    lobbyPort = server.joinRandomLobby(client).getPort(); //join first available lobby, otherwise creates one
                }
                lobbySelected = true;
            } else{ //TODO to parse
                lobbyID = Integer.parseInt(id);
                synchronized (server) {
                    lobbyPort = server.joinSelectedLobby(client, lobbyID).getPort();
                }
                if(lobbyPort == -1)
                    client.out(String.valueOf(lobbyPort)); //client will handle this case
                else lobbySelected = true;
            }

        }
        return lobbyPort;
    }

}
