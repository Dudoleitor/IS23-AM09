package it.polimi.ingsw.client.LobbySelection;

import it.polimi.ingsw.client.Lobby.Lobby;
import it.polimi.ingsw.client.Lobby.LobbyCLI;
import it.polimi.ingsw.client.Lobby.LobbyUI;
import it.polimi.ingsw.server.clientonserver.Client;

import java.util.List;
import java.util.Map;

public class LobbySelection extends Thread{
    //Variables
    private String playerName;
    private Server server;
    private Client client;
    private LobbyUI match;
    private LobbySelectionView view;

    //Constructor
    public LobbySelection(Server server, LobbySelectionView view){
        this.server = server;
        this.view = view;
    }

    //Methods
    private boolean login(){
        return server.login(client);
    }
    private Lobby joinLobby(LobbySelectionCommand command){
        switch (command){
            case Random:
                return server.joinRandomLobby(client);
            case Number:
                return server.joinSelectedLobby(client,command.getID());
            default:
                return null;
        }
    }
    private boolean tryLogin(int tries){
        boolean logged = false;
        //Try to log in for 30s (1 try each second)
        for(int attempt = 0; attempt < tries && !logged; tries++){
            logged = login(); //get previous sessions if present
            if(!logged){
                view.errorMessage("Connection Error, retying in 1 second");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    return false;
                }
            }
        }
        return logged;
    }

    private List<Integer> getPreviousSessions(){
        return server.getJoinedLobbies(playerName);
    }
    private Map getAvailableLobbies(){
        return server.showAvailableLobbbies();
    }

    private LobbyUI getLobbyUI(){
        LobbySelectionCommand command = LobbySelectionCommand.Invalid;
        Lobby lobby = null;
        while(command == LobbySelectionCommand.Invalid){
            command = view.askLobby();
            lobby = joinLobby(command);
            if(command == LobbySelectionCommand.Invalid){
                view.errorMessage("Input a valid id (must be a number)");
            }
        }
        view.message("You joined #"+ lobby.getID()+" lobby!");
        //Create the lobbyUI object and start the match
        return new LobbyCLI(playerName,lobby);
    }

    @Override
    public void run(){
        boolean play = true;
        while(play){
            playerName = view.askUserName();
            client = server.generateClient(playerName);
            tryLogin(30);
            view.showJoinedLobbies(getPreviousSessions());
            view.showLobbyList(getAvailableLobbies());
            LobbyUI match = getLobbyUI();
            match.start();
            try {
                match.join();
            } catch (InterruptedException e) {
                play = false;
            }
            play = view.playAgain();
        }
    }

}
