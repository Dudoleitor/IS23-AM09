package it.polimi.ingsw.client.JoinLobby;

import it.polimi.ingsw.client.InputSanitizer;
import it.polimi.ingsw.client.Lobby.Lobby;
import it.polimi.ingsw.client.Lobby.LobbyCLI;
import it.polimi.ingsw.client.Lobby.LobbyUI;
import it.polimi.ingsw.client.cli_IO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JoinLobbyCLI extends JoinLobbyUI{
    private cli_IO io = new cli_IO();
    private InputSanitizer inputSanitizer = new InputSanitizer();

    public JoinLobbyCLI(Server server) {
        super(server);
    }
    @Override
    public String askUserName(){
        String name = "";
        while(!inputSanitizer.isValidName(name)){
            io.printMessage("Enter your username");
            name = io.scan();
            if(!inputSanitizer.isValidName(name)){
                io.printErrorMessage("Please enter a valid name");
            }
        }
        return name;
    }
    @Override
    public boolean tryLogin(int tries){
        boolean logged = false;
        //Try to log in for 30s (1 try each second)
        for(int attempt = 0; attempt < tries && !logged; tries++){
            logged = login(); //get previous sessions if present
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
    @Override
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
    @Override
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
    @Override
    public LobbyUI getLobbyUI(){
        LobbySelectionCommand command = LobbySelectionCommand.Invalid;
        Lobby lobby = null;
        while(command == LobbySelectionCommand.Invalid){
            command = askLobby();
            lobby = joinLobby(command);
            if(command == LobbySelectionCommand.Invalid){
                io.printErrorMessage("Input a valid id (must be a number)");
            }
        }
        io.printMessage("You joined #"+ lobby.getID()+" lobby!");
        //Create the lobbyUI object and start the match
        return new LobbyCLI(playerName,lobby);
    }
    @Override
    public LobbySelectionCommand askLobby(){
        int lobbyID;
        LobbySelectionCommand command;
        io.printMessage("Choose a Lobby (ENTER for random):");
        String id = io.scan();
        if(id.isEmpty()){
            return LobbySelectionCommand.Random;
        } else if(inputSanitizer.isInteger(id)){
            lobbyID = Integer.parseInt(id);
            command = LobbySelectionCommand.Number;
            command.setId(lobbyID);
        }
        else{
            command = LobbySelectionCommand.Invalid;
            io.printErrorMessage("Input a valid id (must be a number)");
        }
        return command;
    }
    @Override
    public boolean playAgain(){
        String answer;
        io.printMessage("Do you want to play again?");
        answer = io.scan();
        if(answer.equals("yes") || answer.equals("y")){
            return true;
        }
        else {
            return false;
        }
    }
}
