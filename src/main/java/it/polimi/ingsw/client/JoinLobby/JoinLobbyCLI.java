package it.polimi.ingsw.client.JoinLobby;

import it.polimi.ingsw.client.InputSanitizer;
import it.polimi.ingsw.client.Lobby.LobbyCLI;
import it.polimi.ingsw.client.Lobby.LobbyStub;
import it.polimi.ingsw.client.cli_IO;
import it.polimi.ingsw.server.clientonserver.Client;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JoinLobbyCLI extends JoinLobbyUI{
    private cli_IO io = new cli_IO();
    private InputSanitizer inputSanitizer = new InputSanitizer();
    private Client client;

    public JoinLobbyCLI(ServerStub server) {
        super(server);
    }

    @Override
    public void run(){
        setUserName();
        client = server.generateClient(playerName);
        tryLogin(client,30);
        showLobbyList();
        showJoinedLobbies();
        joinLobby(client);
    }

    @Override
    public void setUserName(){
        while(!inputSanitizer.isValidName(playerName)){
            io.printMessage("Enter your username");
            playerName = io.scan();
            if(!inputSanitizer.isValidName(playerName)){
                io.printErrorMessage("Please enter a valid name");
            }
        }
    }
    @Override
    public boolean tryLogin(Client client,int tries){
        boolean logged = false;
        //Try to log in for 30s (1 try each second)
        for(int attempt = 0; attempt < tries && !logged; tries++){
            logged = server.login(client); //get previous sessions if present
            if(logged) {
                showJoinedLobbies();
            }
            else{
                io.printErrorMessage("Connection Error, retying in 1 second");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    return false;
                }
            }
        }
        return logged;
    }

    public void showJoinedLobbies(){
        List<Integer> previousSessions = server.getJoinedLobbies(playerName);
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
    }

    public void joinLobby(Client client){
        boolean lobbySelected = false;
        int lobbyID;
        LobbyStub lobbyStub = null;
        while(!lobbySelected){
            io.printMessage("Choose a Lobby (ENTER for random):");
            String id = io.scan();
            if(id.isEmpty()){
                lobbyStub = server.joinRandomLobby(client); //join first available lobby, otherwise creates one
                lobbySelected = true;
            } else if(inputSanitizer.isInteger(id)){
                lobbyID = Integer.parseInt(id);
                lobbyStub = server.joinSelectedLobby(client, lobbyID);
                if(lobbyStub == null)
                    io.printErrorMessage("Input id not found");
                else lobbySelected = true;
            }
            else{
                io.printErrorMessage("Input a valid id (must be a number)");
            }
        }
        io.printMessage("You joined #"+ lobbyStub.getID()+" lobby!");

        //Create the lobbyUI object and start the match
        LobbyCLI lobbyCLI = new LobbyCLI(playerName,lobbyStub);
        lobbyCLI.run();
    }

    public void showLobbyList(){
        Map availableLobbies = server.showAvailableLobbbies();
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
    }
}
