package it.polimi.ingsw.server;

import it.polimi.ingsw.server.clientonserver.ClientSocket;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.MessageTcp;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerLobbyInterface;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class LoginTcpThread extends Thread{ //TODO
    private final ClientSocket client;
    private final ServerMain server;
    boolean lobbyAssigned = false;

    public LoginTcpThread(ServerMain server, ClientSocket client){
        this.server = server;
        this.client= client;


    }

    /**
     * this method wait for user input and send the message to the correct server method
     */

    public void run() {
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
                case JoinRandomLobby:
                    joinRandomLobby();
                case CreateLobby:
                    createLobby();
                case GetAvailableLobbies:
                    getAvailableLobbies();
                case JoinSelectedLobby:
                    joinSelectedLobby(content);
                default:
                    client.out("Command does not exists");
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
    private void getAvailableLobbies(){
        Map<Integer,Integer> lobbies;
        synchronized (server){
            try {
                lobbies = server.showAvailableLobbies();
            } catch (NullPointerException | RemoteException e) {
                lobbies = new HashMap<>();
            }
        }
        MessageTcp feedback = new MessageTcp(); //message to send back
        feedback.setCommand(MessageTcp.MessageCommand.GetAvailableLobbies); //set message command
        feedback.setContent(Jsonable.map2json(lobbies)); //set message content
        client.out(feedback.toString()); //send object to client
    }

    private void joinRandomLobby(){
        int lobbyID;
        ServerLobbyInterface lobbyInterface = null;
        synchronized (server){
            try {
                lobbyInterface = server.joinRandomLobby(client);
            } catch (NullPointerException e) {
                 //TODO to send back error message to set username first
            }
        }
        lobbyID = LobbyIni(lobbyInterface);
        MessageTcp feedback = new MessageTcp(); //message to send back
        feedback.setCommand(MessageTcp.MessageCommand.JoinRandomLobby); //set message command
        feedback.setContent(Jsonable.int2json(lobbyID)); //set message content
        client.out(feedback.toString()); //send object to client
    }

    private void createLobby(){
        int lobbyID;
        ServerLobbyInterface lobbyInterface = null;
        synchronized (server){
            try {
                lobbyInterface = server.createLobby(client);
            } catch (NullPointerException e) {
                //TODO to send back error message to set username first
            }
        }
        lobbyID = LobbyIni(lobbyInterface);
        MessageTcp feedback = new MessageTcp(); //message to send back
        feedback.setCommand(MessageTcp.MessageCommand.CreateLobby); //set message command
        feedback.setContent(Jsonable.int2json(lobbyID)); //set message content
        client.out(feedback.toString()); //send object to client
    }
    private void joinSelectedLobby(String message){
        int lobbyID = Jsonable.json2int(message);
        ServerLobbyInterface lobbyInterface = null;
        synchronized (server){
            try {
                lobbyInterface = server.joinSelectedLobby(client,lobbyID);
            } catch (NullPointerException e) {
                //TODO to send back error message to set username first
            }
        }
        lobbyID = LobbyIni(lobbyInterface);
        MessageTcp feedback = new MessageTcp(); //message to send back
        feedback.setCommand(MessageTcp.MessageCommand.JoinSelectedLobby); //set message command
        feedback.setContent(Jsonable.int2json(lobbyID)); //set message content
        client.out(feedback.toString()); //send object to client
    }


    private int LobbyIni(ServerLobbyInterface lobbyInterface){
        int lobbyID;
        Lobby lobby;
        try {
            lobbyID = lobbyInterface.getID(); //TODO to find better solution that doesn't rely on this interface
            synchronized (server){
                lobby = server.getLobbybyID(lobbyID);
            }
            if(lobby != null) {
                new LobbyTcpThread(client, lobby).run();
                lobbyAssigned = true;
            }
        } catch (Exception e) {
            lobbyID = 0;
        }
        return lobbyID;
    }


}
