package it.polimi.ingsw.shared.RemoteInterfaces;

public class LobbyRemoteCouple {
    private final LobbyRemoteInterface stub;
    private final int port;

    public LobbyRemoteCouple(LobbyRemoteInterface stub, int port){
        this.stub = stub;
        this.port = port;
    }
    public LobbyRemoteInterface getStub(){
        return stub;
    }
    public int getPort(){
        return port;
    }
}
