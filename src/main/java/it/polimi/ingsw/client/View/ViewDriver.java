package it.polimi.ingsw.client.View;

import it.polimi.ingsw.client.View.LobbyCommand;
import it.polimi.ingsw.client.View.LobbySelectionCommand;
import it.polimi.ingsw.client.View.View;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Move;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class ViewDriver extends View {
    String username;
    static Queue<LobbyCommand> commands = new PriorityQueue<>();
    static Queue<LobbySelectionCommand> lobbySelectionCommands = new PriorityQueue<>();
    static Queue<Move> moves = new PriorityQueue<>();

    @Override
    public LobbyCommand askCommand() {
        if (commands.isEmpty()) {
            return LobbyCommand.Invalid;
        } else {
            return commands.poll();
        }
    }

    public void putCommand(LobbyCommand command) {
        commands.add(command);
    }

    @Override
    public LobbySelectionCommand askLobby() {
        if (lobbySelectionCommands.isEmpty()) {
            return LobbySelectionCommand.Invalid;
        } else {
            return lobbySelectionCommands.poll();
        }
    }

    public void putLobbySelectionCommand(LobbySelectionCommand command) {
        lobbySelectionCommands.add(command);
    }

    @Override
    public Move getMoveFromUser() {
        if (moves.isEmpty()) {
            return null;
        } else {
            return moves.poll();
        }
    }

    public void putMove(Move move) {
        moves.add(move);
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
        //do nothing
    }

    @Override
    public void message(String message) {
        //do nothing
    }

    @Override
    public void setLobbyAdmin(boolean isAdmin) {
        //do nothing
    }
}
