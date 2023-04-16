package it.polimi.ingsw.shared.RemoteInterfaces;

public class LobbyRemoteCouple {
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
