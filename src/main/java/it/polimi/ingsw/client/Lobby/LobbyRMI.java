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
    public void postToLiveChat(String playerName, String message) throws LobbyException {
        try {
            lobby.postToLiveChat(playerName,message);
        } catch (Exception e) {
            throw new LobbyException("Error in Lobby");
        }
    }

    @Override
    public void postSecretToLiveChat(String sender, String receiver, String message) throws LobbyException{
        try{
            lobby.postSecretToLiveChat(sender,receiver,message);
        }
        catch (Exception e){
            throw new LobbyException("Error in Lobby");
        }
    }

    @Override
    public Chat updateLiveChat() throws LobbyException{
        Chat updatedChat = null;
        try{
            updatedChat = lobby.updateLiveChat();
        }
        catch (Exception e){
            throw new LobbyException("Error in Lobby");
        }
        return updatedChat;
    }

    @Override
    public void quitGame(String player){
        try{
            lobby.quitGame(player);
        } catch (Exception e) {
            //do nothing and quit anyway
        }
    }

    @Override
    public boolean matchHasStarted() throws LobbyException{
        boolean started = false;
        try{
            started=  lobby.matchHasStarted();
        } catch (Exception e) {
            throw new LobbyException("Error in Lobby");
        }
        return started;
    }

    @Override
    public void postMove(String player, Move move) throws LobbyException{
        try {
            lobby.postMove(player,move);
        } catch (Exception e) {
            throw new LobbyException("Error in Lobby");
        }
    }

    @Override
    public boolean startGame(String player) throws LobbyException{
        boolean hasStarted = false;
        try{
            hasStarted = lobby.startGame(player);
        } catch (Exception e) {
            throw new LobbyException("Error in Lobby");
        }
        return hasStarted;
    }

    @Override
    public boolean isLobbyAdmin(String player) throws LobbyException{
        boolean result = false;
        try{
            result = lobby.isLobbyAdmin(player);
        } catch (Exception e) {
            throw new LobbyException("Error in Lobby");
        }
        return result;
    }

    @Override
    public int getID() throws LobbyException{
        int id = 0;
        try {
            id = lobby.getID();
        } catch (Exception e) {
            throw new LobbyException("Error in Lobby");
        }
        return id;
    }
}
