package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.Constants;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.function.Predicate;

import static java.lang.Thread.sleep;
import static java.util.Arrays.stream;
import static org.junit.experimental.theories.internal.ParameterizedAssertionError.join;

public class ClientMain implements Runnable{
    public static void main(String argv[]) throws NotBoundException, InterruptedException {
        ServerRemoteInterface stub = null;
        Registry registry = null;
        String playerName = null;
        Scanner scanner = new Scanner(System.in);
        CLIPrinter printer = new CLIPrinter();

        //Set username
        while(!isValid(playerName)){
            printer.printMessage("Enter your username");
            printer.printPlaceHolder();
            playerName = scanner.nextLine();
            if(!isValid(playerName)){
                printer.printErrorMessage("Please enter a valid name");
            }
        }

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
        try {
            int lobbyID = stub.joinLobby(playerName, stub); //join first available lobby, otherwise creates one
            LobbyRemoteInterface lobbyStub = (LobbyRemoteInterface) registry.lookup(String.valueOf(lobbyID));
            LiveChat chat = new LiveChat(playerName, lobbyStub);
            Match match = new Match(playerName, lobbyStub);
            chat.start(); //initialize chat
            while(!lobbyStub.matchHasStarted()){
                if(!lobbyStub.startGame(playerName))
                    System.out.println("wrong player admin or lobby isn't full yet");
                sleep(5000);
            }
            match.start();
            match.join();
            chat.join(); //when chat is closed
            lobbyStub.quitGame(playerName, lobbyStub);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isValid(String name){

        Predicate<Character> isLowerCaseLetter =
                (c) -> {return c.compareTo('a') >= 0 && c.compareTo('z') <= 0;};
        Predicate<Character> isUpperCaseLetter =
                (c) -> {return c.compareTo('A') >= 0 && c.compareTo('Z') <= 0;};
        Predicate<Character> isNumber =
                (c) -> {return c.compareTo('0') >= 0 && c.compareTo('9') <= 0;};
        Predicate<Character> isValidSymbol =
                (c) -> {return c.compareTo('_') == 0 && c.compareTo('-') == 0;};
        return  name!= null &&
                name.chars().count() >= 1 &&
                name.chars().
                allMatch(c ->
                isValidSymbol.test((char) c) ||
                isNumber.test((char) c) ||
                isLowerCaseLetter.test((char) c) ||
                isUpperCaseLetter.test((char) c));
    }
    @Override
    public void run() {
    }
}
