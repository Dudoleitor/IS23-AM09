package it.polimi.ingsw.client.controller.cli;


import it.polimi.ingsw.client.connection.*;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.model.ClientModelCLI;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.ChatMessage;
import it.polimi.ingsw.shared.model.Move;
import it.polimi.ingsw.shared.PrivateChatMessage;

import java.rmi.RemoteException;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * This class handles the sequence of events on the client side.
 */
public class ClientControllerCLI implements ClientController {
    /**
     * The player name
     */
    private String playerName;
    //Objects that handle connection with server
    private Server server;
    private Client client;
    private ClientModelCLI model;
    /**
     * UI View
     */
    private CLI_IO cliIO;


    public ClientControllerCLI() {
    }

    public Server getServer() {
        return server;
    }
    public void setServer(Server server) {
        this.server = server;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public ClientModelCLI getModel() {
        return model;
    }
    public boolean gameIsStarted() {
        return model.gameIsStarted();
    }

    /**
     * Join the lobby by creating a Lobby connection object and connecting it to server
     */
    private void joinLobby() throws ServerException {
        Map<Integer, Integer> availableLobbies = server.getAvailableLobbies();

        // Checking if we were previously disconnected from a lobby
        final int previousLobbyId = server.disconnectedFromLobby(playerName);
        if(previousLobbyId >= 0) {
            joinPreviousLobby(previousLobbyId);
        }
        else{
            //show the client the lobbies they can join
            cliIO.showJoinedLobby(server.getJoinedLobby(playerName),"The lobby you already joined");
            cliIO.showLobbies(availableLobbies, "The lobbies that are available");

            boolean successful = false;
            while(!successful){
                //ask the user
                LobbySelectionCommand command = cliIO.askLobby();
                successful = executeLobbySelectionCommand(command);
            }
        }
    }

    private void joinPreviousLobby(int previousLobbyId){
        try {
            server.joinSelectedLobby(client, previousLobbyId);  // Automatically joining the lobby
            final boolean isLobbyAdmin = server.isLobbyAdmin(playerName);

            cliIO.message("You joined automatically #" + previousLobbyId + " lobby!\nYou were previously connected to it");
            cliIO.setLobbyAdmin(isLobbyAdmin);

        } catch (LobbyException e) {
            throw new RuntimeException("Error while connecting automatically to lobby");
        } catch (ServerException e) {
            cliIO.message("Error while connecting automatically to lobby");
        }
    }

    private boolean executeLobbySelectionCommand(LobbySelectionCommand command) throws ServerException {
        boolean successful = false;
        switch (command){
            case Random:
                server.joinRandomLobby(client);
                break;
            case Number:
                server.joinSelectedLobby(client,command.getID());
                break;
            case Create:
                server.createLobby(client);
                break;
            case Refresh:
                //show the client the lobbies they can join
                cliIO.showJoinedLobby(server.getJoinedLobby(playerName),"The lobby you already joined");
                cliIO.showLobbies(server.getAvailableLobbies(), "The lobbies that are available");
                break;
            default:
                cliIO.notifyInvalidCommand();
                break;
        }

        if(command.isValid()){
            try{
                final int joinedLobbyId = server.getLobbyID();
                final boolean isLobbyAdmin = server.isLobbyAdmin(playerName);

                cliIO.message("You joined #"+ joinedLobbyId +" lobby!");
                cliIO.setLobbyAdmin(isLobbyAdmin);
                successful = true;
            }
            catch (LobbyException e){
                cliIO.errorMessage("Lobby does not exist");
            }
        }
        return successful;
    }

    /**
     * Execute a lobbyCommand received from view
     * @param lobbyCommand the lobbyCommand to execute
     */
    private void executeUserCommand(LobbyCommand lobbyCommand) throws LobbyException {
        //execute action for every lobbyCommand
            switch (lobbyCommand) {
                case Exit: //quit game
                    server.quitGame(playerName);
                    cliIO.notifyExit();
                    exit = true;
                    break;
                case Print: //print all messages
                    printChat();
                    break;
                case Secret: //send private message
                    postToPrivateChat();
                    break;
                case Start:
                    boolean reloadOldGame = cliIO.askToLoadOldMatch();
                    ClientController.start(this,reloadOldGame);
                    break;
                case Move:
                    postMove();
                    break;
                case Message:
                    postToChat();
                    break;
                case Help:
                    cliIO.showHelp();
                    break;
                default: //post message to chat
                    cliIO.notifyInvalidCommand();
                    break;
            }
    }

    /**
     * This will print the whole chat into the cli
     */
    private void printChat(){
        cliIO.showAllMessages(model.getChat());
    }

    /**
     * Check if the reciever of private message is valid
     * @param message the message
     * @param receiverName the receiver name
     * @return true if valid
     */
    private boolean checkValidReceiver(ChatMessage message, String receiverName){
        if (message.getClass().equals(PrivateChatMessage.class)){
            if(!((PrivateChatMessage) message).getReciever().equals(receiverName))
                return false;
        }
        return true;
    }

    /**
     * Get message from user and post to Server Chat
     */
    private void postToChat() throws LobbyException {
        Map<String,String> message = cliIO.getMessageFromUser();
        server.postToLiveChat(
                playerName,
                message.get("message"));
    }

    /**
     * Get private message from user and post to Server Chat
     */
    private void postToPrivateChat() throws LobbyException {
        Map<String,String> privateMessage = cliIO.getPrivateMessageFromUser();
        server.postSecretToLiveChat(
                playerName,
                privateMessage.get("receiver"),
                privateMessage.get("message"));
    }

    /**
     * Get move from user and post to server
     */
    private void postMove() throws LobbyException {
        if(!server.matchHasStarted()){
            cliIO.errorMessage("Match has not started");
            return;
        }
        if (model.isItMyTurn()) {
            Move move = cliIO.getMoveFromUser(model.getBoard(), model.getPlayersShelves().get(playerName));
            server.postMove(playerName, move);
        } else {
            cliIO.errorMessage("It's not your turn");
        }
    }

    /**
     * Play a match in the lobby
     */
    private void playMatch() throws LobbyException {
        //Receive and execute commands until "exit" lobbyCommand is launched
        while(!exit){
            try {
                if (model !=null && model.gameEnded()) return;
            } catch (RemoteException ignored) {} //TODO handle somehow

            LobbyCommand lobbyCommand = cliIO.askCommand();
            executeUserCommand(lobbyCommand);
        }
    }
    /**
     * True when the match has entered or the client has quit
     */
    boolean exit = false;

    /**
     * the main method that handles the client side application logic
     */
    public void startClient(){

        //Initiate the view
        cliIO = new CLI_IO();

        //ask for username
        playerName = cliIO.askUserName();

        try {
            model = new ClientModelCLI(playerName, cliIO);
        } catch (RemoteException ignored) {
        }

        //initiate the connection interface and attempt a login
        boolean successfulLogin = ClientController.connect(this, model);

        if(!successfulLogin)
            cliIO.errorMessage("It was impossible to connect to the server");

        if (successfulLogin) {
            model.setServer(server);
            try {
                //ask the client what lobby to join
                joinLobby();

                //game starts
                playMatch();

            } catch (ServerException | LobbyException e) {
                cliIO.errorMessage("Something went wrong connecting to server");
            }
        }
    }
    public void errorMessage(String msg) {
        cliIO.errorMessage(msg);
    }
}