package it.polimi.ingsw.server.clientonserver;

import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.ChatMessage;
import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
    private final Socket clientSocket;

    private final BufferedReader ClientIn;
    private final PrintWriter ClientOut;

    public ClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
        if(clientSocket != null) {
            try {
                ClientOut = new PrintWriter(clientSocket.getOutputStream(), true);
                ClientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            ClientOut = null;
            ClientIn = null;
        }
    }

    /**
     * socket input buffer
     * @return the read line of the buffer
     */
    public String in(){
        boolean ready = false;
        try {
            while(!ready){
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

    public void setName(String name){
        if(playerName != null){
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
    public void pickedFromBoard(Position position) {
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
     * @param message ChatMessage object
     */
    public void postChatMessage(ChatMessage message) {

    }

    /**
     * This method is used to send the whole chat to the client,
     * it is used when a refresh is needed.
     * @param chat Chat object
     */
    public void refreshChat(Chat chat) {

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
