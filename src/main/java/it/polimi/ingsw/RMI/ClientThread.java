package it.polimi.ingsw.RMI;

import java.util.concurrent.locks.ReentrantLock;

public abstract class ClientThread extends Thread{
    protected String playerName;
    protected LobbyRemoteInterface stub;
    protected static Concurrent_cli_IO io = new Concurrent_cli_IO(new ReentrantLock());
    ClientThread(String playerName, LobbyRemoteInterface stub){
        this.playerName = playerName;
        this.stub = stub;
    }
}
