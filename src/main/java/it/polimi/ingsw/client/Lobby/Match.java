package it.polimi.ingsw.client.Lobby;

import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.*;

import java.util.Map;

public class Match extends Thread{
    private Chat chat;
    private Lobby lobby;
    private String playerName;
    private Client client;
    private MatchView view;

    boolean exit = false;

    public Match(String playerName, Lobby stub, MatchView view){
        this.playerName = playerName;
        this.lobby = stub;
        this.chat = new Chat();
        this.view = view;
    }

    /**
     * Execute a lobbyCommand received from view
     * @param lobbyCommand the lobbyCommand to execute
     */
    private void executeUserCommand(LobbyCommand lobbyCommand){
        //execute action for every lobbyCommand
        try {
            switch (lobbyCommand) {
                case Exit: //quit game
                    view.notifyExit();
                    exit = true;
                    break;
                case Print: //print all messages
                    updateLiveChat();
                    view.showAllMessages(chat);
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
            }
        }
        catch (LobbyException e){
            //TODO handle better
            e.printStackTrace();
        }
    }
    /**
     * Downloads all the messages that are present on server and missing in local copy
     */
    //TODO delete when useless
    private void updateLiveChat() throws LobbyException {
        chat = lobby.updateLiveChat();
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
    private void postToChat() throws LobbyException {
        Map<String,String> message = view.getMessageFromUser();
        lobby.postToLiveChat(
                playerName,
                message.get("message"));
    }

    /**
     * Get private message from user and post to Server Chat
     */
    private void postToPrivateChat() throws LobbyException {
        Map<String,String> privateMessage = view.getPrivateMessageFromUser();
        lobby.postSecretToLiveChat(
                playerName,
                privateMessage.get("receiver"),
                privateMessage.get("message"));
    }

    /**
     * Get move from user and post to server
     */
    private void postMove() throws LobbyException {
        if(!lobby.matchHasStarted()){
            return;
        }
            Move move = view.getMoveFromUser();
            lobby.postMove(playerName,move);
    }

    @Override
    public void run(){
        while(!exit){ //Receive commands until "exit" lobbyCommand is launched
            LobbyCommand lobbyCommand = view.askCommand();
            executeUserCommand(lobbyCommand);
        }
    }
}
