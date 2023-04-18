package it.polimi.ingsw.client.Lobby;

import it.polimi.ingsw.shared.Move;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.RemoteInterfaces.LobbyInterface;

public class LobbyRMI extends Lobby {
    private LobbyInterface lobby;
    public LobbyRMI(LobbyInterface lobby){
        this.lobby = lobby;
    }
    @Override
    public void postToLiveChat(String playerName, String message) {
        try {
            lobby.postToLiveChat(playerName,message);
        } catch (Exception e) {
            //TODO
        }
    }

    @Override
    public void postSecretToLiveChat(String sender, String receiver, String message) {
        try{
            lobby.postSecretToLiveChat(sender,receiver,message);
        }
        catch (Exception e){
            //TODO
        }
    }

    @Override
    public Chat updateLiveChat() {
        Chat updatedChat = null;
        try{
            updatedChat = lobby.updateLiveChat();
        }
        catch (Exception e){
            //TODO
        }
        return updatedChat;
    }

    @Override
    public void quitGame(String player) {
        try{
            lobby.quitGame(player);
        } catch (Exception e) {
            //TODO
        }
    }

    @Override
    public boolean matchHasStarted() {
        boolean started = false;
        try{
            started=  lobby.matchHasStarted();
        } catch (Exception e) {
            //TODO
        }
        return started;
    }

    @Override
    public void postMove(String player, Move move) {
        try {
            lobby.postMove(player,move);
        } catch (Exception e) {
            //TODO
        }
    }

    @Override
    public boolean startGame(String player) {
        boolean hasStarted = false;
        try{
            hasStarted = lobby.startGame(player);
        } catch (Exception e) {
            //TODO
        }
        return hasStarted;
    }

    @Override
    public boolean isLobbyAdmin(String player) {
        boolean result = false;
        try{
            result = lobby.isLobbyAdmin(player);
        } catch (Exception e) {
            //TODO
        }
        return result;
    }

    @Override
    public int getID() {
        int id = 0;
        try {
            id = lobby.getID();
        } catch (Exception e) {
            //TODO
        }
        return id;
    }
}
