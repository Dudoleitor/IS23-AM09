package it.polimi.ingsw.RMI;

import java.rmi.RemoteException;

public class KeepAlive extends Thread{ //AH AH AH AH staying alive staying alive
    private String playerName;
    private LobbyRemoteInterface stub;
    boolean alive;
    private static int sleepTime = 5000;
    KeepAlive(String playerName, LobbyRemoteInterface stub){
        this.playerName = playerName;
        this.stub = stub;
        alive = true;
    }
    @Override
    public void run(){
        while(alive){
            try {
                stub.keepAlive(playerName);
                sleep(sleepTime);
            } catch (InterruptedException | RemoteException e) {
                throw new RuntimeException(e); //TODO handle
            }
        }
    }

    public void interrupt(){
        alive = false;
    }
}
