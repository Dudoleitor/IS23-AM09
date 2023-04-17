package it.polimi.ingsw.client.Lobby;

import it.polimi.ingsw.client.Command;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.Chat;

public abstract class LobbyUI extends Thread{
    protected Chat chat;
    protected Lobby lobby;
    protected String playerName;
    protected Client client;
    LobbyUI(String playerName, Lobby stub){
        this.playerName = playerName;
        this.lobby = stub;
        this.chat = new Chat();
    }
    protected abstract void executeUserCommand(Command com) throws Exception;
}
