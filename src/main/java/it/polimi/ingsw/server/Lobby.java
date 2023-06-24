package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.*;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.shared.RemoteInterfaces.ServerLobbyInterface;
import it.polimi.ingsw.shared.model.Move;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

public class Lobby extends UnicastRemoteObject implements ServerLobbyInterface, NetworkExceptionHandler {
    private final int id;
    private final List<Client> clients = new ArrayList<>();
    private final List<String> disconnectedClients;
    private boolean started = false;
    private final Chat chat;
    private Controller controller;

    /* Note: methods using the list clients need to be synchronized:
    the pings sent by the thread can result in the handleNetworkException method
    being called.
    In addition to that, multiple clients can send requests to the lobby and
    they must send commands to the model one at a time. */
    private final LobbyPingSender pingSender;

    public Lobby(Client firstPlayer, int id) throws RemoteException {
        super();
        this.disconnectedClients = new ArrayList<>();
        this.clients.add(firstPlayer);
        firstPlayer.setExceptionHandler(this);
        this.id = id;
        this.chat = new Chat();
        this.pingSender = new LobbyPingSender(this);
        pingSender.start();
    }

    /**
     * This method is used to notify the clients that something happened
     * and send the list of connected client.
     * @param message message to send before the list, ignored if null
     */
    private void informAboutConnectedClients(String message) {
        final List<Client> clientList = getClients();
        // Sending chat message
        final String playersConnectedMsg = "Players in lobby: " +
                clientList.stream()
                        .reduce("",
                                (s, c1) -> s + c1.getPlayerName() + " ", String::concat
                        );
        for (Client c : clientList) {
            if(message!= null)
                c.postChatMessage("Server", message + "\n" + playersConnectedMsg);
            else
                c.postChatMessage("Server", playersConnectedMsg);
        }
    }

    /**
     * add a player to lobby
     *
     * @param client is the player object to add to the lobby
     */
    public synchronized void addPlayer(Client client) throws RemoteException {
        synchronized (client) {
            if (clients.contains(client)) //if player logged in previously
                return;
            if (isTerminated())
                throw new RemoteException("Game terminated");

            if (disconnectedClients.contains(client.getPlayerName().toLowerCase())) {
                disconnectedClients.remove(client.getPlayerName().toLowerCase());
                clients.add(client);
                client.setExceptionHandler(this);
                if (controller != null)
                    controller.clientReconnected(client);
                if (started)
                    client.gameStarted(false);
                client.refreshChat(chat);
                informAboutConnectedClients(client.getPlayerName() + " joined the lobby");
                return;
            }

            if (started)
                throw new RuntimeException("Cannot add a client after the game has stated");

            if (clients.size() < GameSettings.maxSupportedPlayers) { //checks lobby isn't already full
                clients.add(client);
                client.setExceptionHandler(this);
                client.refreshChat(chat);
                informAboutConnectedClients(client.getPlayerName() + " joined the lobby");
            } else
                throw new RuntimeException("Lobby already full");
        }
    }

    /**
     * @return true is the lobby is ready to start
     */
    public synchronized boolean isReady() {
        return clients.size() >= GameSettings.minSupportedPlayers && !started;
    }

    /**
     * @return true is the lobby is full of players for it's capacity
     */
    public synchronized boolean isFull() {
        return clients.size() >= GameSettings.maxSupportedPlayers;
    }

    /**
     * @return list of players in this lobby
     */
    public synchronized ArrayList<Client> getClients() {
        return new ArrayList<>(clients);
    }

    public synchronized List<String> getPlayerNames() {
        return clients.stream().
                map(Client::getPlayerName).
                collect(Collectors.toList());
    }

    /**
     * @return every message in that lobby
     */
    public synchronized Chat getChat() {
        return new Chat(chat);
    }

    /**
     * @return true if no players are in lobby
     */
    public synchronized boolean isEmpty() {
        return clients.size() == 0;
    }

    /**
     * Tells who the lobby admin is
     *
     * @return the name of the lobby admin
     */
    public synchronized String getLobbyAdmin() {
        if (clients.size() == 0) {
            throw new RuntimeException("No Players while trying to get lobby admin");
        } else {
            return clients.get(0).getPlayerName();
        }
    }

