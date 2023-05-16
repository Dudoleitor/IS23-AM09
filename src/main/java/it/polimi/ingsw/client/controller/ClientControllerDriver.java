package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.connection.Server;
import it.polimi.ingsw.client.controller.cli.LobbyCommand;
import it.polimi.ingsw.client.controller.cli.LobbySelectionCommand;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Color;
import it.polimi.ingsw.shared.model.Board;
import it.polimi.ingsw.shared.model.Move;
import it.polimi.ingsw.shared.model.Shelf;

import java.util.*;

public class ClientControllerDriver implements ClientController {
    String username;
    static LinkedList<LobbyCommand> commands = new LinkedList<>();
    static LinkedList<LobbySelectionCommand> lobbySelectionCommands = new LinkedList<>();
    static LinkedList<Move> moves = new LinkedList<>();
    private boolean verbous = false;

    public LobbyCommand askCommand() {
        if (commands.isEmpty()) {
            if(verbous)
                System.out.println(Color.coloredString("FORCED EXIT",Color.Red));
            return LobbyCommand.Exit;
        } else {
            if(verbous)
                System.out.println(Color.coloredString(commands.peek().toString(),Color.Green));
            return commands.pollFirst();
        }
    }

    public void putCommand(LobbyCommand command) {
        commands.addLast(command);
    }

    public LobbySelectionCommand askLobby() {
        if (lobbySelectionCommands.isEmpty()) {
            return LobbySelectionCommand.Invalid;
        } else {
            return lobbySelectionCommands.pollFirst();
        }
    }

    public void putLobbySelectionCommand(LobbySelectionCommand command) {
        lobbySelectionCommands.addLast(command);
    }

    public Move getMoveFromUser(Board board, Shelf shelf) {
        if (moves.isEmpty()) {
            return null;
        } else {
            return moves.pollFirst();
        }
    }

    public void putMove(Move move) {
        moves.addLast(move);
    }

    public void notifyInvalidCommand() {
        System.out.println("Invalid command");
        //do nothing
    }

    public void notifyExit() {
        //do nothing
    }

    public void showAllMessages(Chat chat) {
        //do nothing
    }

    public Map<String, String> getMessageFromUser() {
        System.out.println("A really meaningful message");
        Map<String, String> msg = new HashMap<>();
        msg.put("message", "A really meaningful message");
        return msg;
    }

    public Map<String, String> getPrivateMessageFromUser() {
        Map<String, String> msg = new HashMap<>();
        msg.put("message", "A really secret message");
        msg.put("receiver", "frigieri");
        return msg;
    }

    public void showHelp() {
        //do nothing
    }

    public String askUserName() {
        return "frigioggi";
    }

    public void showLobbies(Map<Integer, Integer> lobbies, String description) {
        //do nothing
    }

    public boolean playAgain() {
        return false;
    }

    public void errorMessage(String message) {
        if(verbous)
            System.out.println(Color.coloredString(message,Color.Red));
    }

    public void message(String message) {
        if(verbous)
            System.out.println(Color.coloredString(message,Color.Green));
    }

    public void setLobbyAdmin(boolean isAdmin) {
        //do nothing
    }

    public void endGame(Map<String, Integer> leaderBoard, String playername, Map<String, Shelf> playerShelves, Board board) {

    }

    @Override
    public void startClient() {

    }

    @Override
    public Server getServer() {
        return null;
    }

    @Override
    public void setServer(Server server) {

    }

    @Override
    public Client getClient() {
        return null;
    }

    @Override
    public void setClient(Client client) {

    }
}
