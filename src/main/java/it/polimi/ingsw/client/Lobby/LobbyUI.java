package it.polimi.ingsw.client.Lobby;

import it.polimi.ingsw.client.Command;
import it.polimi.ingsw.shared.Chat;

public abstract class LobbyUI extends Thread{
    protected Chat chat;
    protected LobbyStub lobby;
    protected String playerName;
    LobbyUI(String playerName, LobbyStub stub){
        this.playerName = playerName;
        this.lobby = stub;
        this.chat = new Chat();
    }
    protected abstract void executeUserCommand(Command com) throws Exception;
}
