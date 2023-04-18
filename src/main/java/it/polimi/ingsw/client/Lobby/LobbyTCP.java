package it.polimi.ingsw.client.Lobby;

import it.polimi.ingsw.shared.Move;
import it.polimi.ingsw.shared.Chat;

//TODO implement
public class LobbyTCP extends Lobby {
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
        return 0;
    }
}
