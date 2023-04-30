package it.polimi.ingsw.server.clientonserver;

import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.model.Tile;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

/**
 * This object is used on the server to send updates to
 * a specific client.
 * This object is used on the server to create the messages
 * and send them over a tcp tunnel.
 */
public class ClientSocket implements Client {
    // TODO add board and shelves
    private String playerName = null;
    private Chat chat;
    private Socket clientSocket;

    private BufferedReader ClientIn;
    private PrintWriter ClientOut;

    public ClientSocket() {}

    /**
     * socket input buffer
     * @return the read line of the buffer
     */
    public String in(){
        boolean ready = false;
        try {
            while(!ready){ //TODO this is a bad way to wait, look for better options
                if(ClientIn.ready())
                    ready = true;
            }
            return ClientIn.readLine();
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
     * This method is used when a player picks a tile
     * from the board. It sends the message
     * to the remote view to remove the tile
     * from the board.
     *
     * @param position Position
     */
    @Override
    public void pickedFromBoard(JSONObject position) {
        //TODO
    }

    /**
     * This method is used to transfer the whole board
     * to the remote view,
     * it uses a json string.
     *
     * @param board JSONObject.toJsonString
     */
    @Override
    public void refreshBoard(String board) {
        //TODO
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
        //TODO
    }

    /**
     * This method is used to transfer the whole shelf
     * of a player to the remote view,
     * it uses a json string.
     *
     * @param player name of the player
     * @param shelf  JSONObject.toJsonString
     */
    @Override
    public void refreshShelf(String player, String shelf) {
        //TODO
    }

    /**
     * This method is used to send a chat message to clients.
     * @param sender Player's name
     * @param message String message
     */
    @Override
    public void postChatMessage(String sender, String message) {
        //TODO
    }

    /**
     * This method is used to send the whole chat to the client,
     * it is used when a refresh is needed.
     * @param chat Chat object
     */
    public void refreshChat(Chat chat) {
        //TODO
    }

    /**
     * This method is used when the lobby is ready and the
     * admin started the game.
     * @param players List of players, order is used to
     *                determine turns
     */
    @Override
    public void gameStarted(List<String> players) {
        //TODO
    }

    /**
     * This function is used when the turn of a player ends.
     *
     * @param player Name of the player that will play next.
     */
    @Override
    public void updateTurn(String player) {
        //TODO
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
        //TODO
    }

    /**
     * This method is used at the beginning of the game to let
     * the client know its personal goal
     *
     * @param id Int ID of the goal
     */
    @Override
    public void setPlayerGoal(int id) {
        //TODO
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
