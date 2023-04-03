package it.polimi.ingsw.RMI;

import java.util.List;
import java.util.Scanner;
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

    public void threadSafePrint(String str){
        lock.lock();
        System.out.print(str);
        lock.unlock();
    }

    public void threadSafePrint(List<String> stringList){
        lock.lock();
        for(String s : stringList){
            System.out.println(s);
        }
        lock.unlock();
    }

    public String threadSafeScan(Scanner scanner){
        lock.lock();
        String command = scanner.nextLine();
        lock.unlock();
        return command;
    }
}
