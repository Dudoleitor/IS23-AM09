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
    public void out(String message){serverIO.out(message);}
    @Override
    public boolean login(Client client) {
        MessageTcp login = new MessageTcp();
        login.setCommand(MessageTcp.MessageCommand.Login); //set command
        login.setContent(Jsonable.string2json(client.getPlayerName())); //set playername as JsonObject
        login.generateRequestID();
        out(login.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.Login) && !response.getRequestID().equals(login.getRequestID())) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            return Jsonable.json2boolean(response.getContent());
        }catch (RemoteException e){
            return false;
        }


    }


    @Override
    public Map<Integer,Integer> getJoinedLobbies(String playerName) throws ServerException {
        MessageTcp requestLobbies = new MessageTcp();
        requestLobbies.setCommand(MessageTcp.MessageCommand.GetJoinedLobbies); //set command
        requestLobbies.setContent(Jsonable.string2json(playerName)); //set playername as JsonObject
        requestLobbies.generateRequestID();
        out(requestLobbies.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.GetJoinedLobbies) && !response.getRequestID().equals(requestLobbies.getRequestID())) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            return Jsonable.json2mapInt(response.getContent());
        }catch (RemoteException e){
            throw new ServerException(e.getMessage());
        }
    }
    @Override
    public Map<Integer, Integer> getAvailableLobbies()throws ServerException {
        MessageTcp requestLobbies = new MessageTcp();
        requestLobbies.setCommand(MessageTcp.MessageCommand.GetAvailableLobbies); //set command
        requestLobbies.generateRequestID();
        out(requestLobbies.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.GetAvailableLobbies) && !response.getRequestID().equals(requestLobbies.getRequestID())) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            return Jsonable.json2mapInt(response.getContent());
        }catch (RemoteException e){
            throw new ServerException(e.getMessage());
        }
    }

    @Override
    public void joinRandomLobby(Client client) throws ServerException {
        MessageTcp requestLobbies = new MessageTcp();
        requestLobbies.setCommand(MessageTcp.MessageCommand.JoinRandomLobby); //set command
        requestLobbies.generateRequestID();
        out(requestLobbies.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.JoinRandomLobby) && !response.getRequestID().equals(requestLobbies.getRequestID())) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            int Lobbyid = Jsonable.json2int(response.getContent());
            if (Lobbyid > 0)
                this.id = Lobbyid;
        }catch (RemoteException e){
            throw new ServerException(e.getMessage());
        }
    }
    @Override
    public void createLobby(Client client) throws ServerException {
        MessageTcp requestLobbies = new MessageTcp();
        requestLobbies.setCommand(MessageTcp.MessageCommand.CreateLobby); //set command
        requestLobbies.generateRequestID();
        out(requestLobbies.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.CreateLobby) && !response.getRequestID().equals(requestLobbies.getRequestID())) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            int Lobbyid = Jsonable.json2int(response.getContent());
            if (Lobbyid > 0)
                this.id = Lobbyid;
        }catch (RemoteException e){
            throw new ServerException(e.getMessage());
        }
    }


    @Override
    public void joinSelectedLobby(Client client, int id)throws ServerException {
        MessageTcp requestLobbies = new MessageTcp();
        requestLobbies.setCommand(MessageTcp.MessageCommand.JoinSelectedLobby); //set command
        requestLobbies.setContent(Jsonable.int2json(id));
        requestLobbies.generateRequestID();
        out(requestLobbies.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.JoinSelectedLobby) && !response.getRequestID().equals(requestLobbies.getRequestID())) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            int Lobbyid = Jsonable.json2int(response.getContent());
            if (Lobbyid > 0)
                this.id = Lobbyid;
        }catch (RemoteException e){
            throw new ServerException(e.getMessage());
        }
    }
    @Override
    public void postToLiveChat(String playerName, String message) throws LobbyException {
        MessageTcp postMessage = new MessageTcp();
        ChatMessage chatMessage = new ChatMessage(playerName, message, Color.Black); //TODO maybe create a constructor that doesn't need color
        postMessage.setCommand(MessageTcp.MessageCommand.PostToLiveChat); //set command
        postMessage.setContent(chatMessage.toJson()); //set playername as JsonObject
        postMessage.generateRequestID();
        out(postMessage.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object responses
            while (!response.getCommand().equals(MessageTcp.MessageCommand.PostToLiveChat) && !response.getRequestID().equals(postMessage.getRequestID())) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            boolean errorFound = Jsonable.json2boolean(response.getContent());
            if (errorFound) {
                //TODO to implement
            }
        }catch (RemoteException e){
            throw new LobbyException(e.getMessage());
        }
    }

    @Override
    public void postSecretToLiveChat(String sender, String receiver, String message) throws LobbyException{
        MessageTcp postMessage = new MessageTcp();
        PrivateChatMessage chatMessage = new PrivateChatMessage(sender,receiver,message,Color.Black); //TODO maybe create a constructor that doesn't need color
        postMessage.setCommand(MessageTcp.MessageCommand.PostSecretToLiveChat); //set command
        postMessage.setContent(chatMessage.toJson()); //set playername as JsonObject
        postMessage.generateRequestID();
        out(postMessage.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object responses
            while (!response.getCommand().equals(MessageTcp.MessageCommand.PostSecretToLiveChat) && !response.getRequestID().equals(postMessage.getRequestID())) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            boolean errorFound = Jsonable.json2boolean(response.getContent());
            if (errorFound) {
                //TODO to implement
            }
        }catch (RemoteException e){
            throw new LobbyException(e.getMessage());
        }
    }

    @Override
    public void quitGame(String player) throws LobbyException{
        MessageTcp quitMessage = new MessageTcp();
        quitMessage.setCommand(MessageTcp.MessageCommand.Quit); //set command
        quitMessage.setContent(Jsonable.string2json(player));
        quitMessage.generateRequestID();
        out(quitMessage.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object responses
            while (!response.getCommand().equals(MessageTcp.MessageCommand.Quit) && !response.getRequestID().equals(quitMessage.getRequestID())) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            boolean errorFound = Jsonable.json2boolean(response.getContent());
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
        hasStartedMessage.generateRequestID();
        out(hasStartedMessage.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object responses
            while (!response.getCommand().equals(MessageTcp.MessageCommand.MatchHasStarted) && !response.getRequestID().equals(hasStartedMessage.getRequestID())) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            return Jsonable.json2boolean(response.getContent());
        }catch (RemoteException e){
            throw new LobbyException(e.getMessage());
        }
    }

    @Override
    public void postMove(String player, Move move) throws LobbyException {
        MessageTcp postMoveMessage = new MessageTcp();
        JSONObject content = new JSONObject();
        content.put("player",player);
        content.put("move",move.toJson());
        postMoveMessage.setCommand(MessageTcp.MessageCommand.PostMove); //set command
        postMoveMessage.setContent(content);
        postMoveMessage.generateRequestID();
        out(postMoveMessage.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object responses
            while (!response.getCommand().equals(MessageTcp.MessageCommand.PostMove) && !response.getRequestID().equals(postMoveMessage.getRequestID())) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            boolean errorFound = Jsonable.json2boolean(response.getContent());
            if (errorFound) {
                //TODO to implement
            }
        }catch (RemoteException e){
            throw new LobbyException(e.getMessage());
        }

    }

    @Override
    public boolean startGame(String player)throws LobbyException {
        MessageTcp startGame = new MessageTcp();
        startGame.setCommand(MessageTcp.MessageCommand.StartGame); //set command
        startGame.setContent(Jsonable.string2json(player)); //set playername as JsonObject
        startGame.generateRequestID();
        out(startGame.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.StartGame) && !response.getRequestID().equals(startGame.getRequestID())) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            return Jsonable.json2boolean(response.getContent());
        }catch (RemoteException e){
            throw new LobbyException(e.getMessage());
        }
    }
    @Override
    public boolean isLobbyAdmin(String player)throws LobbyException {
        MessageTcp isAdmin = new MessageTcp();
        isAdmin.setCommand(MessageTcp.MessageCommand.IsLobbyAdmin); //set command
        isAdmin.setContent(Jsonable.string2json(player)); //set playername as JsonObject
        isAdmin.generateRequestID();
        out(isAdmin.toString());
        try {
            MessageTcp response = in(); //wait for response by server and create an object response
            while (!response.getCommand().equals(MessageTcp.MessageCommand.IsLobbyAdmin) && !response.getRequestID().equals(isAdmin.getRequestID())) //is there's more than one message in the buffer, then it read until he founds one suitable for the response
                response = in();
            return Jsonable.json2boolean(response.getContent());
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
        return "";
    }
}
