package it.polimi.ingsw.client.Lobby;

import it.polimi.ingsw.client.Command;
import it.polimi.ingsw.client.InputSanitizer;
import it.polimi.ingsw.client.cli_IO;
import it.polimi.ingsw.shared.InvalidMoveException;
import it.polimi.ingsw.shared.Move;
import it.polimi.ingsw.shared.PartialMove;
import it.polimi.ingsw.shared.*;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

public class LobbyCLI extends LobbyUI {
    private static cli_IO io = new cli_IO();
    private InputSanitizer inputSanitizer = new InputSanitizer();
    private boolean exit = false;
    //private KeepAlive keepAlive;

    public LobbyCLI(String playerName, LobbyStub stub){
        super(playerName,stub);
        //this.keepAlive = new KeepAlive(playerName,stub);
        //keepAlive.start();
    }
    protected void loopCommands(){
        String command;
        while(!exit){ //Receive commands until "exit" command is launched
            try{
                command = io.scan();
                //Invalid command
                if(!inputSanitizer.isValidMessage(command)){
                    io.printErrorMessage("Invalid format");
                }
                else{
                    Command toExecute = Command.stringToCommand(command);
                    executeUserCommand(toExecute);
                }
            }
            catch (RemoteException e){
                io.printErrorMessage("Error in RMI");
            } catch (Exception e) {
                e.printStackTrace();
                io.printErrorMessage("Error in Message Format");
            }
        }
    }
    @Override
    protected void executeUserCommand(Command com) throws Exception {
        //execute action for every command
        switch (com){
            case Exit: //quit game
                io.printMessage("You quit");
                //keepAlive.interrupt();
                exit = true;
                break;
            case Print: //print all messages
                updateLiveChat();
                printAllMessages();
                break;
            case Secret: //send private message
                postToPrivateChat();
                break;
            case Start:
                lobby.startGame(playerName);
                break;
            case Move:
                Move move = createMove();
                lobby.postMove(playerName,move);
                break;
            case Peek:
                peekElement();
                break;
            case Message:
                postToChat();
                break;
            default: //post message to chat
                io.printErrorMessage("Invalid Command");
        }
    }

    /**
     * Downloads all the messages that are present on server and missing in local copy
     * @throws RemoteException
     */
    public void updateLiveChat(){
        chat = lobby.updateLiveChat();
    }

    /**
     * Print all messages in local copy of chat. If none is present "No message yet" will be printed
     */
    public void printAllMessages(){
        if(chat == null || chat.size() == 0){
            io.printMessage("No messages yet");
            return;
        }
        List<String> toPrint = chat.getAllMessages().stream().map(mes -> mes.toString()).collect(Collectors.toList());
        toPrint.add(0, io.messageFormat("Here are all messages"));
        io.multiPrint(toPrint);
    }

    public static boolean checkValidReceiver(ChatMessage message, String receiverName){
        if (message.getClass().equals(PrivateChatMessage.class)){
            if(!((PrivateChatMessage) message).getReciever().equals(receiverName))
                return false;
        }
        return true;
    }
    public void postToChat(){
        List<String> fields = new ArrayList<>();
        fields.add("message");
        Map<String,String> header = io.multiScan(fields);
        lobby.postToLiveChat(
                playerName,
                header.get("message"));
    }
    public void postToPrivateChat(){
        List<String> fields = new ArrayList<>();
        fields.add("receiver");
        fields.add("message");
        Map<String,String> header = io.multiScan(fields);
        lobby.postSecretToLiveChat(
                playerName,
                header.get("receiver"),
                header.get("message"));
    }

    private Move createMove() throws InvalidMoveException {
        io.printMessage("Write your move");
        List<String> fields = new ArrayList<>();
        fields.add("first position");
        fields.add("second position");
        fields.add("third position");
        fields.add("column");
        Map<String,String> input = io.multiScan(fields);

        List<Position> positions = getPositionsFromInput(input);
        int column;
        try{
            column = Integer.valueOf(input.get("column"));
        }
        catch (Exception e){
            throw new InvalidMoveException("Bad Input for move");
        }

        if(positions.contains(null)){
            throw new InvalidMoveException("Bad Input for move");
        }

        PartialMove pm = new PartialMove();
        for(Position p : positions){
            pm.addPosition(p);
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

    private void peekElement(){
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
    @Override
    public void run(){
        io.printMessage("Make your moves! Tell your unrequested opinions!"); //introduction message after login
        loopCommands();
    }
}
