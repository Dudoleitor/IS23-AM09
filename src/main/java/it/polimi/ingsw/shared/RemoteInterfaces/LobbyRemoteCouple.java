package it.polimi.ingsw.shared.RemoteInterfaces;

import java.io.Serializable;
import java.rmi.Remote;

public class LobbyRemoteCouple implements Serializable {
    private final LobbyInterface stub;
    private final int port;

    public LobbyRemoteCouple(LobbyInterface stub, int port){
        this.stub = stub;
        this.port = port;
    }
    public LobbyInterface getStub(){
        return stub;
    }
    public int getPort(){
        return port;
    }
}
