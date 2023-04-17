package it.polimi.ingsw.server;

import it.polimi.ingsw.server.clientonserver.ClientSocket;

public class LobbyTcpThread extends Thread{
    private final ClientSocket client;
    private final Lobby lobby;

    public LobbyTcpThread(ClientSocket client, Lobby lobby) {
        this.client = client;
        this.lobby = lobby;
    }


}
