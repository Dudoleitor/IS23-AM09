package it.polimi.ingsw.client;


import it.polimi.ingsw.client.Connection.*;
import it.polimi.ingsw.client.View.LobbyCommand;
import it.polimi.ingsw.client.View.LobbySelectionCommand;
import it.polimi.ingsw.client.View.View;
import it.polimi.ingsw.client.View.ViewDriver;
import it.polimi.ingsw.client.View.cli.CLI;
import it.polimi.ingsw.client.controller.ClientControllerCLI;
import it.polimi.ingsw.client.View.gui.HelloApplication;
import it.polimi.ingsw.client.View.gui.GUI;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;
import it.polimi.ingsw.server.clientonserver.ClientSocket;
import it.polimi.ingsw.shared.ChatMessage;
import it.polimi.ingsw.shared.Move;
import it.polimi.ingsw.shared.NetworkSettings;
import it.polimi.ingsw.shared.PrivateChatMessage;

import java.rmi.RemoteException;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * This class handles the sequence of events on the client side
 */
public class ClientMain{
    /**
     * The player name
     */
    static String playerName;
    //Objects that handle connection with server
    static Server server;
    static Client client;
    /**
     * UI View
     */
    static View view;

    /**
     * Get a List of the lobby IDs where the player is in
     * @return the list of lobby IDs
     */
    private static Map<Integer,Integer> getPreviousSessions() throws ServerException {
        return server.getJoinedLobbies(playerName);
    }

    /**
     * Get all the lobbies that are available for the client to join
     * @return a map of the lobby IDs to the Number of player present
     */
    private static Map<Integer,Integer> getAvailableLobbies() throws ServerException {
        return server.getAvailableLobbies();
    }

