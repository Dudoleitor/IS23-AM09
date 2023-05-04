package it.polimi.ingsw.client.Connection;

import it.polimi.ingsw.client.Connection.TCPThread.ServerTCP_IO;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.*;
import it.polimi.ingsw.shared.model.Move;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Map;


public class ServerTCP extends Server {
    private int id;
    private Socket serverSocket;

    private final ServerTCP_IO serverIO;

    public ServerTCP(IpAddressV4 ip, int port) throws ServerException {
        super(ip, port);
        try {
            serverSocket = new Socket(ip.toString(), port);
            serverIO = new ServerTCP_IO(serverSocket);
        } catch (IOException e) {
            throw new ServerException(e.getMessage());
        }
    }
    /**
     * socket input buffer
     * @return the read line of the buffer
     */
    public MessageTcp in() throws RemoteException {
        return serverIO.in();

    }

    /**
     * send a message through socket connection
     * @param message is the message to send
     */
    public void out(String message){
        serverIO.out(message);
    }
    @Override
    public boolean login(Client client) {
        JSONObject content = new JSONObject();
        content.put("player", client.getPlayerName());
        MessageTcp login = new MessageTcp();
        login.setCommand(MessageTcp.MessageCommand.Login); //set command
        login.setContent(content);
        out(login.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.Login)) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            return Boolean.parseBoolean(response.getContent().get("result").toString());
        }catch (RemoteException e){
            return false;
        }


    }


    @Override
    public Map<Integer,Integer> getJoinedLobbies(String playerName) throws ServerException {
        JSONObject content = new JSONObject();
        content.put("player", playerName);
        MessageTcp requestLobbies = new MessageTcp();
        requestLobbies.setCommand(MessageTcp.MessageCommand.GetJoinedLobbies); //set command
        requestLobbies.setContent(content); //set playername as JsonObject
        out(requestLobbies.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.GetJoinedLobbies)) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            return Jsonable.json2mapInt((JSONObject) response.getContent().get("lobbies"));
        }catch (RemoteException e){
            throw new ServerException(e.getMessage());
        }
    }
    @Override
    public Map<Integer, Integer> getAvailableLobbies()throws ServerException {
        MessageTcp requestLobbies = new MessageTcp();
        requestLobbies.setCommand(MessageTcp.MessageCommand.GetAvailableLobbies); //set command
        out(requestLobbies.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.GetAvailableLobbies)) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            return Jsonable.json2mapInt((JSONObject) response.getContent().get("lobbies"));
        }catch (RemoteException e){
            throw new ServerException(e.getMessage());
        }
    }

    @Override
    public void joinRandomLobby(Client client) throws ServerException {
        MessageTcp requestLobbies = new MessageTcp();
        requestLobbies.setCommand(MessageTcp.MessageCommand.JoinRandomLobby); //set command
        out(requestLobbies.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.JoinRandomLobby)) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            long Lobbyid = Long.parseLong(response.getContent().get("lobbyID").toString());
            if (Lobbyid > 0)
                this.id = (int)Lobbyid;
        }catch (RemoteException e){
            throw new ServerException(e.getMessage());
        }
    }
    @Override
    public void createLobby(Client client) throws ServerException {
        MessageTcp requestLobbies = new MessageTcp();
        requestLobbies.setCommand(MessageTcp.MessageCommand.CreateLobby); //set command
        out(requestLobbies.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.CreateLobby)) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            long Lobbyid = Long.parseLong(response.getContent().get("lobbyID").toString());
            if (Lobbyid > 0)
                this.id = (int) Lobbyid;
        }catch (RemoteException e){
            throw new ServerException(e.getMessage());
        }
    }


    @Override
    public void joinSelectedLobby(Client client, int id)throws ServerException {
        JSONObject content = new JSONObject();
        content.put("lobbyID", id);
        MessageTcp requestLobbies = new MessageTcp();
        requestLobbies.setCommand(MessageTcp.MessageCommand.JoinSelectedLobby); //set command
        requestLobbies.setContent(content);
        out(requestLobbies.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.JoinSelectedLobby)) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            long Lobbyid = Long.parseLong(response.getContent().get("lobbyID").toString());
            if (Lobbyid > 0)
                this.id = (int) Lobbyid;
        }catch (RemoteException e){
            throw new ServerException(e.getMessage());
        }
    }
    @Override
    public void postToLiveChat(String playerName, String message) throws LobbyException {
        ChatMessage chatMessage = new ChatMessage(playerName, message, Color.Black); //TODO maybe create a constructor that doesn't need color
        JSONObject content = new JSONObject();
        MessageTcp postMessage = new MessageTcp();
        content.put("chat", chatMessage.toJson());
        postMessage.setCommand(MessageTcp.MessageCommand.PostToLiveChat); //set command
        postMessage.setContent(content); //set playername as JsonObject
        out(postMessage.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object responses
            while (!response.getCommand().equals(MessageTcp.MessageCommand.PostToLiveChat)) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            boolean errorFound = Boolean.parseBoolean(response.getContent().get("errors").toString());
            if (errorFound) {
                //TODO to implement
            }
        }catch (RemoteException e){
            throw new LobbyException(e.getMessage());
        }
    }

    @Override
    public void postSecretToLiveChat(String sender, String receiver, String message) throws LobbyException{
        PrivateChatMessage chatMessage = new PrivateChatMessage(sender,receiver,message,Color.Black); //TODO maybe create a constructor that doesn't need color
        JSONObject content = new JSONObject();
        content.put("chat", chatMessage.toJson());
        MessageTcp postMessage = new MessageTcp();
        postMessage.setCommand(MessageTcp.MessageCommand.PostSecretToLiveChat); //set command
        postMessage.setContent(content); //set playername as JsonObject
        out(postMessage.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object responses
            while (!response.getCommand().equals(MessageTcp.MessageCommand.PostSecretToLiveChat)) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            boolean errorFound = Boolean.parseBoolean(response.getContent().get("errors").toString());
            if (errorFound) {
                //TODO to implement
            }
        }catch (RemoteException e){
            throw new LobbyException(e.getMessage());
        }
    }

    @Override
    public void quitGame(String player) throws LobbyException{
        JSONObject content = new JSONObject();
        content.put("player", player);
        MessageTcp quitMessage = new MessageTcp();
        quitMessage.setCommand(MessageTcp.MessageCommand.Quit); //set command
        quitMessage.setContent(content);
        out(quitMessage.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object responses
            while (!response.getCommand().equals(MessageTcp.MessageCommand.Quit)) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            boolean errorFound = Boolean.parseBoolean(response.getContent().get("errors").toString());
            if (errorFound) {
                //TODO to implement
            }
        }catch (RemoteException e){
            throw new LobbyException(e.getMessage());
        } finally{
            //TODO TO QUIT anyway
        }

    }

    @Override
    public boolean matchHasStarted() throws LobbyException {
        MessageTcp hasStartedMessage = new MessageTcp();
        hasStartedMessage.setCommand(MessageTcp.MessageCommand.MatchHasStarted); //set command
        out(hasStartedMessage.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object responses
            while (!response.getCommand().equals(MessageTcp.MessageCommand.MatchHasStarted)) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            return Boolean.parseBoolean(response.getContent().get("started").toString());
        }catch (RemoteException e){
            throw new LobbyException(e.getMessage());
        }
    }

    @Override
    public void postMove(String player, Move move) throws LobbyException {
        JSONObject content = new JSONObject();
        content.put("player",player);
        content.put("move",move.toJson());
        MessageTcp postMoveMessage = new MessageTcp();
        postMoveMessage.setCommand(MessageTcp.MessageCommand.PostMove); //set command
        postMoveMessage.setContent(content);
        out(postMoveMessage.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object responses
            while (!response.getCommand().equals(MessageTcp.MessageCommand.PostMove)) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            boolean errorFound = Boolean.parseBoolean(response.getContent().get("errors").toString());
            if (errorFound) {
                //TODO to implement
            }
        }catch (RemoteException e){
            throw new LobbyException(e.getMessage());
        }

    }

    @Override
    public boolean startGame(String player)throws LobbyException {
        JSONObject content = new JSONObject();
        content.put("player", player);
        MessageTcp startGame = new MessageTcp();
        startGame.setCommand(MessageTcp.MessageCommand.StartGame); //set command
        startGame.setContent(content); //set playername as JsonObject
        out(startGame.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.StartGame)) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            return Boolean.parseBoolean(response.getContent().get("start").toString());
        }catch (RemoteException e){
            throw new LobbyException(e.getMessage());
        }
    }
    @Override
    public boolean isLobbyAdmin(String player)throws LobbyException {
        JSONObject content = new JSONObject();
        content.put("player", player);
        MessageTcp isAdmin = new MessageTcp();
        isAdmin.setCommand(MessageTcp.MessageCommand.IsLobbyAdmin); //set command
        isAdmin.setContent(content); //set playername as JsonObject
        out(isAdmin.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.IsLobbyAdmin)) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            return Boolean.parseBoolean(response.getContent().get("admin").toString());
        }catch (RemoteException e){
            throw new LobbyException(e.getMessage());
        }
    }
    @Override
    public int getLobbyID()throws LobbyException {
        if(id >0)
            return this.id;
        else
            throw new LobbyException("lobby doesn't exists");
    }

    /**
     * This method is used to observe the player supposed
     * to play in the current turn.
     *
     * @return String name of the player
     */
    @Override
    public String getCurrentPlayer() throws LobbyException {
        return ""; // TODO
    }

    /**
     * This function is used to check if the client was already connected to
     * a lobby and was previously disconnected.
     *
     * @param playerName String name of the player
     * @return Int id of the lobby if the player was previously connected,
     * -1 if not.
     */
    @Override
    public int disconnectedFromLobby(String playerName) throws ServerException {
        return -1; //TODO
    }
}
