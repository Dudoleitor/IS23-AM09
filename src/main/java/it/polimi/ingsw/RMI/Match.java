package it.polimi.ingsw.RMI;

import it.polimi.ingsw.server.Move;
import it.polimi.ingsw.server.PartialMove;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.Shelf;

import java.rmi.RemoteException;

public class Match extends ClientThread{
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
                try {
                    io.printMessage("Board:\n"+stub.peekBoard());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case("shelf"):
                io.printMessage("Select the player:");
                String player = io.scan();
                if(!inputSanitizer.isValidMessage(choice)){
                    io.printErrorMessage("Please enter valid command");
                    return;
                }
                try {
                    String shelf = stub.peekPlayerShelf(player);
                    if(shelf == null){
                        io.printErrorMessage("Player not found");
                    }
                    else{
                        io.printMessage("Shelf:\n"+shelf);
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
        }
    }
    @Override
    public void run(){
        io.printMessage("Make your moves!"); //introduction message after login
        loopCommands();
    }

}