    /**
     * Return the list of clients that were previously disconnected.
     *
     * @return List of String, playerNames in lowercase
     */
    public List<String> getDisconnectedClients() {
        return new ArrayList<>(disconnectedClients);
    }

    /**
     * @return true if match has started
     */
    @Override
    public boolean matchHasStarted() {
        return started;
    }

    /**
     * start the lobby if it is ready and the player who has asked is admin
     *
     * @param player               is the player who asked to start the game
     * @param erasePreviousMatches if true, the previous match with the same players will be erased
     *                             if false, the previous match with the same players will be loaded
     * @return true if successful
     */
    @Override
    public synchronized boolean startGame(String player, boolean erasePreviousMatches) {
        pingSender.ping();  // Making sure no client disconnected

        try {
            if (!isReady() || !getLobbyAdmin().equals(player))
                return false;
        } catch (RuntimeException e) {
            return false;
        }

        started = true;
        disconnectedClients.removeIf(x -> true);  /* If a client disconnects before the controller is initialized,
        it won't be added to the match later */

        controller = null;

        // Determining if there is a previous match with the same players
        final String prevSaveFilename = Controller.getFileName(clients
                .stream()
                .map(Client::getPlayerName)
                .collect(Collectors.toList()));
        final Path prevSavePath = Path.of("./" + prevSaveFilename);

        boolean loadedFromSave = false;
        if (Files.exists(prevSavePath) && erasePreviousMatches) {
            try {
                Files.delete(prevSavePath);
            } catch (IOException e) {
                System.err.println("Error while deleting previous match: " + e.getMessage());
            }
        }

        if (Files.exists(prevSavePath) && !erasePreviousMatches) {
            try (InputStream stream = Files.newInputStream(prevSavePath)) {
                final Reader reader = new InputStreamReader(stream);
                final JSONObject gameStatus = (JSONObject) new JSONParser().parse(reader);
                controller = new Controller(gameStatus, new ArrayList<>(clients));
                loadedFromSave = true;
            } catch (IOException | ParseException | JsonBadParsingException e) {
                System.err.println("Error while loading previous match: " + e.getMessage());
                try {
                    Files.deleteIfExists(prevSavePath);
                } catch (IOException ex) {
                    System.err.println("Error while deleting invalid match: " + ex.getMessage());
                }
                controller = null;
            }
        }

        if (controller == null) {  // No valid previous match found
            System.out.println("Loaded a new match, lobby #" + id);
            controller = new Controller(new ArrayList<>(clients));  // List is given by copy
        } else {
            System.out.println("Loaded previous match, lobby #" + id);
        }

        for (Client c : clients)
            c.gameStarted(!loadedFromSave);
        return true;
    }

    /**
     * @param playerName
     * @return true if playerName is the name of the lobby admin
     */
    @Override
    public synchronized boolean isLobbyAdmin(String playerName) {
        if (isEmpty()) {
            return false;
        } else {
            return clients.get(0).getPlayerName().equals(playerName);
        }
    }

    /**
     * @param playerName is the player that is sending a message
     * @param message    is the content
     */
    @Override
    public synchronized void postToLiveChat(String playerName, String message) {
        if (playerName == null || message == null) {
            throw new RuntimeException("Wrong format of message");
        }
        chat.addMessage(playerName, message);
        for (Client client : clients) {
            client.postChatMessage(playerName, message);
        }
    }

    /**
     * @param sender   is the player that is sending a message
     * @param receiver is the player that is sending a message
     * @param message  is the content
     */
    @Override
    public synchronized void postSecretToLiveChat(String sender, String receiver, String message) {
        if (sender == null || receiver == null || message == null) {
            throw new RuntimeException("Wrong format of message");
        }
        chat.addSecret(sender, receiver, message);

        Client client = clients.stream().filter(x -> x.getPlayerName().equals(receiver)).findFirst().orElse(null);
        if (client == null) return;
        client.postChatMessage("PRIVATE " + sender, message);
    }

    @Override
    public synchronized void quitGame(String player) {
        Optional<Client> clientOptional = clients.stream().filter(x -> x.getPlayerName().equalsIgnoreCase(player)).findFirst();
        if (clientOptional.isEmpty()) return;
        Client client = clientOptional.get();
        disconnectClient(client);
    }

