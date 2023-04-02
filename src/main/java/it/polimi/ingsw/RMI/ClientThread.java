package it.polimi.ingsw.RMI;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ClientThread extends Thread{
    protected String playerName;
    protected RemoteCall stub;
    public static Lock lock = new ReentrantLock();;
    ClientThread(String playerName, RemoteCall stub){
        this.playerName = playerName;
        this.stub = stub;
    }
}
