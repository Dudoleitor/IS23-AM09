package it.polimi.ingsw.client.LobbySelection;

import it.polimi.ingsw.client.Lobby.Lobby;
import it.polimi.ingsw.client.Lobby.LobbyException;
import it.polimi.ingsw.client.Lobby.MatchCLI;
import it.polimi.ingsw.client.Lobby.Match;
import it.polimi.ingsw.server.clientonserver.Client;

import java.util.List;
import java.util.Map;

public class LobbySelection extends Thread{
    //Variables
    private String playerName;
    private Server server;
    private Client client;
    private LobbySelectionView view;

    //Constructor
    public LobbySelection(Server server, LobbySelectionView view){
        this.server = server;
        this.view = view;
    }

    //Methods

    /**
     * Login client to server
     * @return true if successful
     */
    private boolean login(){
        return server.login(client);
    }

    /**
     * Join a lobby accordingly to the player command
     * @param command
     * @return the Lobby object that will handle the connection to the
     * Lobby on the server
     */
    private Lobby joinLobby(LobbySelectionCommand command) throws ServerException {
        switch (command){
            case Random:
                return server.joinRandomLobby(client);
            case Number:
                return server.joinSelectedLobby(client,command.getID());
            default:
                return null;
        }
    }

    /**
     * Try login tries times
     * @param tries
     * @return true if successful
     */
    private boolean tryLogin(int tries, int seconds){
        boolean logged = false;
        for(int attempt = 0; attempt < tries && !logged; attempt++){
            logged = login(); //get previous sessions if present
            if(!logged){
                view.errorMessage("Connection Error, retying in "+seconds+" seconds");
                try {
                    sleep(seconds*1000);
                } catch (InterruptedException e) {
                    return false;
                }
            }
        }
        return logged;
    }

    /**
     * Get a List of the lobby IDs where the player is in
     * @return the list of lobby IDs
     */
    private Map<Integer,Integer> getPreviousSessions() throws ServerException {
        return server.getJoinedLobbies(playerName);
    }

    /**
     * Get all the lobbies that are available for the client to join
     * @return a map of the lobby IDs to the Number of player present
     */
    private Map getAvailableLobbies() throws ServerException {
        return server.getAvailableLobbies();
    }

    /**
     * Get the match thread to start in order to play
     * @return the Match object
     */
    private Match getMatch() throws ServerException {
        LobbySelectionCommand command = LobbySelectionCommand.Invalid;
        Lobby lobby = null;
        while(command == LobbySelectionCommand.Invalid){
            command = view.askLobby();
            lobby = joinLobby(command);
            if(command == LobbySelectionCommand.Invalid){
                view.errorMessage("Input a valid id (must be a number)");
            }
        }
        try{
            view.message("You joined #"+ lobby.getID()+" lobby!");
        }
        catch (LobbyException e){
            throw new ServerException("Error in Server");
        }
        //Create the lobbyUI object and start the match
        return new Match(playerName, lobby, new MatchCLI());
    }

    @Override
    public void run(){
        boolean play = true;
        while(play){
            playerName = view.askUserName();
            client = server.generateClient(playerName);
            view.greet(playerName);
            boolean successfulLogin = tryLogin(3,2);
            if(successfulLogin){
                try{
                    view.showLobbies(getPreviousSessions(),"The lobbies you already joined");
                    view.showLobbies(getAvailableLobbies(), "The lobbies that are available");
                    Match match = getMatch();
                    match.start();
                    try {
                        match.join();
                    } catch (InterruptedException e) {
                        play = false;
                    }
                } catch (ServerException e) {
                    view.errorMessage("Something went wrong connecting to client");
                    play = false;
                }
                play = view.playAgain();
            }
            else{
                view.errorMessage("It was impossible to connect to server");
                play = false;
            }
        }
    }

}
