package it.polimi.ingsw.client.Connection;

import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class ServerTCP extends Server {
    private int id;
    private Socket serverSocket;
    private PrintWriter serverOut;
    private BufferedReader serverIn;

    public ServerTCP(IpAddressV4 ip, int port) {
        super(ip, port);
        try {
            serverSocket = new Socket(ip.toString(), port);
            serverOut = new PrintWriter(serverSocket.getOutputStream(), true);
            serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
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
    public boolean login(Client client) {
        MessageTcp login = new MessageTcp();
        login.setCommand(MessageTcp.MessageCommand.Login); //set command
        login.setContent(Jsonable.string2json(client.getPlayerName())); //set playername as JsonObject
        out(login.toString());
        MessageTcp response = new MessageTcp(in()); //wait for response by server and create an object response

        return Jsonable.json2boolean(response.getContent());


    }


    @Override
    public Map<Integer,Integer> getJoinedLobbies(String playerName) throws ServerException {
        MessageTcp requestLobbies = new MessageTcp();
        requestLobbies.setCommand(MessageTcp.MessageCommand.GetJoinedLobbies); //set command
        requestLobbies.setContent(Jsonable.string2json(playerName)); //set playername as JsonObject
        out(requestLobbies.toString());
        MessageTcp response = new MessageTcp(in()); //wait for response by server and create an object response

        return Jsonable.json2mapInt(response.getContent());
    }
    @Override
    public Map<Integer, Integer> getAvailableLobbies()throws ServerException {
        MessageTcp requestLobbies = new MessageTcp();
        requestLobbies.setCommand(MessageTcp.MessageCommand.GetAvailableLobbies); //set command
        out(requestLobbies.toString());
        MessageTcp response = new MessageTcp(in()); //wait for response by server and create an object response
        return Jsonable.json2mapInt(response.getContent());
    }

    @Override
    public void joinRandomLobby(Client client) throws ServerException {
        MessageTcp requestLobbies = new MessageTcp();
        requestLobbies.setCommand(MessageTcp.MessageCommand.JoinRandomLobby); //set command
        out(requestLobbies.toString());
        MessageTcp response = new MessageTcp(in()); //wait for response by server and create an object response
        this.id = Jsonable.json2int(response.getContent());
    }

    @Override
    void createLobby(Client client) throws ServerException {
        MessageTcp requestLobbies = new MessageTcp();
        requestLobbies.setCommand(MessageTcp.MessageCommand.CreateLobby); //set command
        out(requestLobbies.toString());
        MessageTcp response = new MessageTcp(in()); //wait for response by server and create an object response
        this.id = Jsonable.json2int(response.getContent());

    }


    @Override
    public void joinSelectedLobby(Client client, int id)throws ServerException {
        MessageTcp requestLobbies = new MessageTcp();
        requestLobbies.setCommand(MessageTcp.MessageCommand.JoinSelectedLobby); //set command
        requestLobbies.setContent(Jsonable.int2json(id));
        out(requestLobbies.toString());
        MessageTcp response = new MessageTcp(in()); //wait for response by server and create an object response
        this.id= Jsonable.json2int(response.getContent());
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
        MessageTcp quitMessage = new MessageTcp();
        quitMessage.setCommand(MessageTcp.MessageCommand.Quit); //set command
        quitMessage.setContent(Jsonable.string2json(player));
        out(quitMessage.toString());
        MessageTcp response = new MessageTcp(in()); //wait for response by server and create an object responses
        boolean errorFound = Jsonable.json2boolean(response.getContent());
        if(errorFound) {
            //TODO to implement
        }

    }

    @Override
    public boolean matchHasStarted() throws LobbyException {
        MessageTcp hasStartedMessage = new MessageTcp();
        hasStartedMessage.setCommand(MessageTcp.MessageCommand.MatchHasStarted); //set command
        out(hasStartedMessage.toString());
        MessageTcp response = new MessageTcp(in()); //wait for response by server and create an object responses

        return Jsonable.json2boolean(response.getContent());

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
    public int getLobbyID()throws LobbyException {
        return this.id;
    }
}
