package it.polimi.ingsw.client.Connection;

import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.IpAddressV4;
import it.polimi.ingsw.shared.Move;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;


public class ConnectionStub extends Server {
    static Queue<String> callsToStub;
    boolean verbous;
    public ConnectionStub(){
        super(new IpAddressV4((byte) 127, (byte) 0, (byte) 0, (byte) 1),80);
        callsToStub = new PriorityQueue<>();
        verbous = true;
    }
    @Override
    public boolean login(Client client) {
        callsToStub.add(client.getPlayerName()+" login");
        if(verbous)
            System.out.println(client.getPlayerName()+" login");
        return true;
    }

    @Override
    public void joinRandomLobby(Client client) throws ServerException {
        callsToStub.add(client.getPlayerName()+" random login");
        if(verbous)
            System.out.println(client.getPlayerName()+" random login");
    }

    @Override
    public void createLobby(Client client) throws ServerException {
        callsToStub.add(client.getPlayerName()+" create lobby");
        if(verbous)
            System.out.println(client.getPlayerName()+" create lobby");
    }

    @Override
    public Map<Integer, Integer> getAvailableLobbies() throws ServerException {
        callsToStub.add("available lobbies");
        if(verbous)
            System.out.println("available lobbies");
        return null;
    }

    @Override
    public Map<Integer, Integer> getJoinedLobbies(String playerName) throws ServerException {
        callsToStub.add("joined lobbies");
        if(verbous)
            System.out.println("joined lobbies");
        return null;
    }

    @Override
    public void joinSelectedLobby(Client client, int id) throws ServerException {
        callsToStub.add(client.getPlayerName()+" specific lobby "+id);
        if(verbous)
            System.out.println(client.getPlayerName()+" specific lobby "+id);
    }

    @Override
    public void postToLiveChat(String playerName, String message) throws LobbyException {
        if(verbous)
            System.out.println(playerName+" posted "+message);
        callsToStub.add(playerName+" posted "+message);
    }

    @Override
    public void postSecretToLiveChat(String sender, String receiver, String message) throws LobbyException {
        if(verbous)
            System.out.println(sender+" posted secret "+message+ " for "+receiver);
        callsToStub.add(sender+" posted secret "+message+ " for "+receiver);
    }

    @Override
    public void quitGame(String player) throws LobbyException {
        if(verbous)
            System.out.println(player + " quit");
        callsToStub.add(player + " quit");
    }

    @Override
    public boolean matchHasStarted() throws LobbyException {
        return true; //TODO
    }

    @Override
    public void postMove(String player, Move move) throws LobbyException {
        callsToStub.add(player + " posted move " + move);
        if(verbous)
            System.out.println(player + " posted move " + move);
    }

    @Override
    public boolean startGame(String player) throws LobbyException {
        if(verbous)
            System.out.println(player + " posted started game");
        return true;
    }

    @Override
    public boolean isLobbyAdmin(String player) throws LobbyException {
        if(verbous)
            System.out.println(player + " asked lobby admin");
        return true;
    }

    @Override
    public int getLobbyID() throws LobbyException {
        return 0;
    }

    public Queue<String> getCallsToStub() {
        return new PriorityQueue<>(callsToStub);
    }
}

