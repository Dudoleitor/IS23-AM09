package it.polimi.ingsw.server.adapters;

import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Move;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyInterface;

import java.rmi.RemoteException;

public class LobbySocketAdapter implements LobbyInterface { //TODO it will implement an interface
    private Lobby lobby;

    public LobbySocketAdapter(Lobby lobby){
        this.lobby = lobby;
    }
    @Override
    public void postToLiveChat(String playerName, String message) throws Exception {
        //TODO
    }

    @Override
    public void postSecretToLiveChat(String sender, String receiver, String message) throws Exception {
        //TODO
    }
    @Override
    public Chat updateLiveChat() throws Exception {
        return null;
        //TODO
    }

    @Override
    public void quitGame(String player) throws Exception {
        //TODO
    }

    @Override
    public boolean matchHasStarted() throws Exception {
        return false;
        //TODO
    }

    @Override
    public void postMove(String player, Move move) throws Exception {
        //TODO
    }

    @Override
    public boolean startGame(String player) throws Exception {
        return false;
        //TODO
    }

    @Override
    public boolean isLobbyAdmin(String player) throws Exception {
        return false;
        //TODO
    }

    @Override
    public int getID() throws Exception {
        return 0;
        //TODO
    }
}
