package it.polimi.ingsw.client.View.cli;

import it.polimi.ingsw.client.Client_Settings;
import it.polimi.ingsw.client.View.LobbyCommand;
import it.polimi.ingsw.client.View.View;
import it.polimi.ingsw.client.View.cli.InputSanitizer;
import it.polimi.ingsw.client.View.LobbySelectionCommand;
import it.polimi.ingsw.client.View.cli.cli_IO;
import it.polimi.ingsw.client.controller.ClientControllerCLI;
import it.polimi.ingsw.shared.Move;
import it.polimi.ingsw.shared.PartialMove;
import it.polimi.ingsw.shared.*;

import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.client.Client_Settings.*;

public class CLI extends View {
    private static cli_IO io = new cli_IO();
    private InputSanitizer inputSanitizer = new InputSanitizer();

    @Override
    public LobbyCommand askCommand(){
        String input;
        input = io.scan();
        //Invalid command
        if(!inputSanitizer.isValidMessage(input)){
            io.printErrorMessage("Invalid format");
            return LobbyCommand.Invalid;
        }
        else{
            return LobbyCommand.stringToCommand(input);
        }
    }

    @Override
    public void notifyExit() {
        io.printErrorMessage("You quit!");
    }

    /**
     * Print all messages in local copy of chat. If none is present "No message yet" will be printed
     */
    @Override
    public void showAllMessages(Chat chat){
        if(chat == null || chat.size() == 0){
            io.printMessage("No messages yet");
            return;
        }
        List<String> toPrint = chat.getAllMessages().stream().map(mes -> mes.toString()).collect(Collectors.toList());
        toPrint.add(0, io.messageFormat("Here are all messages"));
        io.multiPrint(toPrint);
    }
    public Map<String,String> getMessageFromUser(){
        List<String> fields = new ArrayList<>();
        fields.add("message");
        return io.multiScan(fields);
    }

    public Map<String,String> getPrivateMessageFromUser(){
        List<String> fields = new ArrayList<>();
        fields.add("receiver");
        fields.add("message");
        return io.multiScan(fields);
    }

    public Move getMoveFromUser(){
        boolean validInput = false;List<Position> positions = null;
        int column = 0;
        PartialMove pm = new PartialMove();
        while(!validInput){
            validInput = true;
            io.printMessage("Write your move");
            List<String> fields = new ArrayList<>();
            fields.add("first position");
            //fields.add("second position");
            //fields.add("third position");
            fields.add("column");
            Map<String,String> input = io.multiScan(fields);
            try{
                positions = getPositionsFromInput(input);
                column = Integer.valueOf(input.get("column"));
                if(positions == null ||  positions.contains(null)){
                    throw new Exception();
                }
                for(Position p : positions){
                    pm.addPosition(p);
                }
            }
            catch (Exception e){
                io.printErrorMessage("Invalid format");
                validInput = false;
            }
        }
        return new Move(pm, column);
    }

    private List<Position> getPositionsFromInput(Map<String,String> input){
        List<Position> positions = new ArrayList<>();
        positions.add(Position.fromString(input.get("first position")));
        //positions.add(Position.fromString(input.get("second position")));
        //positions.add(Position.fromString(input.get("third position")));
        return positions;
    }

    public void showElement(){
        io.printMessage("What do you want to see (Board/Shelf)");
        String choice = io.scan();
        if(!inputSanitizer.isValidMessage(choice)){
            io.printErrorMessage("Please enter valid command");
            return;
        }
        switch (choice.toLowerCase()){
            case("board"):
                //TODO show board
                break;
            case("shelf"):
                io.printMessage("Select the player:");
                String player = io.scan();
                if(!inputSanitizer.isValidName(choice)){
                    io.printErrorMessage("Please enter valid command");
                    return;
                }
                //TODO show player's shelf
                break;
            default:
                io.printErrorMessage("Not valid element");
        }
    }

    public void showBoard(Board b) {
        io.printMessage(b.toString());
    }

    public void showShelves(Map<String, Shelf> playerShelves) {
        for (String player : playerShelves.keySet()) {
            io.printMessage(player);
            io.printMessage(playerShelves.get(player).toString());
        }
    }

    public void showChatMessage(String sender, String message) {
        io.printMessage(sender + " sent a chat message: " + message);
    }

    @Override
    public void showHelp() {
        String help = "Here are all the commands:\n";
        List<String> commandList = Arrays.stream(LobbyCommand.values()).
                filter(c -> c != LobbyCommand.Help && c != LobbyCommand.Invalid).
                map(c -> "    -> " + c.getCode().toUpperCase() + " ["+c.getShortcut()+"] :"+c.getDescription()+"\n").
                collect(Collectors.toList());
        for(String command : commandList){
            help = help.concat(command);
        }
        io.printMessage(help);
    }

    public void notifyInvalidCommand(){
        io.printErrorMessage("Invalid LobbyCommand!");
    }
    @Override
    public String askUserName(){
        System.out.println(gameLogo);
        String name = "";
        while(!inputSanitizer.isValidName(name)){
            io.printMessage("Enter your username");
            name = io.scan();
            if(!inputSanitizer.isValidName(name)){
                io.printErrorMessage("Please enter a valid name");
            }
        }
        io.printMessage("Hello "+name+"!");
        return name;
    }
    @Override
    public void showLobbies(Map<Integer,Integer> availableLobbies, String description){
        String lobbyMessage = description;
        if (!(availableLobbies == null) && !availableLobbies.isEmpty()) {
            List<String> lobbyList = (List<String>)
                    availableLobbies.keySet().stream().
                            map(x -> "\n    -> Lobby " + x + ":  " + availableLobbies.get(x) + " players in").
                            collect(Collectors.toList());
            for(String mes : lobbyList){
                lobbyMessage = lobbyMessage.concat(mes);
            }
        } else {
            lobbyMessage = lobbyMessage + "\n       None";
        }
        io.printMessage(lobbyMessage);
    }
    @Override
    public LobbySelectionCommand askLobby(){
        int lobbyID;
        LobbySelectionCommand command;
        io.printMessage("Choose a Lobby (ENTER for random, NEW to create a new one):");
        String id = io.scan();
        if(id.isEmpty()){
            return LobbySelectionCommand.Random;
        } else if (id.toLowerCase().equals("new")) {
            return LobbySelectionCommand.Create;
        } else if(inputSanitizer.isInteger(id)){
            lobbyID = Integer.parseInt(id);
            command = LobbySelectionCommand.Number;
            command.setId(lobbyID);
        }
        else{
            command = LobbySelectionCommand.Invalid;
            io.printErrorMessage("Input a valid id or command");
        }
        return command;
    }
    @Override
    public boolean playAgain(){
        String answer;
        message("Do you want to play again?");
        answer = io.scan();
        if(answer.toLowerCase().equals("yes") || answer.toLowerCase().equals("y")){
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
    public void setLobbyAdmin(boolean isAdmin){
        if(isAdmin){
            message("You are the lobby admin! (we got you a crown)");
            io.setPlaceHolder(Client_Settings.adminPlaceHolder);
        }
        else{
            io.setPlaceHolder(Client_Settings.stdPlaceHolder);
        }
    }

}