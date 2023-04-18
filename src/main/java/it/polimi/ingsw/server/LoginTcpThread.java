package it.polimi.ingsw.server;

import it.polimi.ingsw.server.clientonserver.ClientSocket;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.MessageTcp;

import java.io.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class LoginTcpThread extends Thread{ //TODO
    private final ClientSocket client;
    private final ServerMain server;


    public LoginTcpThread(ServerMain server, ClientSocket client){
        this.server = server;
        this.client= client;


    }

    /**
     * this method wait for user input and send the message to the correct server method
     */

    public void run() {
        boolean lobbyAssigned = false;
        while(!lobbyAssigned){
            String string = client.in();
            MessageTcp message = new MessageTcp(string);
            MessageTcp.MessageCommand command = message.getCommand(); //header of message
            String content = message.getContent(); //content in JSON
            switch (command){
                case Login:
                    login(content);
                case GetJoinedLobbies:
                    getJoinedLobbies(content);
            }
        }
    }

    private void login(String message){
        client.setName(Jsonable.json2string(message));
        boolean logged;
        synchronized (server){
            try {
                logged = server.login(client);
            } catch (RemoteException e) {
                logged = false;
            }
        }
        MessageTcp feedback = new MessageTcp(); //message to send back
        feedback.setCommand(MessageTcp.MessageCommand.Login); //set message command
        feedback.setContent(Jsonable.boolean2json(logged)); //set message content
        client.out(feedback.toString()); //send object to client
    }
    private void getJoinedLobbies(String message){
        Map<Integer,Integer> lobbies;
        String username = Jsonable.json2string(message);
        synchronized (server){
            try {
                lobbies = server.getJoinedLobbies(username);
            } catch (NullPointerException e) {
                lobbies = new HashMap<>(); //TODO to send back error message to set username first
            }
        }
        MessageTcp feedback = new MessageTcp(); //message to send back
        feedback.setCommand(MessageTcp.MessageCommand.GetJoinedLobbies); //set message command
        feedback.setContent(Jsonable.map2json(lobbies)); //set message content
        client.out(feedback.toString()); //send object to client
    }


}
