package it.polimi.ingsw.RMI;

import java.util.concurrent.locks.ReentrantLock;

public abstract class ClientThread extends Thread{
    protected String playerName;
    protected LobbyRemoteInterface stub;
    protected static ConcurrentCLiPrinter printer = new ConcurrentCLiPrinter(new ReentrantLock());
    ClientThread(String playerName, LobbyRemoteInterface stub){
        this.playerName = playerName;
        this.stub = stub;
    }
}
