package it.polimi.ingsw.client.Lobby;

import it.polimi.ingsw.client.Command;
import it.polimi.ingsw.client.InputSanitizer;
import it.polimi.ingsw.client.cli_IO;
import it.polimi.ingsw.shared.Move;
import it.polimi.ingsw.shared.PartialMove;
import it.polimi.ingsw.shared.*;

import java.util.*;
import java.util.stream.Collectors;

public class MatchCLI extends MatchView {
    private static cli_IO io = new cli_IO();
    private InputSanitizer inputSanitizer = new InputSanitizer();
    private boolean exit = false;

    @Override
    protected Command askCommand(){
        String input;
        input = io.scan();
        //Invalid command
        if(!inputSanitizer.isValidMessage(input)){
            io.printErrorMessage("Invalid format");
            return Command.Invalid;
        }
        else{
            return Command.stringToCommand(input);
        }
    }

    @Override
    protected void notifyExit() {
        io.printErrorMessage("You quit!");
    }

    @Override
    public void greet(String playerName) {
        io.printMessage("Hello "+playerName+"!");
    }
    /**
     * Print all messages in local copy of chat. If none is present "No message yet" will be printed
     */
    public void printAllMessages(Chat chat){
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

    protected Move getMoveFromUser(){
        boolean validInput = false;List<Position> positions = null;
        int column = 0;
        PartialMove pm = new PartialMove();
        while(!validInput){
            validInput = true;
            io.printMessage("Write your move");
            List<String> fields = new ArrayList<>();
            fields.add("first position");
            fields.add("second position");
            fields.add("third position");
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
        positions.add(Position.fromString(input.get("second position")));
        positions.add(Position.fromString(input.get("third position")));
        return positions;
    }

    protected void showElement(){
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
    protected void notifyInvalidCommand(){
        io.printErrorMessage("Invalid Command!");
    }
}