package it.polimi.ingsw.client.Connection;

import it.polimi.ingsw.shared.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//TODO implement
public class LobbyTCP extends Lobby {

    private int port;
    private int id;
    private PrintWriter serverOut;
    private BufferedReader serverIn;

    public LobbyTCP(Socket server, int id) {
        this.port = port;
        this.id = id; //calculate id by reversing creation lobbyPort criteria;
        try {
            Socket lobby = server;
            serverOut = new PrintWriter(lobby.getOutputStream(), true);
            serverIn = new BufferedReader(new InputStreamReader(lobby.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String in(){
        boolean ready = false;
        try {
            while(!ready){
                if(serverIn.ready())
                    ready = true;
            }
            return serverIn.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * send a message through socket connection
     * @param message is the message to send
     */
    public void out(String message){
        serverOut.println(message);
    }

    @Override
    public void postToLiveChat(String playerName, String message) throws LobbyException{
        MessageTcp postMessage = new MessageTcp();
        ChatMessage chatMessage = new ChatMessage(playerName,message, Color.Black); //TODO maybe create a constructor that doesn't need color
        postMessage.setCommand(MessageTcp.MessageCommand.PostToLiveChat); //set command
        postMessage.setContent(chatMessage.toJson()); //set playername as JsonObject
        out(postMessage.toString());
        MessageTcp response = new MessageTcp(in()); //wait for response by server and create an object responses
        boolean errorFound = Jsonable.json2boolean(response.getContent());
        if(errorFound) {
            //TODO to implement
        }
    }

    @Override
    public void postSecretToLiveChat(String sender, String receiver, String message) throws LobbyException{
        MessageTcp postMessage = new MessageTcp();
        PrivateChatMessage chatMessage = new PrivateChatMessage(sender,receiver,message,Color.Black); //TODO maybe create a constructor that doesn't need color
        postMessage.setCommand(MessageTcp.MessageCommand.PostSecretToLiveChat); //set command
        postMessage.setContent(chatMessage.toJson()); //set playername as JsonObject
        out(postMessage.toString());
        MessageTcp response = new MessageTcp(in()); //wait for response by server and create an object responses
        boolean errorFound = Jsonable.json2boolean(response.getContent());
        if(errorFound) {
            //TODO to implement
        }

    }

    @Override
    public void quitGame(String player) throws LobbyException{

    }

    @Override
    public boolean matchHasStarted() throws LobbyException {
        return false;
    }

    @Override
    public void postMove(String player, Move move) throws LobbyException {

    }

    @Override
    public boolean startGame(String player)throws LobbyException {
        return false;
    }

    @Override
    public boolean isLobbyAdmin(String player)throws LobbyException {
        return false;
    }

    @Override
    public int getID()throws LobbyException {
        return this.id;
    }
}
