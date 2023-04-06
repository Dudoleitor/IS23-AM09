package it.polimi.ingsw.RMI;

import it.polimi.ingsw.server.Move;
import it.polimi.ingsw.server.PartialMove;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.Shelf;

import java.rmi.RemoteException;

public class Match extends ClientThread{
    //TODO add view and local copies of Model
    Match(String playerName, LobbyRemoteInterface stub) {
        super(playerName, stub);
    }

    @Override
    protected void executeUserCommand(String command) throws Exception {
        //Invalid command
        if(!inputSanitizer.isValidMessage(command)){
            io.printErrorMessage("Invalid format");
            return;
        }
        //execute action for every command
        switch (command.toLowerCase()){
            case("exit"): //quit game
                io.printMessage("You quit");
                exit = true;
                break;
            case("start"):
                stub.startGame(playerName);
                break;
            case("move"):
                Move move = createMove();
                stub.postMove(playerName,move);
                break;
            case("peek"):
                peekElement();
                break;
            default:
                io.printErrorMessage("Please enter valid command");
                break;
        }
    }
    private Move createMove(){
        //TODO implement
        return null;
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
                if(!inputSanitizer.isValidMessage(choice)){
                    io.printErrorMessage("Please enter valid command");
                    return;
                }
                //TODO show player's shelf
                break;
        }
    }
    @Override
    public void run(){
        io.printMessage("Make your moves!"); //introduction message after login
        loopCommands();
    }

}
