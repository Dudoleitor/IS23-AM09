package it.polimi.ingsw.client.Lobby;

import it.polimi.ingsw.client.Command;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.*;

import java.util.Map;

public abstract class LobbyUI extends Thread{
    protected Chat chat;
    protected Lobby lobby;
    protected String playerName;
    protected Client client;

    boolean exit = false;

    LobbyUI(String playerName, Lobby stub){
        this.playerName = playerName;
        this.lobby = stub;
        this.chat = new Chat();
    }
    protected abstract Command askCommand();
    protected void executeUserCommand(Command com){
        //execute action for every command
        switch (com){
            case Exit: //quit game
                notifyExit();
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
                postMove();
                break;
            case Peek:
                showElement();
                break;
            case Message:
                postToChat();
                break;
            default: //post message to chat
                notifyInvalidCommand();
        }
    }
    protected abstract void notifyExit();
    /**
     * Downloads all the messages that are present on server and missing in local copy
     */
    public void updateLiveChat(){
        chat = lobby.updateLiveChat();
    }
    public abstract void printAllMessages();
    public abstract void greet();
    public static boolean checkValidReceiver(ChatMessage message, String receiverName){
        if (message.getClass().equals(PrivateChatMessage.class)){
            if(!((PrivateChatMessage) message).getReciever().equals(receiverName))
                return false;
        }
        return true;
    }
    public void postToChat(){
        Map<String,String> message = getMessageFromUser();
        lobby.postToLiveChat(
                playerName,
                message.get("message"));
    }
    public abstract Map<String,String> getMessageFromUser();
    public void postToPrivateChat(){
        Map<String,String> privateMessage = getPrivateMessageFromUser();
        lobby.postSecretToLiveChat(
                playerName,
                privateMessage.get("receiver"),
                privateMessage.get("message"));
    }
    public abstract Map<String,String> getPrivateMessageFromUser();
    private void postMove(){
        if(!lobby.matchHasStarted()){
            return;
        }
            Move move = getMoveFromUser();
            lobby.postMove(playerName,move);
    }
    protected abstract Move getMoveFromUser();

    protected abstract void showElement();

    protected abstract void notifyInvalidCommand();

    @Override
    public void run(){
        greet();
        while(!exit){ //Receive commands until "exit" command is launched
            Command command = askCommand();
            try {
                executeUserCommand(command);
            } catch (Exception e) {
                //TODO hanlde
            }
        }
    }
}
