package it.polimi.ingsw.client;


import it.polimi.ingsw.client.Lobby.*;
import it.polimi.ingsw.client.LobbySelection.*;
import it.polimi.ingsw.client.controller.ClientControllerCLI;
import it.polimi.ingsw.client.gui.HelloApplication;
import it.polimi.ingsw.client.gui.LobbySelectionGUI;
import it.polimi.ingsw.client.gui.MatchGUI;
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
    /**
     * Objects that handle connection with server
     */
    static Server server;
    static Lobby lobby;
    static Client client;
    /**
     * UI View Objects
     */
    static LobbySelectionView lobbySelectionView;
    static MatchView matchView;

    /**
     * state of the match
     */
    static boolean play = true;
    static boolean exit = false;

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
                lobbySelectionView.errorMessage("Connection Error, retying in "+seconds+" seconds");
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
    private static Map getAvailableLobbies() throws ServerException {
        return server.getAvailableLobbies();
    }

    /**
     * Get the match thread to start in order to play
     * @return the Match object
     */
    private static void joinLobby() throws ServerException {
        LobbySelectionCommand command = LobbySelectionCommand.Invalid;
        while(command == LobbySelectionCommand.Invalid){
            command = lobbySelectionView.askLobby();
            switch (command){
                case Random:
                    lobby = server.joinRandomLobby(client);
                    break;
                case Number:
                    lobby = server.joinSelectedLobby(client,command.getID());
                    break;
                default:
                    lobbySelectionView.errorMessage("Input a valid id");
                    break;
            }
        }
        try{
            lobbySelectionView.message("You joined #"+ lobby.getID()+" lobby!");
        }
        catch (LobbyException e){
            throw new ServerException("Error in Server");
        }
    }

    /**
     * Execute a lobbyCommand received from view
     * @param lobbyCommand the lobbyCommand to execute
     */
    private static void executeUserCommand(LobbyCommand lobbyCommand){
        //execute action for every lobbyCommand
        try {
            switch (lobbyCommand) {
                case Exit: //quit game
                    matchView.notifyExit();
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
                    lobby.startGame(playerName);
                    break;
                case Move:
                    postMove();
                    break;
                case Peek:
                    matchView.showElement();
                    break;
                case Message:
                    postToChat();
                    break;
                case Help:
                    matchView.showHelp();
                    break;
                default: //post message to chat
                    matchView.notifyInvalidCommand();
            }
        }
        catch (LobbyException e){
            //TODO handle better
            e.printStackTrace();
        }
    }
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
        Map<String,String> message = matchView.getMessageFromUser();
        lobby.postToLiveChat(
                playerName,
                message.get("message"));
    }

    /**
     * Get private message from user and post to Server Chat
     */
    private static void postToPrivateChat() throws LobbyException {
        Map<String,String> privateMessage = matchView.getPrivateMessageFromUser();
        lobby.postSecretToLiveChat(
                playerName,
                privateMessage.get("receiver"),
                privateMessage.get("message"));
    }

    /**
     * Get move from user and post to server
     */
    private static void postMove() throws LobbyException {
        if(!lobby.matchHasStarted()){
            return;
        }
        Move move = matchView.getMoveFromUser();
        lobby.postMove(playerName,move);
    }

    /**
     * Initiate the view according to the selected option
     * @param uiOption
     */
    private static void initView(Client_Settings.UI uiOption){
        switch (uiOption){
            case CLI:
                lobbySelectionView = new LobbySelectionCLI();
                matchView = new MatchCLI();
                break;
            case GUI:
                lobbySelectionView = new LobbySelectionGUI();
                matchView = new MatchGUI();
                HelloApplication.startApp();
                break;
        }
    }

    private static void initConnectionInterface(){
        switch (Client_Settings.connection){
            case RMI:
                server = new ServerRMI(NetworkSettings.serverIp, NetworkSettings.RMIport);
                try {
                    ClientControllerCLI remoteObject = new ClientControllerCLI(playerName);
                    client = new ClientRMI(remoteObject);
                } catch (RemoteException e) {
                    throw new RuntimeException(e); //TODO handle (do we really need exceptions?)
                }
                break;
            case TCP:
                server = new ServerTCP(NetworkSettings.serverIp, NetworkSettings.TCPport);
                client = new ClientSocket();
                break;
        }
    }

    public static void startClient(Client_Settings.UI uiOption){

        //Initiate the view according to the settings
        initView(uiOption);

        //ask for username
        playerName = lobbySelectionView.askUserName();
        lobbySelectionView.greet(playerName);

        //Initiate the server connection interfaces according to settings
        initConnectionInterface();

        //login
        boolean successfulLogin = tryLogin(3,2);

        while(play){
            if(successfulLogin){
                try{
                    //show the client the lobbies they can join
                    lobbySelectionView.showLobbies(getPreviousSessions(),"The lobbies you already joined");
                    lobbySelectionView.showLobbies(getAvailableLobbies(), "The lobbies that are available");

                    //ask the client what lobby to join
                    joinLobby();

                    //game starts
                    //Receive and execute commands until "exit" lobbyCommand is launched
                    while(!exit){
                        LobbyCommand lobbyCommand = matchView.askCommand();
                        executeUserCommand(lobbyCommand);
                    }
                } catch (ServerException e) {
                    lobbySelectionView.errorMessage("Something went wrong connecting to server");
                    play = false;
                }
                //ask the player if they want to play again
                play = lobbySelectionView.playAgain();
            }
            else{
                lobbySelectionView.errorMessage("It was impossible to connect to server");
                play = false;
            }
        }
    }
}