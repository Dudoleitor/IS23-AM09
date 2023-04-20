package it.polimi.ingsw.server;

import it.polimi.ingsw.server.clientonserver.ClientSocket;
import it.polimi.ingsw.shared.ChatMessage;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.MessageTcp;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerLobbyInterface;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class ServerTcpThread extends Thread{ //TODO
    private final ClientSocket client;
    private final ServerMain server;
    private  Lobby lobby;
    private boolean lobbyAssigned;
    private boolean exit;



    public ServerTcpThread(ServerMain server, ClientSocket client){
        this.server = server;
        this.client= client;
        this.lobbyAssigned = false;
        this.exit = false;

    }

    /**
     * this method wait for user input and send the message to the correct server method
     */
    @Override
    public void run() {
        while(!exit){
            String string = client.in(); //TODO to make it wait on input ready
            MessageTcp message = new MessageTcp(string);
            MessageTcp.MessageCommand command = message.getCommand(); //header of message
            String content = message.getContent(); //content in JSON
            if(!lobbyAssigned)
                exectuteServerCommand(command,content); //execute if still searching for a lobby
            else
                executeLobbyCommnad(command,content); //execute if lobby was assigned
        }


    }
    private void exectuteServerCommand(MessageTcp.MessageCommand command, String content){
        switch (command){
            case Login:
                login(content);
                break;
            case GetJoinedLobbies:
                getJoinedLobbies(content);
                break;
            case JoinRandomLobby:
                joinRandomLobby();
                break;
            case CreateLobby:
                createLobby();
                break;
            case GetAvailableLobbies:
                getAvailableLobbies();
                break;
            case JoinSelectedLobby:
                joinSelectedLobby(content);
                break;
            default:
                client.out("Command does not exists");
                break;
        }

    }
    private void executeLobbyCommnad(MessageTcp.MessageCommand command, String content){
        switch (command){
            case PostToLiveChat:
                postToLiveChat(content);
                break;

            default:
                client.out("Command does not exists");
                break;
        }

    }

    //SERVER METHODS

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
        Lobby lobbyGet;
        try {
            lobbyID = lobbyInterface.getID(); //TODO to find better solution that doesn't rely on this interface
            synchronized (server){
                lobbyGet = server.getLobbybyID(lobbyID);
            }
            this.lobbyAssigned = true;
            this.lobby = lobbyGet;

        } catch (Exception e) {
            lobbyID = 0;
        }
        return lobbyID;
    }

    //LOBBY METHODS

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
