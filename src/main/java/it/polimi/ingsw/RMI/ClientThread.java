package it.polimi.ingsw.RMI;

import java.rmi.RemoteException;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ClientThread extends Thread{
    protected String playerName;
    protected LobbyRemoteInterface stub;
    protected static Concurrent_cli_IO io = new Concurrent_cli_IO(new ReentrantLock());
    protected InputSanitizer inputSanitizer = new InputSanitizer();
    protected boolean exit;
    ClientThread(String playerName, LobbyRemoteInterface stub){
        this.playerName = playerName;
        this.stub = stub;
    }
    protected void loopCommands(){
        String command;
        while(!exit){ //Receive commands until "exit" command is launched
            try{
                command = io.scan();
                executeUserCommand(command);
            }
            catch (RemoteException e){
                io.printErrorMessage("Error in RMI");
            } catch (Exception e) {
                io.printErrorMessage("Error in Message Format");
            }
        }
    }
    protected abstract void executeUserCommand(String command) throws Exception;
}