    @Override
    public synchronized void postMove(String player, JSONObject moveJson) {
        Client client = null;
        final Move move = new Move(moveJson);
        try {
            client = clients.stream().filter(x -> x.getPlayerName().equals(player)).findFirst().orElse(null);
            if (client != null) {
                controller.moveTiles(player, move);
            }
        } catch (ControllerGenericException e) {
            if (client != null)
                client.postChatMessage("Server", e.getMessage());
            throw e;
        }
    }

    @Override
    public int getID() {
        return this.id;
    }

    /**
     * This method is used to observe the player supposed
     * to play in the current turn.
     *
     * @return String name of the player
     */
    @Override
    public synchronized String getCurrentPlayer() {
        return controller.getCurrentPlayerName();
    }

    /**
     * This function is used to handle network exceptions thrown by RMI or the socket.
     * The function disconnects the client and sets the player as inactive in the controller.
     *
     * @param client Client object
     * @param e      Exception thrown
     */
    public void handleNetworkException(Client client, Exception e) {
        System.err.println("Exception thrown (in lobby) while trying to reach client " + client.getPlayerName() + ": " + e.getMessage());
        disconnectClient(client);
    }

    /**
     * This function is used to start a timer when there are not enough players in the lobby.
     * The timer is used to automatically declare victory to the players that are still connected
     * if nobody reconnects in a certain amount of time.
     */
    public void startAutomaticVictoryTimer() {
        final Object lock = this;
        new Thread(() -> {
            try {
                Thread.sleep(GameSettings.automaticVictoryTimeoutSec * 1000);
            } catch (InterruptedException e) {
                return;
            }
            synchronized (lock) {
                if (controller!=null && !clients.isEmpty())
                    controller.endGameNotEnoughConnected();
            }
        }).start();
    }

    /**
     * The function disconnects the client and sets the player as inactive in the controller.
     * A timer is started to automatically declare victory if needed.
     *
     * @param client Client object
     */
    public synchronized void disconnectClient(Client client) {
        client.disconnect();
        if (!clients.remove(client)) {
            System.out.println("Called disconnectClient but client " + client.getPlayerName() + " is not in lobby #" + id + ", ignoring");
            return;
        }

        String message = client.getPlayerName() + " disconnected from the lobby";
        if(started)
            message = message + "\nNot enough players online, if nobody reconnects in " + GameSettings.automaticVictoryTimeoutSec + " seconds the game will end";
        informAboutConnectedClients(message);

        if (controller != null && started) {
            /* If game is started and controller is null it's still being initialized,
                    we won't disconnect the player. An exception will be thrown later
                    and the player will be disconnected then. */
            disconnectedClients.add(client.getPlayerName().toLowerCase());
            controller.clientDisconnected(client);  // It's ok if a network exception is thrown here
            System.out.println("Disconnected client " + client.getPlayerName() + " from lobby #" + id);


            if (clients.size()<GameSettings.minSupportedPlayers)
                startAutomaticVictoryTimer();

            return;
        }

        if (controller == null && !started) {
            // The admin did not start the match yet
            disconnectedClients.add(client.getPlayerName().toLowerCase());
            System.out.println("Disconnected client " + client.getPlayerName() + " from lobby #" + id);
        }
    }

    /**
     * This method is used to check if the
     * game termination has been handled in the controller.
     * In that case, the lobby can be deleted.
     * @return boolean, True if the game is terminated
     */
    public boolean isTerminated() {
        if (controller == null) return false;
        return controller.gameTerminated();
    }
}

class LobbyPingSender extends Thread {

    private final Lobby lobby;  // Needed for proper synchronization

    public LobbyPingSender(Lobby lobby) {
        this.lobby = lobby;
    }

    public void ping() {
        synchronized (lobby) {
            for (Client c : lobby.getClients())
                c.ping();
        }
    }

    /**
     * This Runnable is used to ping clients, if a client is not available
     * an exception is thrown and the exception handles kicks in.
     */
    @Override
    public void run() {
        while(true) {
            ping();
            try {
                Thread.sleep(NetworkSettings.serverPingIntervalSeconds * 1000);
            } catch (InterruptedException ignored) {
                return;
            }
        }
    };
}