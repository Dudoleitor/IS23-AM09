package it.polimi.ingsw.server.clientonserver;

import it.polimi.ingsw.server.NetworkExceptionHandler;
import it.polimi.ingsw.server.ServerTcpThread;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.MessageTcp;
import it.polimi.ingsw.shared.model.Tile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This object is used on the server to send updates to
 * a specific client.
 * This object is used on the server to create the messages
 * and send them over a tcp tunnel.
 */
public class ClientSocket implements Client {
    private String playerName = null;
    private NetworkExceptionHandler networkExceptionHandler;
    private Chat chat;
    private Socket clientSocket;

    private BufferedReader ClientIn;
    private PrintWriter ClientOut;
    private ServerTcpThread serverThreadListener;

    public ClientSocket() {}

    /**
     * socket input buffer
     * @return the read line of the buffer
     */
    public MessageTcp in(){
        boolean ready = false;
        try {
            while(!ready){
                if(ClientIn.ready())
                    ready = true;
            }
            return new MessageTcp(ClientIn.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * send a message through socket connection
     * @param message is the message to send
     */
    public void out(String message){
        ClientOut.println(message);
    }

    public void setClientSocket(Socket clientSocket){
        this.clientSocket = clientSocket;
        if(clientSocket != null) {
            try {
                ClientOut = new PrintWriter(clientSocket.getOutputStream(), true);
                ClientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void setName(String name){
        if(playerName == null){
            playerName = name;
        }
    }
    public void setThreadReference(ServerTcpThread thread){
        this.serverThreadListener = thread;
    }

    public Socket getClientSocket() {
        return clientSocket;
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
     *
     * @param e Exception handler
     */
    @Override
    public void setExceptionHandler(NetworkExceptionHandler e) {
        this.networkExceptionHandler = e;
    }

    /**
     * This function is used by the clientAlive runnable
     * to notify the exception handler when the client is
     * not reachable anymore.
     * @return reference to Exception handler
     */
    public NetworkExceptionHandler getNetworkExceptionHandler() {
        return networkExceptionHandler;
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
        JSONObject content = new JSONObject();
        content.put("position", position);
        MessageTcp update = new MessageTcp();
        update.setCommand(MessageTcp.MessageCommand.PickedFromBoard);
        update.setContent(content);
        out(update.toString());
    }

    /**
     * This method is used to transfer the whole board
     * to the remote view,
     * it uses a json string.
     *
     * @param board JSONObject
     */
    @Override
    public void refreshBoard(JSONObject board) {
        JSONObject content = new JSONObject();
        content.put("board", board);
        MessageTcp update = new MessageTcp();
        update.setCommand(MessageTcp.MessageCommand.RefreshBoard);
        update.setContent(content);
        out(update.toString());
    }

    /**
     * This method is used when a player inserts a single
     * tile into his shelf.
     * It is used to send the message to the remote view
     * of the client in order to insert the tile
     * into the shelf.
     *
     * @param player String name of the player that moved the tile
     * @param column destination column of the shelf
     * @param tile Tile to insert
     */
    @Override
    public void putIntoShelf(String player, int column, Tile tile) {
        JSONObject content= new JSONObject();
        content.put("player", player);
        content.put("column", column);
        content.put("tile", tile.toString());

        MessageTcp update = new MessageTcp();
        update.setCommand(MessageTcp.MessageCommand.PutIntoShelf);
        update.setContent(content);
        out(update.toString());
    }

    /**
     * This method is used to transfer the whole shelf
     * of a player to the remote view,
     * it uses a json string.
     *
     * @param player name of the player
     * @param shelf  JSONObject
     */
    @Override
    public void refreshShelf(String player, JSONObject shelf) {
        JSONObject content= new JSONObject();
        content.put("player", player);
        content.put("shelf", shelf);

        MessageTcp update = new MessageTcp();
        update.setCommand(MessageTcp.MessageCommand.RefreshShelf);
        update.setContent(content);
        out(update.toString());
    }

    /**
     * This method is used to send a chat message to clients.
     * @param sender Player's name
     * @param message String message
     */
    @Override
    public void postChatMessage(String sender, String message) {
        JSONObject content= new JSONObject();
        content.put("sender", sender);
        content.put("message", message);

        MessageTcp update = new MessageTcp();
        update.setCommand(MessageTcp.MessageCommand.ChatMessageUpdate);
        update.setContent(content);
        out(update.toString());
    }

    /**
     * This method is used to send the whole chat to the client,
     * it is used when a refresh is needed.
     * @param chat Chat object
     */
    public void refreshChat(Chat chat) {
        JSONObject content= new JSONObject();
        content.put("chat", chat.toJson());

        MessageTcp update = new MessageTcp();
        update.setCommand(MessageTcp.MessageCommand.RefreshChat);
        update.setContent(content);
        out(update.toString());
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
        JSONObject content= new JSONObject();
        content.put("newMatch", newMatch);

        MessageTcp update = new MessageTcp();
        update.setCommand(MessageTcp.MessageCommand.NotifyStart);
        update.setContent(content);
        out(update.toString());

    }

    /**
     * This function is used when the turn of a player ends.
     *
     * @param player Name of the player that will play next.
     */
    @Override
    public void updateTurn(String player) {
        JSONObject content= new JSONObject();
        content.put("player", player);

        MessageTcp update = new MessageTcp();
        update.setCommand(MessageTcp.MessageCommand.UpdateTurn);
        update.setContent(content);
        out(update.toString());
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
        JSONObject content= new JSONObject();
        content.put("id", id);
        content.put("points", Jsonable.list2json(points));

        MessageTcp update = new MessageTcp();
        update.setCommand(MessageTcp.MessageCommand.RefreshCommonGoals);
        update.setContent(content);
        out(update.toString());
    }

    /**
     * This method is used at the beginning of the game to let
     * the client know its personal goal
     *
     * @param id Int ID of the goal
     */
    @Override
    public void setPlayerGoal(int id) {
        JSONObject content= new JSONObject();
        content.put("id", id);

        MessageTcp update = new MessageTcp();
        update.setCommand(MessageTcp.MessageCommand.SetPlayerGoal);
        update.setContent(content);
        out(update.toString());
    }

    /**
     * This method is used at the end of the game to
     * send the leaderboard to the client.
     * @param leaderBoard Map: player's name - points
     */
    public void endGame(Map<String, Integer> leaderBoard){
        JSONObject content= new JSONObject();
        content.put("leaderboard", Jsonable.map2json(leaderBoard));
        MessageTcp update = new MessageTcp();
        update.setCommand(MessageTcp.MessageCommand.EndGame);
        update.setContent(content);
        out(update.toString());

        if(serverThreadListener != null)
            serverThreadListener.terminate();
    }

    /**
     * This function is used to close the connection with the client
     */
    @Override
    public void disconnect() {
        MessageTcp update = new MessageTcp();
        update.setCommand(MessageTcp.MessageCommand.Disconnect);
        out(update.toString());
        if (serverThreadListener!=null)
            serverThreadListener.terminate();
    }

    /**
     * This function is used to ensure the client is still connected.
     * Expected return value is "pong".
     */
    @Override
    public String ping() {
        MessageTcp update = new MessageTcp();
        update.setCommand(MessageTcp.MessageCommand.Ping);
        out(update.toString());
        return "";
    }

    @Override
    public boolean equals(Object o) {  // Checking using LOWERCASE name
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientSocket clientSocket = (ClientSocket) o;
        return Objects.equals(playerName.toLowerCase(), clientSocket.playerName.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName.toLowerCase());
    }

}
