package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.ClientRMI;
import it.polimi.ingsw.shared.Constants;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;
import static java.util.Arrays.stream;
import static org.junit.experimental.theories.internal.ParameterizedAssertionError.join;

public class ClientMain{
    //Variables
    static ServerRemoteInterface stub = null;

    // TODO WARNING this field must be final!!
    // this needs to disappear from here asap
    static String playerName = null;
    static Registry registry = null;
    static cli_IO io = new cli_IO();
    static LobbyRemoteInterface lobbyStub;
    static InputSanitizer inputSanitizer = new InputSanitizer();


    public static void main(String argv[]) throws NotBoundException, InterruptedException {
        setUserName();
        ClientRMI clientRMI = new ClientRMI(playerName);
        io.printMessage("Hello "+playerName+"!");
        boolean logged = tryLogin(clientRMI);
        if (logged)
            io.printMessage("Login was successful");
        else{ //if none of the 30 tries was successful
            io.printErrorMessage("Shutting down. 30s elapsed");
            return;
        }
        try {
            showLobbyList();
            joinLobby(clientRMI);
            LiveChat chat = new LiveChat(playerName, lobbyStub);
            Match match = new Match(playerName, lobbyStub);
            chat.start(); //initialize chat

            //TODO orribile
            while(!lobbyStub.matchHasStarted()){
                if(!lobbyStub.startGame(playerName))
                sleep(5000);
            }

            match.start();
            match.join();
            chat.join(); //when chat is closed
            io.printErrorMessage("You quit the game!");
            lobbyStub.quitGame(playerName, lobbyStub);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setUserName(){
        while(!inputSanitizer.isValidName(playerName)){
            io.printMessage("Enter your username");
            playerName = io.scan();
            if(!inputSanitizer.isValidName(playerName)){
                io.printErrorMessage("Please enter a valid name");
            }
        }
    }

    public static boolean tryLogin(ClientRMI clientRMI) throws InterruptedException {
        boolean logged = false;
        //Try to log in for 30s (1 try each second)
        for(int tries = 0; tries < 30 && !logged; tries++){
            try {
                //get remote registry that points to 127.0.0.1:port
                registry = LocateRegistry.getRegistry(Constants.serverIp.toString(), Constants.port);
                //get interface from remote registry
                stub = (ServerRemoteInterface) registry.lookup("interface");
                //try to log in
                logged = stub.login(clientRMI); //get previous sessions if present
                if(logged) {
                    showJoinedLobbies();
                }
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

    public static void showJoinedLobbies(){
        try {
            List<Integer> previousSessions = stub.getJoinedLobbies(playerName);
            if (previousSessions != null && !previousSessions.isEmpty()) { //if some previous session is available
                io.printMessage("Welcome back!");
                String message = "Here are your already joined lobbies";
                List<String> previousLobbies = previousSessions.stream().
                        map(x -> "\n   --> Lobby " + x).
                        collect(Collectors.toList());
                for(String s : previousLobbies){
                    message = message.concat(s);
                }
                io.printMessage(message);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static void joinLobby(ClientRMI clientRMI) throws RemoteException, NotBoundException {
        boolean lobbySelected = false;
        int lobbyID = 0;
        while(!lobbySelected){
            io.printMessage("Choose a Lobby (ENTER for random):");
            String id = io.scan();
            if(id.isEmpty()){
                lobbyID = stub.joinRandomLobby(clientRMI); //join first available lobby, otherwise creates one
                lobbySelected = true;
            }
            else if(inputSanitizer.isInteger(id)){
                lobbyID = Integer.parseInt(id);
                lobbySelected = stub.joinSelectedLobby(clientRMI, lobbyID);
                if(!lobbySelected){
                    io.printErrorMessage("Input id not found");
                }
            }
            else{
                io.printErrorMessage("Input a valid id (must be a number)");
            }
        }
        io.printMessage("You joined #"+lobbyID+" lobby!");
        lobbyStub = (LobbyRemoteInterface) registry.lookup(String.valueOf(lobbyID));
    }
    public static void showLobbyList(){
        try {
            Map availableLobbies = stub.showAvailableLobbbies();
            if (!availableLobbies.isEmpty()) {
                String lobbyMessage = "Here are the active lobbies:";
                List<String> lobbyList = (List<String>)
                        availableLobbies.keySet().stream().
                        map(x -> "\n   --> Lobby " + x + ":  " + availableLobbies.get(x) + " players in").
                        collect(Collectors.toList());
                for(String mes : lobbyList){
                    lobbyMessage = lobbyMessage.concat(mes);
                }
                io.printMessage(lobbyMessage);
            } else {
                io.printMessage("No other active lobby is available, you might want to create one");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

}
