package it.polimi.ingsw.client.controller.cli;


import it.polimi.ingsw.client.Client_Settings;
import it.polimi.ingsw.client.connection.*;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.model.ClientModelCLI;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;
import it.polimi.ingsw.server.clientonserver.ClientSocket;
import it.polimi.ingsw.shared.ChatMessage;
import it.polimi.ingsw.shared.model.Move;
import it.polimi.ingsw.shared.NetworkSettings;
import it.polimi.ingsw.shared.PrivateChatMessage;

import java.rmi.RemoteException;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * This class handles the sequence of events on the client side
 */
public class ClientControllerCLI implements ClientController {
    /**
     * The player name
     */
    private String playerName;
    //Objects that handle connection with server
    private Server server;
    private Client client;
    private ClientModel model;
    /**
     * UI View
     */
    private CLI_IO cliIO;

    /**
     * Join the lobby by creating a Lobby connection object and connecting it to server
     */
    private void joinLobby() throws ServerException {
        Map<Integer, Integer> availableLobbies = server.getAvailableLobbies();

        // Checking if we were previously disconnected from a lobby
        final int previousLobbyId = server.disconnectedFromLobby(playerName);
        if(previousLobbyId >= 0) {
            server.joinSelectedLobby(client, previousLobbyId);  // Automatically joining the lobby
            try {
                final boolean isLobbyAdmin = server.isLobbyAdmin(playerName);

                cliIO.message("You joined automatically #"+ previousLobbyId +" lobby!\nYou were previously connected to it");
                cliIO.setLobbyAdmin(isLobbyAdmin);

                return;
            } catch (LobbyException e) {
                cliIO.errorMessage("Error while connecting automatically to lobby");
            }
        }

        //show the client the lobbies they can join
        cliIO.showLobbies(server.getJoinedLobbies(playerName),"The lobbies you already joined");
        cliIO.showLobbies(availableLobbies, "The lobbies that are available");

        boolean successful = false;
        while(!successful){
            //ask the user
            LobbySelectionCommand command = cliIO.askLobby();
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
                    cliIO.showLobbies(server.getJoinedLobbies(playerName),"The lobbies you already joined");
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
        }
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
                    start();
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
        try {
            cliIO.showAllMessages(model.getChat());
        } catch (RemoteException e) {
            cliIO.errorMessage("Error while loading resourses");
        }
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
        try {
            if (model.isItMyTurn()) {
                try {
                    Move move = cliIO.getMoveFromUser(model.getBoard(), model.getPlayersShelves().get(playerName));
                    server.postMove(playerName, move);
                }
                catch (RemoteException e){
                    cliIO.errorMessage("Error while loading resources");
                }
            } else {
                cliIO.errorMessage("It's not your turn");
            }
        } catch (RemoteException ignored) {
            // Never thrown
        }
    }

    private void start(){
        boolean admin = false;
        boolean started = false;
        try {
            admin = server.isLobbyAdmin(playerName);
            if(!admin){
                cliIO.errorMessage("You are not lobby admin");
                return;
            }
            started = server.startGame(playerName);
        } catch (LobbyException e) {
            started = false;
        }
        if(!started){
            cliIO.errorMessage("You can not start lobby now");
        }
    }


    /**
     * Initiate all the objects that will handle the connection to serer
     */
    private void initConnectionInterface() throws ServerException{
        switch (Client_Settings.connection){
            case RMI:
                server = new ServerRMI(NetworkSettings.serverIp, NetworkSettings.RMIport);
                try {
                    client = new ClientRMI(model);
                } catch (RemoteException e) {
                    throw new ServerException("Impossible to create RMI client object");
                }
                break;
            case TCP:
                server = new ServerTCP(NetworkSettings.serverIp, NetworkSettings.TCPport, model);
                client = new ClientSocket();
                ((ClientSocket) client).setName(playerName);
                break;
            case STUB:
                server = new ConnectionStub();
                try {
                    ClientModelCLI remoteObject = new ClientModelCLI(playerName, new CLI_IO());
                    client = new ClientRMI(remoteObject); //TODO create stub when completed the real one
                } catch (RemoteException e) {
                    throw new ServerException("Impossible to create RMI client object");
                }
        }
    }

    /**
     * Initiate the connection interface and attempt a login
     * @return true if login was successful
     */
    private boolean connect() {
        try{
            //Initiate the server connection interfaces according to settings
            tryConnect(10,1);
            //login
            return tryLogin(3,2);
        } catch (ServerException e) {
            return false;
        }
    }
    /**
     * Try login tries times
     * @param tries available to connect
     * @param seconds to wait in case of failure
     */
    private void tryConnect(int tries, int seconds) throws ServerException {
        try {
            initConnectionInterface();
        } catch (ServerException e) {
            cliIO.errorMessage("Connection Error, retying in "+seconds+" seconds");
            try {
                sleep(seconds * 1000);
            } catch (InterruptedException i) {
                return;
            }
            if (tries > 1)
                tryConnect(tries - 1, seconds);
            else throw new ServerException("Can't connect to the server");
        }
    }


    /**
     * Try login tries times
     * @param tries available to login
     * @param seconds to wait in case of failure
     * @return true if successful
     */
    private boolean tryLogin(int tries, int seconds){
        boolean logged = false;
        for(int attempt = 0; attempt < tries && !logged; attempt++){
            logged = server.login(client); //get previous sessions if present
            if(!logged){
                cliIO.errorMessage("Connection Error, retying in "+seconds+" seconds");
                try {
                    sleep(seconds*1000);
                } catch (InterruptedException e) {
                    return false;
                }
            }
        }
        return logged;
    }

    /**
     * Play a match in the lobby
     */
    private void playMatch() throws LobbyException {
        //Receive and execute commands until "exit" lobbyCommand is launched
        while(!exit){

            try {
                if (model !=null && model.gameEnded()) return;
            } catch (RemoteException ignored) {
            }

            LobbyCommand lobbyCommand = cliIO.askCommand();
            executeUserCommand(lobbyCommand);
        }
    }
    /**
     * True when the client wants to keep playing
     */
    boolean play = true;
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
        boolean successfulLogin = connect();

        while(play){
            if(successfulLogin){
                try{
                    //ask the client what lobby to join
                    joinLobby();

                    //game starts
                    playMatch();

                } catch (ServerException | LobbyException e) {
                    cliIO.errorMessage("Something went wrong connecting to server");
                    play = false;
                }
                //ask the player if they want to play again
                play = cliIO.playAgain();
            }
            else{
                cliIO.errorMessage("It was impossible to connect to server");
                play = false;
            }
        }
    }
}