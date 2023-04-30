package it.polimi.ingsw.client.View;

import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Color;
import it.polimi.ingsw.shared.model.Move;

import java.util.*;

public class ViewDriver extends View {
    String username;
    static LinkedList<LobbyCommand> commands = new LinkedList<>();
    static LinkedList<LobbySelectionCommand> lobbySelectionCommands = new LinkedList<>();
    static LinkedList<Move> moves = new LinkedList<>();
    private boolean verbous = false;

    @Override
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

    @Override
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

    @Override
    public Move getMoveFromUser() {
        if (moves.isEmpty()) {
            return null;
        } else {
            return moves.pollFirst();
        }
    }

    public void putMove(Move move) {
        moves.addLast(move);
    }

    @Override
    public void notifyInvalidCommand() {
        //do nothing
    }

    @Override
    public void notifyExit() {
        //do nothing
    }

    @Override
    public void showAllMessages(Chat chat) {
        //do nothing
    }

    @Override
    public Map<String, String> getMessageFromUser() {
        Map<String, String> msg = new HashMap<>();
        msg.put("message", "A really meaningful message");
        return msg;
    }

    @Override
    public Map<String, String> getPrivateMessageFromUser() {
        Map<String, String> msg = new HashMap<>();
        msg.put("message", "A really secret message");
        msg.put("receiver", "frigieri");
        return msg;
    }

    @Override
    public void showElement() {
        //do nothing
    }

    @Override
    public void showHelp() {
        //do nothing
    }

    @Override
    public String askUserName() {
        return "frigioggi";
    }

    @Override
    public void showLobbies(Map<Integer, Integer> lobbies, String description) {
        //do nothing
    }

    @Override
    public boolean playAgain() {
        return false;
    }

    @Override
    public void errorMessage(String message) {
        if(verbous)
            System.out.println(Color.coloredString(message,Color.Red));
    }

    @Override
    public void message(String message) {
        if(verbous)
            System.out.println(Color.coloredString(message,Color.Green));
    }

    @Override
    public void setLobbyAdmin(boolean isAdmin) {
        //do nothing
    }
}
