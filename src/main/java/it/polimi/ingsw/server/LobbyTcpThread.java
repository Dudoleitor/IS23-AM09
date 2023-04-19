package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Connection.LobbyException;
import it.polimi.ingsw.server.clientonserver.ClientSocket;
import it.polimi.ingsw.shared.ChatMessage;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.MessageTcp;

import java.util.HashMap;
import java.util.Map;

public class LobbyTcpThread extends Thread{
    private final ClientSocket client;
    private final Lobby lobby;
    private boolean exit;
    public LobbyTcpThread(ClientSocket client, Lobby lobby) {
        this.client = client;
        this.lobby = lobby;
        this.exit = false;
    }
    public void run() {
        while(!exit){
            String string = client.in();
            MessageTcp message = new MessageTcp(string);
            MessageTcp.MessageCommand command = message.getCommand(); //header of message
            String content = message.getContent(); //content in JSON
            executeCommand(command,content);
        }
    }
    private void executeCommand(MessageTcp.MessageCommand command, String content){
        switch (command){
            case PostToLiveChat:
                postToLiveChat(content);
                break;

            default:
                client.out("Command does not exists");
                break;
        }

    }
    private void postToLiveChat(String message){
        boolean foundErrors = false;
        ChatMessage chatMessage = new ChatMessage(Jsonable.parseString(message));
        System.out.println(chatMessage);
        String sender = chatMessage.getSender();
        String content = chatMessage.getMessage();
        synchronized (lobby) {
            try {
                lobby.postToLiveChat(sender, content);
            } catch (Exception e) {
                foundErrors = true;
            }
            MessageTcp feedback = new MessageTcp(); //message to send back
            feedback.setCommand(MessageTcp.MessageCommand.PostToLiveChat); //set message command
            feedback.setContent(Jsonable.boolean2json(foundErrors)); //set message content
            client.out(feedback.toString()); //send object to client

        }
    }


}
