package it.polimi.ingsw.client.JoinLobby;

import it.polimi.ingsw.server.clientonserver.Client;

public abstract class JoinLobbyUI extends Thread{
    //Variables
    protected String playerName;
    protected ServerStub server;
    protected Client client;

    //Constructor
    JoinLobbyUI(ServerStub server){
        this.server = server;
    }

    //Methods
    public abstract void setUserName();
    public abstract boolean tryLogin(Client client, int tries);
    public abstract void showJoinedLobbies();
    public abstract void joinLobby(Client client);
    public abstract void showLobbyList();
}
