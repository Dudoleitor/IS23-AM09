package it.polimi.ingsw.client;

import it.polimi.ingsw.client.Lobby.LobbyCLI;
import it.polimi.ingsw.client.Lobby.LobbyRMIStub;
import it.polimi.ingsw.server.clientonserver.ClientRMI;
import it.polimi.ingsw.shared.ClientRemoteObject;
import it.polimi.ingsw.shared.Constants;
import it.polimi.ingsw.shared.RemoteInterfaces.ClientRemote;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyRemoteInterface;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerRemoteInterface;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class ClientMain{
    //Variables
    static ServerRemoteInterface stub = null;

    // TODO WARNING this field must be final!!
    // this needs to disappear from here asap
    static String playerName;
    static Registry registry;
    static cli_IO io = new cli_IO();
    static LobbyRemoteInterface lobbyRemoteInterface;
    static InputSanitizer inputSanitizer = new InputSanitizer();
    public static void main(String argv[]) throws NotBoundException, InterruptedException, RemoteException {
        setUserName();
        ClientRemote clientRemote = new ClientRemoteObject(playerName);
        ClientRMI clientRMI = new ClientRMI(clientRemote);
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
            LobbyCLI cli = new LobbyCLI(playerName, new LobbyRMIStub(lobbyRemoteInterface));
            cli.start();
            cli.join();
            io.printErrorMessage("You quit the game!");
            lobbyRemoteInterface.quitGame(playerName, lobbyRemoteInterface);
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
        int lobbyID;
        while(!lobbySelected){
            io.printMessage("Choose a Lobby (ENTER for random):");
            String id = io.scan();
            if(id.isEmpty()){
                lobbyRemoteInterface = stub.joinRandomLobby(clientRMI); //join first available lobby, otherwise creates one
                lobbySelected = true;
            } else if(inputSanitizer.isInteger(id)){
                lobbyID = Integer.parseInt(id);
                lobbyRemoteInterface = stub.joinSelectedLobby(clientRMI, lobbyID);
                if(lobbyRemoteInterface == null)
                    io.printErrorMessage("Input id not found");
                else lobbySelected = true;
            }
            else{
                io.printErrorMessage("Input a valid id (must be a number)");
            }
        }
        io.printMessage("You joined #"+ lobbyRemoteInterface.getID()+" lobby!");
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