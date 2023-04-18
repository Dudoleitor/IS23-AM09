package it.polimi.ingsw.client.LobbySelection;

import it.polimi.ingsw.client.InputSanitizer;
import it.polimi.ingsw.client.cli_IO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LobbySelectionCLI extends LobbySelectionView {
    private cli_IO io = new cli_IO();
    private InputSanitizer inputSanitizer = new InputSanitizer();
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
    public void showLobbies(Map<Integer,Integer> availableLobbies, String description){
        io.printMessage(description);
        if (!(availableLobbies == null) && !availableLobbies.isEmpty()) {
            String lobbyMessage = "";
            List<String> lobbyList = (List<String>)
                    availableLobbies.keySet().stream().
                            map(x -> "   --> Lobby " + x + ":  " + availableLobbies.get(x) + " players in\n").
                            collect(Collectors.toList());
            for(String mes : lobbyList){
                lobbyMessage = lobbyMessage.concat(mes);
            }
            io.printMessage(lobbyMessage);
        } else {
            io.printMessage("None");
        }
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
        message("Do you want to play again?");
        answer = io.scan();
        if(answer.equals("yes") || answer.equals("y")){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void errorMessage(String message) {
        io.printErrorMessage(message);
    }

    @Override
    public void message(String message) {
        io.printMessage(message);
    }

    @Override
    public void greet(String playerName) {
        io.printMessage("Hello "+playerName+"!");
    }
}
