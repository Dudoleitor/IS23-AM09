package it.polimi.ingsw.RMI;

public abstract class ClientThread extends Thread{
    protected String playerName;
    protected RemoteCall stub;
    ClientThread(String playerName, RemoteCall stub){
        this.playerName = playerName;
        this.stub = stub;
    }
}