    /**
     * Join the lobby by creating a Lobby connection object and connecting it to server
     */
    private static void joinLobby() throws ServerException {
        //show the client the lobbies they can join
        view.showLobbies(getPreviousSessions(),"The lobbies you already joined");
        view.showLobbies(getAvailableLobbies(), "The lobbies that are available");
        //ask the user
        LobbySelectionCommand command = LobbySelectionCommand.Invalid;
        while(command == LobbySelectionCommand.Invalid){
            command = view.askLobby();
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
                default:
                    view.errorMessage("Input a valid id");
                    break;
            }
            try{
                view.message("You joined #"+ server.getLobbyID()+" lobby!");
                view.setLobbyAdmin(server.isLobbyAdmin(playerName));
            }
            catch (LobbyException e){
                view.errorMessage("Lobby does not exist");
                command = LobbySelectionCommand.Invalid;
            }
        }
    }

    /**
     * Execute a lobbyCommand received from view
     * @param lobbyCommand the lobbyCommand to execute
     */
    private static void executeUserCommand(LobbyCommand lobbyCommand) throws LobbyException {
        //execute action for every lobbyCommand
            switch (lobbyCommand) {
                case Exit: //quit game
                    server.quitGame(playerName);
                    view.notifyExit();
                    exit = true;
                    break;
                case Print: //print all messages
                    //view.showAllMessages(chat);
                    // TODO Here we should use ClientControllerCLI
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
                case Peek:
                    view.showElement();
                    break;
                case Message:
                    postToChat();
                    break;
                case Help:
                    view.showHelp();
                    break;
                default: //post message to chat
                    view.notifyInvalidCommand();
                    break;
            }
    }

    /**
     * Check if the reciever of private message is valid
     * @param message the message
     * @param receiverName the receiver name
     * @return true if valid
     */
    private static boolean checkValidReceiver(ChatMessage message, String receiverName){
        if (message.getClass().equals(PrivateChatMessage.class)){
            if(!((PrivateChatMessage) message).getReciever().equals(receiverName))
                return false;
        }
        return true;
    }

    /**
     * Get message from user and post to Server Chat
     */
    private static void postToChat() throws LobbyException {
        Map<String,String> message = view.getMessageFromUser();
        server.postToLiveChat(
                playerName,
                message.get("message"));
    }

    /**
     * Get private message from user and post to Server Chat
     */
    private static void postToPrivateChat() throws LobbyException {
        Map<String,String> privateMessage = view.getPrivateMessageFromUser();
        server.postSecretToLiveChat(
                playerName,
                privateMessage.get("receiver"),
                privateMessage.get("message"));
    }

    /**
     * Get move from user and post to server
     */
    private static void postMove() throws LobbyException {
        if(!server.matchHasStarted()){
            return;
        }
        Move move = view.getMoveFromUser();
        server.postMove(playerName,move);
    }

    private static void start(){
        boolean admin = false;
        boolean started = false;
        try {
            admin = server.isLobbyAdmin(playerName);
            if(!admin){
                view.errorMessage("You are not lobby admin");
                return;
            }
            started = server.startGame(playerName);
        } catch (LobbyException e) {
            started = false;
        }
        if(started){
            view.message("Game has started!");
        }
        else{
            view.errorMessage("You can not start lobby now");
        }
    }

    /**
     * Initiate the view according to the selected option
     */
    private static void initView(){
        switch (Client_Settings.ui){
            case CLI:
                view = new CLI();
                break;
            case GUI:
                view = new GUI();
                HelloApplication.startApp();
                break;
            case DRIVER:
                view = new ViewDriver();
        }
    }

    /**
     * Initiate all the objects that will handle the connection to serer
     */
    private static void initConnectionInterface() throws ServerException {
        switch (Client_Settings.connection){
            case RMI:
                server = new ServerRMI(NetworkSettings.serverIp, NetworkSettings.RMIport);
                try {
                    ClientControllerCLI remoteObject = new ClientControllerCLI(playerName);
                    client = new ClientRMI(remoteObject);
                } catch (RemoteException e) {
                    throw new ServerException("Impossible to create RMI client object");
                }
                break;
            case TCP:
                server = new ServerTCP(NetworkSettings.serverIp, NetworkSettings.TCPport);
                client = new ClientSocket();
                ((ClientSocket) client).setName(playerName);
                break;
            case STUB:
                server = new ConnectionStub();
                try {
                    ClientControllerCLI remoteObject = new ClientControllerCLI(playerName);
                    client = new ClientRMI(remoteObject);
                } catch (RemoteException e) {
                    throw new ServerException("Impossible to create RMI client object");
                } //TODO create stub when completed the real one
        }
    }

    /**
     * Initiate the connection interface and attempt a login
     * @return true if login was successful
     */
    private static boolean connect(){
        try{
            //Initiate the server connection interfaces according to settings
            initConnectionInterface();
            //login
            return tryLogin(3,2);
        } catch (ServerException e) {
            return false;
        }
    }

    /**
     * Try login tries times
     * @param tries
     * @return true if successful
     */
    private static boolean tryLogin(int tries, int seconds){
        boolean logged = false;
        for(int attempt = 0; attempt < tries && !logged; attempt++){
            logged = server.login(client); //get previous sessions if present
            if(!logged){
                view.errorMessage("Connection Error, retying in "+seconds+" seconds");
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
    private static void playMatch() throws LobbyException {
        //Receive and execute commands until "exit" lobbyCommand is launched
        while(!exit){
            LobbyCommand lobbyCommand = view.askCommand();
            executeUserCommand(lobbyCommand);
        }
    }
    /**
     * True when the client wants to keep playing
     */
    static boolean play = true;
    /**
     * True when the match has entered or the client has quit
     */
    static boolean exit = false;

    /**
     * the main method that handles the client side application logic
     */
    public static void startClient(){

        //Initiate the view according to the settings
        initView();

        //ask for username
        playerName = view.askUserName();

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
                    view.errorMessage("Something went wrong connecting to server");
                    play = false;
                }
                //ask the player if they want to play again
                play = view.playAgain();
            }
            else{
                view.errorMessage("It was impossible to connect to server");
                play = false;
            }
        }
    }
}