package it.polimi.ingsw.client.Connection;

import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.IpAddressV4;
import it.polimi.ingsw.shared.Move;

import java.util.LinkedList;
import java.util.Map;


public class ConnectionStub extends Server {
    static LinkedList<String> callsToStub;
    boolean verbous = false;
    public ConnectionStub(){
        super(new IpAddressV4((byte) 127, (byte) 0, (byte) 0, (byte) 1),80);
        callsToStub = new LinkedList<>();
    }
    @Override
    public boolean login(Client client) {
        callsToStub.addLast(client.getPlayerName()+" login");
        if(verbous)
            System.out.println(client.getPlayerName()+" login");
        return true;
    }

    @Override
    public void joinRandomLobby(Client client) throws ServerException {
        callsToStub.addLast(client.getPlayerName()+" random login");
        if(verbous)
            System.out.println(client.getPlayerName()+" random login");
    }

    @Override
    public void createLobby(Client client) throws ServerException {
        callsToStub.addLast(client.getPlayerName()+" create lobby");
        if(verbous)
            System.out.println(client.getPlayerName()+" create lobby");
    }

    @Override
    public Map<Integer, Integer> getAvailableLobbies() throws ServerException {
        callsToStub.addLast("available lobbies");
        if(verbous)
            System.out.println("available lobbies");
        return null;
    }

    @Override
    public Map<Integer, Integer> getJoinedLobbies(String playerName) throws ServerException {
        callsToStub.addLast("joined lobbies");
        if(verbous)
            System.out.println("joined lobbies");
        return null;
    }

    @Override
    public void joinSelectedLobby(Client client, int id) throws ServerException {
        callsToStub.addLast(client.getPlayerName()+" specific lobby "+id);
        if(verbous)
            System.out.println(client.getPlayerName()+" specific lobby "+id);
    }

    @Override
    public void postToLiveChat(String playerName, String message) throws LobbyException {
        if(verbous)
            System.out.println(playerName+" posted "+message);
        callsToStub.addLast(playerName+" posted "+message);
    }

    @Override
    public void postSecretToLiveChat(String sender, String receiver, String message) throws LobbyException {
        if(verbous)
            System.out.println(sender+" posted secret "+message+ " for "+receiver);
        callsToStub.addLast(sender+" posted secret "+message+ " for "+receiver);
    }

    @Override
    public void quitGame(String player) throws LobbyException {
        if(verbous)
            System.out.println(player + " quit");
        callsToStub.addLast(player + " quit");
    }

    @Override
    public boolean matchHasStarted() throws LobbyException {
        return true; //TODO
    }

    @Override
    public void postMove(String player, Move move) throws LobbyException {
        callsToStub.addLast(player + " posted move " + move);
        if(verbous)
            System.out.println(player + " posted move " + move);
    }

    @Override
    public boolean startGame(String player) throws LobbyException {
        if(verbous)
            System.out.println(player + " started game");
        callsToStub.addLast(player + " started game");
        return true;
    }

    @Override
    public boolean isLobbyAdmin(String player) throws LobbyException {
        if(verbous)
            System.out.println(player + " asked lobby admin");
        callsToStub.addLast(player + " asked lobby admin");
        return true;
    }

    @Override
    public int getLobbyID() throws LobbyException {
        return 1;
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

    public LinkedList<String> getCallsToStub() {
        return new LinkedList<>(callsToStub);
    }
}

