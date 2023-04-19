package it.polimi.ingsw.client.Connection;

import it.polimi.ingsw.shared.IpAddressV4;
import it.polimi.ingsw.shared.Move;
import it.polimi.ingsw.shared.NetworkSettings;

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

    public LobbyTCP(IpAddressV4 ip, int port) {
        this.port = port;
        this.id = port- NetworkSettings.TCPport; //calculate id by reversing creation lobbyPort criteria;
        try {
            Socket lobby = new Socket(ip.toString(), port);
            serverOut = new PrintWriter(lobby.getOutputStream(), true);
            serverIn = new BufferedReader(new InputStreamReader(lobby.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void postToLiveChat(String playerName, String message) throws LobbyException{

    }

    @Override
    public void postSecretToLiveChat(String sender, String receiver, String message) throws LobbyException{

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
