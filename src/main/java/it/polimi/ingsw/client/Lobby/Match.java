package it.polimi.ingsw.client.Lobby;

import it.polimi.ingsw.client.Command;
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
     * Execute a command received from view
     * @param command the command to execute
     */
    private void executeUserCommand(Command command){
        //execute action for every command
        switch (command){
            case Exit: //quit game
                view.notifyExit();
                exit = true;
                break;
            case Print: //print all messages
                updateLiveChat();
                view.printAllMessages(chat);
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
            default: //post message to chat
                view.notifyInvalidCommand();
        }
    }
    /**
     * Downloads all the messages that are present on server and missing in local copy
     */
    private void updateLiveChat(){
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
    private void postToChat(){
        Map<String,String> message = view.getMessageFromUser();
        lobby.postToLiveChat(
                playerName,
                message.get("message"));
    }

    /**
     * Get private message from user and post to Server Chat
     */
    private void postToPrivateChat(){
        Map<String,String> privateMessage = view.getPrivateMessageFromUser();
        lobby.postSecretToLiveChat(
                playerName,
                privateMessage.get("receiver"),
                privateMessage.get("message"));
    }

    /**
     * Get move from user and post to server
     */
    private void postMove(){
        if(!lobby.matchHasStarted()){
            return;
        }
            Move move = view.getMoveFromUser();
            lobby.postMove(playerName,move);
    }

    @Override
    public void run(){
        while(!exit){ //Receive commands until "exit" command is launched
            Command command = view.askCommand();
            try {
                executeUserCommand(command);
            } catch (Exception e) {
                //TODO hanlde
            }
        }
    }
}
