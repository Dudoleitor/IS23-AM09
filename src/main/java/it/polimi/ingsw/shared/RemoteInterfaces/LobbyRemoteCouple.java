package it.polimi.ingsw.shared.RemoteInterfaces;

import java.io.Serializable;

public class LobbyRemoteCouple implements Serializable {
    private final ServerLobbyInterface stub;
    private final int port;

    public LobbyRemoteCouple(ServerLobbyInterface stub, int port){
        this.stub = stub;
        this.port = port;
    }
    public ServerLobbyInterface getStub(){
        return stub;
    }
    public int getPort(){
        return port;
    }
}
