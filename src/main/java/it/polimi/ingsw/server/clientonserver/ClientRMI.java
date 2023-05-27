package it.polimi.ingsw.server.clientonserver;

import it.polimi.ingsw.server.NetworkExceptionHandler;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.RemoteInterfaces.ClientRemote;
import it.polimi.ingsw.shared.model.Tile;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This object is used to send updates to a specific client.
 * A copy of this object will reside on the server.
 * It is a wrapper for ClientRemoteObject: it's
 * needed to handle RemoteExceptions.
 * Remote network invocations are done inside a dedicated
 * thread to avoid blocking the caller.
 */
public class ClientRMI implements Client, Serializable {
    private final String playerName;
    private final ClientRemote clientRemote;
    private NetworkExceptionHandler exceptionHandler;

    /**
     * The queue and the consumer are used to implement a
     * multithreading consumer-producer pattern in order to
     * make remote network invocations in a separate thread,
     * without blocking the caller.
     * These two must be used only calling the
     * submitRunnable method.
     */
    private transient BlockingQueue<Runnable> runnablesQueue;
    private transient Thread queueConsumer;

    public ClientRMI(ClientRemote clientRemote) throws RemoteException {
        this.playerName = clientRemote.getPlayerName();
        this.clientRemote = clientRemote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientRMI clientRMI = (ClientRMI) o;
        return Objects.equals(playerName.toLowerCase(), clientRMI.playerName.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName.toLowerCase());
    }

    /**
     * This method is used to execute a network update
     * by submitting a runnable to the runnable consumer.
     * Always use this method to add a runnable to the queue.
     * @param toRun Runnable to run
     */
    private void submitRunnable(Runnable toRun) {
        if (this.runnablesQueue==null)
            this.runnablesQueue = new LinkedBlockingQueue<>();
        if(this.queueConsumer==null) {
            this.queueConsumer = new Thread() {
                final BlockingQueue<Runnable> queue = runnablesQueue;

                @Override
                public void run() {
                    while (true) {
                        final Runnable toRun;
                        try {
                            toRun = queue.take();
                        } catch (InterruptedException ignored) {
                            return;
                        }
                        toRun.run();
                    }
                }
            };
            this.queueConsumer.start();
        }
        runnablesQueue.add(toRun);
    }

    /**
     * This method is used to return the name of
     * the players using this client.
     * @return String, player's name.
     */
    public String getPlayerName() {return playerName;}

    /**
     * This function is used to set the ExceptionHandler the client
     * will notify when a network exception happens.
     * @param e Exception handler
     */
    @Override
    public void setExceptionHandler(NetworkExceptionHandler e) {
        this.exceptionHandler = e;
    }

    /**
     * This method is used when a player picks a tile
     * from the board. It sends the message
     * to the remote view to remove the tile
     * from the board.
     *
     * @param position Position
     */
    @Override
    public void pickedFromBoard(JSONObject position) {
        submitRunnable(() -> {
            try {
                clientRemote.pickedFromBoard(position);
            } catch (RemoteException e) {
                exceptionHandler.handleNetworkException(this, e);
            }
        });
    }

    /**
     * This method is used to transfer the whole board
     * to the remote view,
     * it uses a json string.
     * @param board JSONObject
     */
    @Override
    public void refreshBoard(JSONObject board) {
        submitRunnable(()->{
            try {
                clientRemote.refreshBoard(board);
            } catch (RemoteException e) {
                exceptionHandler.handleNetworkException(this, e);
            }
        });
    }

    /**
     * This method is used when a player inserts a single
     * tile into his shelf.
     * It is used to send the message to the remote view
     * of the client in order to insert the tile
     * into the shelf.
     * @param player String name of the player that moved the tile
     * @param column destination column of the shelf
     * @param tile Tile to insert
     */
    @Override
    public void putIntoShelf(String player, int column, Tile tile) {
        submitRunnable(()->{
            try {
                clientRemote.putIntoShelf(player, column, tile);
            } catch (RemoteException e) {
                exceptionHandler.handleNetworkException(this, e);
            }
        });
    }

