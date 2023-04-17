package it.polimi.ingsw.client.JoinLobby;

import it.polimi.ingsw.client.Lobby.Lobby;
import it.polimi.ingsw.client.Lobby.LobbyUI;
import it.polimi.ingsw.server.clientonserver.Client;

public abstract class JoinLobbyUI extends Thread{
    //Variables
    protected String playerName;
    protected Server server;
    protected Client client;
    protected LobbyUI match;

    //Constructor
    JoinLobbyUI(Server server){
        this.server = server;
    }

    //Methods
    public abstract String askUserName();
    public abstract boolean tryLogin(int tries);
    public boolean login(){
        return server.login(client);
    }
    public abstract LobbyUI getLobbyUI();
    public abstract LobbySelectionCommand askLobby();
    public Lobby joinLobby(LobbySelectionCommand command){
        switch (command){
            case Random:
                return server.joinRandomLobby(client);
            case Number:
                return server.joinSelectedLobby(client,command.getID());
            default:
                return null;
        }
    }
    public abstract void showJoinedLobbies();
    public abstract void showLobbyList();
    public abstract boolean playAgain();
    @Override
    public void run(){
        boolean play = true;
        while(play){
            playerName = askUserName();
            client = server.generateClient(playerName);
            tryLogin(30);
            showLobbyList();
            showJoinedLobbies();
            LobbyUI match = getLobbyUI();
            match.start();
            try {
                match.join();
            } catch (InterruptedException e) {
                play = false;
            }
            play = playAgain();
        }
    }

    protected enum LobbySelectionCommand{
        Invalid(),
        Number(),
        Random();
        private int id = -1;
        public void setId(int id){
            this.id = id;
        }
        public int getID(){
            return id;
        }
    }

}
