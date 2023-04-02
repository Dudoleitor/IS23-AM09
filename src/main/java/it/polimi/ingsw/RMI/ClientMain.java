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
        System.out.println(Color.coloredString("ENTER YOUR USERNAME",Color.Green));
        playerName = scanner.nextLine();
        boolean logged = false;
        for(int tries = 0; tries < 30 && !logged; tries++){
            try {
                registry = LocateRegistry.getRegistry(Constants.serverIp.toString(), Constants.port); //get remote registry that points to 127.0.0.1:port
                stub = (RemoteCall) registry.lookup("interface"); //get interface from remote registry
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
        else{
            System.out.println("30s elapsed. Server is down, retry later");
            return;
        }

        //Get shelf
        Thread match = new Match(playerName,stub);
        match.start();

        //CHAT
        Thread liveChat = new LiveChat(playerName,stub);
        liveChat.start();

        join(String.valueOf(match));
        join(String.valueOf(liveChat));

        /*boolean end =stub.shutDown();
        if(end){
            System.out.println("Server is now down");
        }*/
    }
}