    /**
     * This method is used to transfer the whole shelf
     * of a player to the remote view,
     * it uses a json string.
     * @param player name of the player
     * @param shelf  JSONObject
     */
    @Override
    public void refreshShelf(String player, JSONObject shelf) {
        submitRunnable(()->{
            try {
                clientRemote.refreshShelf(player, shelf);
            } catch (RemoteException e) {
                exceptionHandler.handleNetworkException(this, e);
            }
        });
    }

    /**
     * This method is used to send a chat message to clients.
     * @param sender Player's name
     * @param message String message
     */
    @Override
    public void postChatMessage(String sender, String message) {
        submitRunnable(() -> {
            try {
                clientRemote.postChatMessage(sender, message);
            } catch (RemoteException e) {
                exceptionHandler.handleNetworkException(this, e);
            }
        });
    }

    /**
     * This method is used to send the whole chat to the client,
     * it is used when a refresh is needed.
     * @param chat Chat object
     */
    @Override
    public void refreshChat(Chat chat) {
        submitRunnable(()->{
            try {
                clientRemote.refreshChat(chat);
            } catch (RemoteException e) {
                exceptionHandler.handleNetworkException(this, e);
            }
        });
    }

    /**
     * This method is used when the lobby is ready and the
     * admin started the game.
     * @param newMatch true if the game is new,
     *        false if it was loaded from a save or the player
     *        reconnected.
     */
    @Override
    public void gameStarted(boolean newMatch) {
        submitRunnable(()->{
            try {
                clientRemote.gameStarted(newMatch);
            } catch (RemoteException e) {
                exceptionHandler.handleNetworkException(this, e);
            }
        });
    }

    /**
     * This function is used when the turn of a player ends.
     *
     * @param player Name of the player that will play next.
     */
    @Override
    public void updateTurn(String player) {
        submitRunnable(()->{
            try {
                clientRemote.nextTurn(player);
            } catch (RemoteException e) {
                exceptionHandler.handleNetworkException(this, e);
            }
        });
    }

    /**
     * This method is used when a player achieves
     * a common goal and pops points from
     * its stack.
     * It is also used to init the common goal.
     *
     * @param id     ID of the common goal
     * @param points Copy of the stack with points that
     *               can still be achieved
     */
    @Override
    public void refreshCommonGoal(int id, List<Integer> points) {
        submitRunnable(()->{
            try {
                clientRemote.refreshCommonGoal(id, points);
            } catch (RemoteException e) {
                exceptionHandler.handleNetworkException(this, e);
            }
        });
    }

    /**
     * This method is used at the beginning of the game to let
     * the client know its personal goal
     *
     * @param id Int ID of the goal
     */
    @Override
    public void setPlayerGoal(int id) {
        submitRunnable(()->{
            try {
                clientRemote.setPlayerGoal(id);
            } catch (RemoteException e) {
                exceptionHandler.handleNetworkException(this, e);
            }
        });
    }

    /**
     * This method is used at the end of the game to
     * send the leaderboard to the client.
     * @param leaderBoard Map: player's name - points
     */
    public void endGame(Map<String, Integer> leaderBoard){
        submitRunnable(()->{
            try {
                clientRemote.endGame(leaderBoard);
            } catch (RemoteException e) {
                exceptionHandler.handleNetworkException(this, e);
            }
        });
    }

    /**
     * This function is used to close the connection with the client
     */
    @Override
    public void disconnect() {
        if(this.queueConsumer!=null)
            this.queueConsumer.interrupt();
        return; // Nothing to do, RMI does everything
    }

    /**
     * This function is used to ensure the client is still connected.
     * Expected return value is "pong".
     * Note: this method does NOT run in a separate thread and
     * blocks the caller waiting for the remote invocation.
     */
    @Override
    public String ping() {
        try {
            return clientRemote.ping();
        } catch (RemoteException e) {
            exceptionHandler.handleNetworkException(this, e);
        }
        return "notPong";
    }
}
