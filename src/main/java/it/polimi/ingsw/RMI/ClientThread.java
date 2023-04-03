package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.Color;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ClientThread extends Thread{
    protected String playerName;
    protected RemoteCall stub;
    protected static ConcurrentCLiPrinter printer = new ConcurrentCLiPrinter(new ReentrantLock());
    ClientThread(String playerName, RemoteCall stub){
        this.playerName = playerName;
        this.stub = stub;
    }
}
