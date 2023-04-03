package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.Color;
import it.polimi.ingsw.shared.Constants;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;
import static org.junit.experimental.theories.internal.ParameterizedAssertionError.join;

public class ClientMain implements Runnable{
    public static void main(String argv[]) throws NotBoundException, InterruptedException {
        RemoteCall stub = null;
        Registry registry = null;
        String playerName;
        Scanner scanner = new Scanner(System.in);

        //Set username
        System.out.println(Color.coloredString("ENTER YOUR USERNAME",Color.Green));
        playerName = scanner.nextLine();
        printMessage("Hello "+playerName);
        boolean logged = false;
        //Try to log in for 30s (1 try each second)
        for(int tries = 0; tries < 30 && !logged; tries++){
            try {
                printMessage("Login attempt");
                //get remote registry that points to 127.0.0.1:port
                registry = LocateRegistry.getRegistry(Constants.serverIp.toString(), Constants.port);
                //get interface from remote registry
                stub = (RemoteCall) registry.lookup("interface");
                //try to log in
                logged = stub.login(playerName);
            }
            catch (ConnectException e){
                printErrorMessage("Server was down, retying in 1 second");
                sleep(1000);
            }
            catch (RemoteException e) {
                printErrorMessage("Error in RMI, retying in 1 second");
                sleep(1000);
            }
        }
        if (logged)
            printStatusUpdate("Login was successful");
        else{ //if none of the 30 tries was successful
            printErrorMessage("30s elapsed. Server is down, retry later");
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

    public static void printErrorMessage(String mes){
        System.out.println(Color.coloredString(mes,Color.Red));
    }

    public static void printStatusUpdate(String mes){
        System.out.println(Color.coloredString(mes,Color.Green));
    }

    public static void printMessage(String mes){
        System.out.println(Color.coloredString(mes,Color.Yellow));
    }

    @Override
    public void run() {

    }
}
