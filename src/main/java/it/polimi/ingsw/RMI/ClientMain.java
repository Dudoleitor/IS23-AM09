package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.Constants;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static java.lang.Thread.sleep;
import static java.util.Arrays.stream;
import static org.junit.experimental.theories.internal.ParameterizedAssertionError.join;

public class ClientMain{
    //Variables
    static ServerRemoteInterface stub = null;
    static String playerName = null;
    static Registry registry = null;
    static cli_IO io = new cli_IO();
    static LobbyRemoteInterface lobbyStub;
    public static void main(String argv[]) throws NotBoundException, InterruptedException {
        setUserName();
        io.printMessage("Hello "+playerName+"!");
        boolean logged = tryLogin();
        if (logged)
            io.printMessage("Login was successful");
        else{ //if none of the 30 tries was successful
            io.printErrorMessage("Shutting down. 30s elapsed");
            return;
        }
        try {
            joinLobby();
            LiveChat chat = new LiveChat(playerName, lobbyStub);
            Match match = new Match(playerName, lobbyStub);
            chat.start(); //initialize chat
            while(!lobbyStub.matchHasStarted()){
                if(!lobbyStub.startGame(playerName))
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
    public static boolean isSanitized(String name){
        return  name!= null &&
                name.chars().count() >= 1 &&
                name.chars().
                allMatch(c ->
                isValidSymbol((char) c) ||
                isNumber((char) c) ||
                isLowerCaseLetter((char) c) ||
                isUpperCaseLetter((char) c));
    }
    public static boolean isNumber(String id){
        return id != null &&
                id.chars().count() >= 1 &&
                id.chars().allMatch(c ->
                isNumber((char) c)
                );
    }
    public static boolean isLowerCaseLetter(Character c){
        return c.compareTo('a') >= 0 && c.compareTo('z') <= 0;
    }
    public static boolean isUpperCaseLetter(Character c){
        return c.compareTo('A') >= 0 && c.compareTo('Z') <= 0;
    }
    public static boolean isNumber(Character c){
        return c.compareTo('0') >= 0 && c.compareTo('9') <= 0;
    }
    public static boolean isValidSymbol(Character c){
        return c.compareTo('_') == 0 && c.compareTo('-') == 0;
    }


    public static void setUserName(){
        while(!isSanitized(playerName)){
            io.printMessage("Enter your username");
            playerName = io.scan();
            if(!isSanitized(playerName)){
                io.printErrorMessage("Please enter a valid name");
            }
        }
    }

    public static boolean tryLogin() throws InterruptedException {
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
            catch (ConnectException | NotBoundException e){
                io.printErrorMessage("Server was down, retying in 1 second");
                sleep(1000);
            }
            catch (RemoteException e) {
                io.printErrorMessage("Error in RMI, retying in 1 second");
                sleep(1000);
            }
        }
        return logged;
    }

    public static void joinLobby() throws RemoteException, NotBoundException {
        boolean lobbySelected = false;
        int lobbyID = 0;
        while(!lobbySelected){
            io.printMessage("Choose a Lobby (ENTER for random):");
            String id = io.scan();
            if(id.length() == 0){
                lobbyID = stub.joinRandomLobby(playerName); //join first available lobby, otherwise creates one
            }
            else if(isNumber(id)){
                lobbyID = Integer.valueOf(id);
                lobbySelected = stub.joinSelectedLobby(playerName,lobbyID);
            }
            else{
                io.printErrorMessage("Input a valid id (must be a number)");
            }
        }
        lobbyStub = (LobbyRemoteInterface) registry.lookup(String.valueOf(lobbyID));
    }

}
