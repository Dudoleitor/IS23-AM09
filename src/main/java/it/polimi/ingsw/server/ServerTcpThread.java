package it.polimi.ingsw.server;

import it.polimi.ingsw.server.clientonserver.ClientSocket;
import it.polimi.ingsw.shared.*;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerLobbyInterface;
import org.json.simple.JSONObject;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTcpThread extends Thread{
    private final ClientSocket client;
    private final ServerMain server;
    private Lobby lobby;
    private boolean lobbyAssigned;
    private boolean exit;
    private final Object pingLock;
    private final ExecutorService executor;

    public ServerTcpThread(ServerMain server, ClientSocket client){
        this.server = server;
        this.client= client;
        client.setThreadReference(this);
        this.lobbyAssigned = false;
        this.exit = false;
        this.pingLock = new Object();
        this.executor = Executors.newSingleThreadExecutor();
        executor.submit(new clientAlive(pingLock, client));
    }

    /**
     * this method wait for user input and send the message to the correct server method
     */
    @Override
    public void run() {
        while(!exit) {
            MessageTcp message = client.in();
            MessageTcp.MessageCommand command = message.getCommand(); //header of message
            String ID = message.getRequestID();
            JSONObject content = message.getContent(); //content in JSON

            if (command == MessageTcp.MessageCommand.Ping) {
                synchronized (pingLock) {
                    pingLock.notifyAll();
                }
            }
            else if(!lobbyAssigned)
                exectuteServerCommand(command,content,ID); //execute if still searching for a lobby
            else
                executeLobbyCommnad(command,content,ID); //execute if lobby was assigned
        }
        executor.shutdownNow(); //quits all runnable launched

    }
    private void exectuteServerCommand(MessageTcp.MessageCommand command, JSONObject content, String ID){
        switch (command){
            case Login:
                login(content,ID);
                break;
            case GetJoinedLobbies:
                getJoinedLobbies(content,ID);
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
                joinSelectedLobby(content,ID);
                break;
            default:
                break;
        }

    }
    private void executeLobbyCommnad(MessageTcp.MessageCommand command, JSONObject content,String ID){
        switch (command){
            case PostToLiveChat:
                postToLiveChat(content,ID);
                break;
            case PostSecretToLiveChat:
                postSecretToLiveChat(content,ID);
                break;
            case Quit:
                quit(content,ID);
                break;
            case MatchHasStarted:
                matchHasStarted(ID);
                break;
            case PostMove:
                postMove(content,ID);
                break;
            case StartGame:
                startGame(content,ID);
                break;
            case IsLobbyAdmin:
                isLobbyAdmin(content,ID);
                break;
            default:
                break;
        }

    }

    //SERVER METHODS

    private void login(JSONObject message, String ID){
        client.setName(message.get("player").toString());
        boolean logged;
        synchronized (server){
            try {
                logged = server.login(client);
            } catch (RemoteException e) {
                logged = false;
            }
        }
        JSONObject result = new JSONObject();
        result.put("result", logged);
        MessageTcp feedback = new MessageTcp(); //message to send back
        feedback.setCommand(MessageTcp.MessageCommand.Login); //set message command
        feedback.setContent(result); //set message content
        feedback.setRequestID(ID);
        client.out(feedback.toString()); //send object to client
    }
    private void getJoinedLobbies(JSONObject message, String ID){
        Map<Integer,Integer> lobbies;
        String username = message.get("player").toString();
        synchronized (server){
            try {
                lobbies = server.getJoinedLobbies(username);
            } catch (NullPointerException e) {
                lobbies = new HashMap<>(); //TODO to send back error message to set username first
            }
        }
        JSONObject result = new JSONObject();
        result.put("lobbies", Jsonable.map2json(lobbies));
        MessageTcp feedback = new MessageTcp(); //message to send back
        feedback.setCommand(MessageTcp.MessageCommand.GetJoinedLobbies); //set message command
        feedback.setContent(result); //set message content
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
        JSONObject result = new JSONObject();
        result.put("lobbies", Jsonable.map2json(lobbies));
        MessageTcp feedback = new MessageTcp(); //message to send back
        feedback.setCommand(MessageTcp.MessageCommand.GetAvailableLobbies); //set message command
        feedback.setContent(result); //set message content
        feedback.setRequestID(ID);
        client.out(feedback.toString()); //send object to client
    }

    private void joinRandomLobby(String ID){
        int lobbyID;
        ServerLobbyInterface lobbyInterface = null;
        synchronized (server){
            try {
                lobbyInterface = server.joinRandomLobby(client);
            } catch (NullPointerException | RemoteException  e) {
                 //TODO to send back error message to set username first
            }
        }
        lobbyID = LobbyIni(lobbyInterface);
        JSONObject result = new JSONObject();
        result.put("lobbyID", lobbyID);
        MessageTcp feedback = new MessageTcp(); //message to send back
        feedback.setCommand(MessageTcp.MessageCommand.JoinRandomLobby); //set message command
        feedback.setContent(result); //set message content
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
        JSONObject result = new JSONObject();
        result.put("lobbyID", lobbyID);
        MessageTcp feedback = new MessageTcp(); //message to send back
        feedback.setCommand(MessageTcp.MessageCommand.CreateLobby); //set message command
        feedback.setContent(result); //set message content
        feedback.setRequestID(ID);
        client.out(feedback.toString()); //send object to client
    }
    private void joinSelectedLobby(JSONObject message, String ID){
        long lobbyID = Long.parseLong(message.get("lobbyID").toString());
        ServerLobbyInterface lobbyInterface = null;
        synchronized (server){
            try {
                lobbyInterface = server.joinSelectedLobby(client,(int) lobbyID);
            } catch (NullPointerException | RemoteException e) {
                //TODO to send back error message to set username first
            }
        }
        lobbyID = LobbyIni(lobbyInterface);
        JSONObject result = new JSONObject();
        result.put("lobbyID", lobbyID);
        MessageTcp feedback = new MessageTcp(); //message to send back
        feedback.setCommand(MessageTcp.MessageCommand.JoinSelectedLobby); //set message command
        feedback.setContent(result); //set message content
        feedback.setRequestID(ID);
        client.out(feedback.toString()); //send object to client
    }


    private int LobbyIni(ServerLobbyInterface lobbyInterface){
        if(lobbyInterface != null) {
            this.lobbyAssigned = true;
            this.lobby = (Lobby) lobbyInterface;

            return lobby.getID();
        } else {
            return 0;
        }
    }

    //LOBBY METHODS

    private void postToLiveChat(JSONObject message, String ID){
        boolean foundErrors = false;
        ChatMessage chatMessage = new ChatMessage((JSONObject) message.get("chat"));
        String sender = chatMessage.getSender();
        String content = chatMessage.getMessage();
        synchronized (lobby) {
            try {
                lobby.postToLiveChat(sender, content);
            } catch (RuntimeException e) {
                foundErrors = true;
            }
            JSONObject result = new JSONObject();
            result.put("errors", foundErrors);
            MessageTcp feedback = new MessageTcp(); //message to send back
            feedback.setCommand(MessageTcp.MessageCommand.PostToLiveChat); //set message command
            feedback.setContent(result); //set message content
            feedback.setRequestID(ID);
            client.out(feedback.toString()); //send object to client

        }
    }
    private void postSecretToLiveChat(JSONObject message, String ID){
        boolean foundErrors = false;
        PrivateChatMessage chatMessage = new PrivateChatMessage((JSONObject) message.get("chat"));
        String sender = chatMessage.getSender();
        String content = chatMessage.getMessage();
        String receiver = chatMessage.getReciever();
        synchronized (lobby) {
            try {
                lobby.postSecretToLiveChat(sender,receiver,content);
            } catch (RuntimeException e) {
                foundErrors = true;
            }
            JSONObject result = new JSONObject();
            result.put("errors", foundErrors);
            MessageTcp feedback = new MessageTcp(); //message to send back
            feedback.setCommand(MessageTcp.MessageCommand.PostSecretToLiveChat); //set message command
            feedback.setContent(result); //set message content
            feedback.setRequestID(ID);
            client.out(feedback.toString()); //send object to client
        }

    }
    private void quit(JSONObject message, String ID){
        boolean foundErrors = false;
        String playername = message.get("player").toString();
        synchronized (lobby) {
            try {
                lobby.quitGame(playername);
            } catch (RuntimeException e) {
                foundErrors = true;
            }
            JSONObject result = new JSONObject();
            result.put("errors", foundErrors);
            MessageTcp feedback = new MessageTcp(); //message to send back
            feedback.setCommand(MessageTcp.MessageCommand.Quit); //set message command
            feedback.setContent(result); //set message content
            feedback.setRequestID(ID);
            client.out(feedback.toString()); //send object to client
            terminate();
        }

    }
    private void matchHasStarted(String ID){
        boolean hasStarted;
        synchronized (lobby) {
            hasStarted = lobby.matchHasStarted();
            JSONObject result = new JSONObject();
            result.put("started", hasStarted);
            MessageTcp feedback = new MessageTcp(); //message to send back
            feedback.setCommand(MessageTcp.MessageCommand.MatchHasStarted); //set message command
            feedback.setContent(result); //set message content
            feedback.setRequestID(ID);
            client.out(feedback.toString()); //send object to client
        }

    }
    private void postMove(JSONObject message, String ID){
        boolean foundErrors = false;
        String player = message.get("player").toString(); //TODO for myself, to find a more clean way
        JSONObject move = (JSONObject) message.get("move");
        synchronized (lobby) {
            try {
                lobby.postMove(player,move);
            } catch (ControllerGenericException e) {
                foundErrors = true;
            }
            JSONObject result = new JSONObject();
            result.put("errors", foundErrors);
            MessageTcp feedback = new MessageTcp(); //message to send back
            feedback.setCommand(MessageTcp.MessageCommand.PostMove); //set message command
            feedback.setContent(result); //set message content
            feedback.setRequestID(ID);
            client.out(feedback.toString()); //send object to client
        }

    }

    private void startGame(JSONObject content, String ID){
        boolean hasStarted;
        String username = content.get("player").toString();
        final boolean erasePreviousMatches = content.get("erasePreviousMatches").toString().equals("true");
        synchronized (lobby) {
            hasStarted = lobby.startGame(username, erasePreviousMatches);
            JSONObject result = new JSONObject();
            result.put("start", hasStarted);
            MessageTcp feedback = new MessageTcp(); //message to send back
            feedback.setCommand(MessageTcp.MessageCommand.StartGame); //set message command
            feedback.setContent(result); //set message content
            feedback.setRequestID(ID);
            client.out(feedback.toString()); //send object to client
        }

    }

    private void isLobbyAdmin(JSONObject player, String ID){
        boolean isAdmin;
        String username = player.get("player").toString();
        synchronized (lobby) {
            try {
                isAdmin = lobby.isLobbyAdmin(username);
            } catch (RuntimeException e) {
                isAdmin = false;
            }
            JSONObject result = new JSONObject();
            result.put("admin", isAdmin);
            MessageTcp feedback = new MessageTcp(); //message to send back
            feedback.setCommand(MessageTcp.MessageCommand.IsLobbyAdmin); //set message command
            feedback.setContent(result); //set message content
            feedback.setRequestID(ID);
            client.out(feedback.toString()); //send object to client
        }

    }

    public void terminate(){
        this.exit = true;
        executor.shutdownNow();
        Thread.currentThread().interrupt();
    }

}

class clientAlive implements Runnable {
    private final Object pingLock;
    private final ClientSocket client;
    private final int waitTime = ((int) NetworkSettings.serverPingIntervalSeconds) * 2000;
    protected clientAlive(Object pingLock, ClientSocket client){
        this.pingLock = pingLock;
        this.client = client;
    }

    @Override
    public void run() {
        long waitStart;
        synchronized (pingLock) {
            while (true) {
                waitStart = System.currentTimeMillis();
                try {
                    pingLock.wait(waitTime);
                } catch (InterruptedException e) {
                    return;
                }
                if (System.currentTimeMillis() >=
                        waitStart + waitTime) {
                    System.err.println("Client " + client.getPlayerName() + " has timed out.");
                    client.getNetworkExceptionHandler()
                            .handleNetworkException(
                                    client,
                                    new RemoteException("Client has timed out."));
                }
            }
        }
    }
}
