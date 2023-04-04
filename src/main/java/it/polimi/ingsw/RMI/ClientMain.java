package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.Constants;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import static java.lang.Thread.sleep;
import static org.junit.experimental.theories.internal.ParameterizedAssertionError.join;

public class ClientMain implements Runnable{
    public static void main(String argv[]) throws NotBoundException, InterruptedException {
        ServerRemoteInterface stub = null;
        Registry registry = null;
        String playerName;
        Scanner scanner = new Scanner(System.in);
        CLIPrinter printer = new CLIPrinter();

        //Set username
        printer.printMessage("Enter your username");
        printer.printPlaceHolder();
        playerName = scanner.nextLine();
        printer.printMessage("Hello "+playerName+"!");
        boolean logged = false;
        //Try to log in for 30s (1 try each second)
        for(int tries = 0; tries < 30 && !logged; tries++){
            try {
                //get remote registry that points to 127.0.0.1:port
                registry = LocateRegistry.getRegistry(Constants.serverIp.toString(), Constants.port);
                //get interface from remote registry
                stub = (ServerRemoteInterface) registry.lookup("interface");
                //try to log in
                logged = stub.login(playerName);
            }
            catch (ConnectException e){
                printer.printErrorMessage("Server was down, retying in 1 second");
                sleep(1000);
            }
            catch (RemoteException e) {
                printer.printErrorMessage("Error in RMI, retying in 1 second");
                sleep(1000);
            }
        }
        if (logged)
            printer.printMessage("Login was successful");
        else{ //if none of the 30 tries was successful
            printer.printErrorMessage("Shutting down. 30s elapsed");
            return;
        }
        try { //this is a test behavioural
            stub.joinLobby(playerName, stub); //join first available lobby, otherwise creates one
            LiveChat chat = new LiveChat(playerName, stub);
            Match match = new Match(playerName, stub);
            chat.start(); //initialize chat
            while(!stub.matchHasStarted(playerName)){
                sleep(5000);
            }
            match.start();
            match.join();
            chat.join(); //when chat is closed
            stub.quitGame(playerName, stub);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {

    }
}
