package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Connection.LobbyException;
import it.polimi.ingsw.server.clientonserver.ClientSocket;
import it.polimi.ingsw.shared.*;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerLobbyInterface;
import org.json.simple.JSONObject;

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
            String ID = message.getRequestID();
            JSONObject content = message.getContent(); //content in JSON
            if(!lobbyAssigned)
                exectuteServerCommand(command,content, ID); //execute if still searching for a lobby
            else
                executeLobbyCommnad(command,content, ID); //execute if lobby was assigned
        }


    }
    private void exectuteServerCommand(MessageTcp.MessageCommand command, JSONObject content, String ID){
        switch (command){
            case Login:
                login(content, ID);
                break;
            case GetJoinedLobbies:
                getJoinedLobbies(content, ID);
                break;
            case JoinRandomLobby:
                joinRandomLobby(ID);
                break;
            case CreateLobby:
                createLobby(ID);
                break;
            case GetAvailableLobbies:
                getAvailableLobbies(ID);
                break;
            case JoinSelectedLobby:
                joinSelectedLobby(content, ID);
                break;
            default:
                client.out("Command does not exists");
                break;
        }

    }
    private void executeLobbyCommnad(MessageTcp.MessageCommand command, JSONObject content, String ID){
        switch (command){
            case PostToLiveChat:
                postToLiveChat(content, ID);
                break;
            case PostSecretToLiveChat:
                postSecretToLiveChat(content, ID);
                break;
            case Quit:
                quit(content, ID);
                break;
            case MatchHasStarted:
                matchHasStarted(ID);
                break;
            case PostMove:
                postMove(content, ID);
                break;
            case StartGame:
                startGame(content, ID);
                break;
            case IsLobbyAdmin:
                isLobbyAdmin(content, ID);
                break;
            default:
                client.out("Command does not exists");
                break;
        }

    }

    //SERVER METHODS

    private void login(JSONObject message, String ID){
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
        feedback.setRequestID(ID);
        client.out(feedback.toString()); //send object to client
    }
    private void getJoinedLobbies(JSONObject message, String ID){
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
        feedback.setRequestID(ID);
        client.out(feedback.toString()); //send object to client
    }
    private void getAvailableLobbies(String ID){
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
        feedback.setRequestID(ID);
        client.out(feedback.toString()); //send object to client
    }

    private void joinRandomLobby(String ID){
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
        feedback.setRequestID(ID);
        client.out(feedback.toString()); //send object to client
    }

    private void createLobby(String ID){
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
        feedback.setRequestID(ID);
        client.out(feedback.toString()); //send object to client
    }
    private void joinSelectedLobby(JSONObject message, String ID){
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
        feedback.setRequestID(ID);
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

        } catch (RemoteException e) {
            lobbyID = 0;
        }
        return lobbyID;
    }

    //LOBBY METHODS

    private void postToLiveChat(JSONObject message, String ID){
        boolean foundErrors = false;
        ChatMessage chatMessage = new ChatMessage(message);
        String sender = chatMessage.getSender();
        String content = chatMessage.getMessage();
        synchronized (lobby) {
            try {
                lobby.postToLiveChat(sender, content);
            } catch (RuntimeException e) {
                foundErrors = true;
            }
            MessageTcp feedback = new MessageTcp(); //message to send back
            feedback.setCommand(MessageTcp.MessageCommand.PostToLiveChat); //set message command
            feedback.setContent(Jsonable.boolean2json(foundErrors)); //set message content
            feedback.setRequestID(ID);
            client.out(feedback.toString()); //send object to client

        }
    }
    public void postSecretToLiveChat(JSONObject message, String ID){
        boolean foundErrors = false;
        PrivateChatMessage chatMessage = new PrivateChatMessage(message);
        String sender = chatMessage.getSender();
        String content = chatMessage.getMessage();
        String receiver = chatMessage.getReciever();
        synchronized (lobby) {
            try {
                lobby.postSecretToLiveChat(sender,receiver,content);
            } catch (RuntimeException e) {
                foundErrors = true;
            }
            MessageTcp feedback = new MessageTcp(); //message to send back
            feedback.setCommand(MessageTcp.MessageCommand.PostSecretToLiveChat); //set message command
            feedback.setContent(Jsonable.boolean2json(foundErrors)); //set message content
            feedback.setRequestID(ID);
            client.out(feedback.toString()); //send object to client
        }

    }
    public void quit(JSONObject message, String ID){
        boolean foundErrors = false;
        String playername = Jsonable.json2string(message);
        synchronized (lobby) {
            try {
                lobby.quitGame(playername);
            } catch (RuntimeException e) {
                foundErrors = true;
            }
            MessageTcp feedback = new MessageTcp(); //message to send back
            feedback.setCommand(MessageTcp.MessageCommand.Quit); //set message command
            feedback.setContent(Jsonable.boolean2json(foundErrors)); //set message content
            feedback.setRequestID(ID);
            client.out(feedback.toString()); //send object to client
        }

    }
    public void matchHasStarted(String ID){
        boolean hasStarted;
        synchronized (lobby) {
            hasStarted = lobby.matchHasStarted();
            MessageTcp feedback = new MessageTcp(); //message to send back
            feedback.setCommand(MessageTcp.MessageCommand.MatchHasStarted); //set message command
            feedback.setContent(Jsonable.boolean2json(hasStarted)); //set message content
            feedback.setRequestID(ID);
            client.out(feedback.toString()); //send object to client
        }

    }
    public void postMove(JSONObject message, String ID){
        boolean foundErrors = false;;
        String player = message.get("player").toString(); //TODO for myself, to find a more clean way
        JSONObject move = (JSONObject) message.get("move");
        synchronized (lobby) {
            try {
                lobby.postMove(player,move);
            } catch (ControllerGenericException e) {
                foundErrors = true;
            }
            MessageTcp feedback = new MessageTcp(); //message to send back
            feedback.setCommand(MessageTcp.MessageCommand.PostMove); //set message command
            feedback.setContent(Jsonable.boolean2json(foundErrors)); //set message content
            feedback.setRequestID(ID);
            client.out(feedback.toString()); //send object to client
        }

    }

    public void startGame(JSONObject player, String ID){
        boolean hasStarted;
        String username = Jsonable.json2string(player);
        synchronized (lobby) {
            hasStarted = lobby.startGame(username);
            MessageTcp feedback = new MessageTcp(); //message to send back
            feedback.setCommand(MessageTcp.MessageCommand.StartGame); //set message command
            feedback.setContent(Jsonable.boolean2json(hasStarted)); //set message content
            feedback.setRequestID(ID);
            client.out(feedback.toString()); //send object to client
        }

    }

    public void isLobbyAdmin(JSONObject player, String ID){
        boolean isAdmin;
        String username = Jsonable.json2string(player);
        synchronized (lobby) {
            try {
                isAdmin = lobby.isLobbyAdmin(username);
            } catch (RuntimeException e) {
                isAdmin = false;
            }
            MessageTcp feedback = new MessageTcp(); //message to send back
            feedback.setCommand(MessageTcp.MessageCommand.IsLobbyAdmin); //set message command
            feedback.setContent(Jsonable.boolean2json(isAdmin)); //set message content
            feedback.setRequestID(ID);
            client.out(feedback.toString()); //send object to client
        }

    }


}
