package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.Color;
import it.polimi.ingsw.shared.Constants;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import static java.lang.Thread.sleep;
import static org.junit.experimental.theories.internal.ParameterizedAssertionError.join;

public class ClientMain {
    public static void main(String argv[]) throws NotBoundException, InterruptedException {
        RemoteCall stub = null;
        Registry registry = null;
        String playerName;
        Scanner scanner = new Scanner(System.in);
        //Set username
        System.out.println(Color.coloredString("ENTER YOUR USERNAME",Color.Green));
        playerName = scanner.nextLine();
        boolean logged = false;
        //Try to log in for 30s (1 try each second)
        for(int tries = 0; tries < 30 && !logged; tries++){
            try {
                //get remote registry that points to 127.0.0.1:port
                registry = LocateRegistry.getRegistry(Constants.serverIp.toString(), Constants.port);
                //get interface from remote registry
                stub = (RemoteCall) registry.lookup("interface");
                //try to log in
                logged = stub.login(playerName);
            }
            catch (ConnectException e){
                System.out.println("Server was down, retying in 1 second");
                sleep(1000);
            }
            catch (RemoteException e) {
                System.out.println("Error in RMI, retying in 1 second");
                sleep(1000);
            }
        }
        if (logged)
            System.out.println("Logged successfully");
        else{ //if none of the 30 tries was successful
            System.out.println("30s elapsed. Server is down, retry later");
            return;
        }

        //Launch match handling thread
        Thread match = new Match(playerName,stub);
        match.start();

        //CHAT
        Thread liveChat = new LiveChat(playerName,stub);
        liveChat.start();

        //End all threads
        liveChat.join();
        match.join();

        try {
            //shutdown server
            stub.shutDown();
            System.out.println("Server is now down");
        } catch (RemoteException e) {
            System.out.println("Failed to shut server down");
        }
    }
}
