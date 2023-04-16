package it.polimi.ingsw.server.adapters;

import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Move;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyInterface;

import java.rmi.RemoteException;

public class LobbyRMIAdapter implements LobbyInterface {
    private final Lobby lobby;
    public LobbyRMIAdapter(Lobby lobby){
        this.lobby = lobby;
    }
    @Override
    public void postToLiveChat(String playerName, String message) throws Exception {
        lobby.postToLiveChat(playerName,message);
    }

    @Override
    public void postSecretToLiveChat(String sender, String receiver, String message) throws Exception {
        lobby.postSecretToLiveChat(sender, receiver, message);
    }

    @Override
    public Chat updateLiveChat() throws RemoteException {
        return lobby.updateLiveChat();
    }

    @Override
    public void quitGame(String player) throws RemoteException {
        lobby.quitGame(player);

    }

    @Override
    public boolean matchHasStarted() throws RemoteException {
        return lobby.matchHasStarted();
    }

    @Override
    public void postMove(String player, Move move) throws RemoteException {
        lobby.postMove(player, move);
    }

    @Override
    public boolean startGame(String player) throws RemoteException {
        return lobby.startGame(player);
    }

    @Override
    public boolean isLobbyAdmin(String player) throws RemoteException {
        return lobby.isLobbyAdmin(player);
    }

    @Override
    public int getID() throws RemoteException {
        return lobby.getID();
    }
}
