package it.polimi.ingsw.server.adapters;

import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Move;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyRemoteInterface;

import java.rmi.RemoteException;

public class LobbySocketAdapter { //TODO it will implement an interface
    private Lobby lobby;
    public LobbySocketAdapter(Lobby lobby){
        this.lobby = lobby;
    }
    public void postToLiveChat(String playerName, String message) throws Exception {
        //TODO
    }


    public void postSecretToLiveChat(String sender, String receiver, String message) throws Exception {
        //TODO
    }

    public Chat updateLiveChat() throws RemoteException {
        return null;
        //TODO
    }


    public void quitGame(String player) throws RemoteException {
        //TODO
    }


    public boolean matchHasStarted() throws RemoteException {
        return false;
        //TODO
    }


    public void postMove(String player, Move move) throws RemoteException {
        //TODO
    }


    public boolean startGame(String player) throws RemoteException {
        return false;
        //TODO
    }


    public boolean isLobbyAdmin(String player) throws RemoteException {
        return false;
        //TODO
    }


    public int getID() throws RemoteException {
        return 0;
        //TODO
    }
}
