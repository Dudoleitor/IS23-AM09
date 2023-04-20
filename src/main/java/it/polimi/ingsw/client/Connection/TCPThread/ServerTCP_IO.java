package it.polimi.ingsw.client.Connection.TCPThread;

import it.polimi.ingsw.shared.MessageTcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerTCP_IO{
    private final Socket serverSocket;
    private final PrintWriter serverOut;
    private final BufferedReader serverIn;
    private final ArrayList<MessageTcp> input;
    private final ServerTCPListener serverListener;

    public ServerTCP_IO(Socket server) {
        try {
            serverSocket = server;
            serverOut = new PrintWriter(serverSocket.getOutputStream(), true);
            serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            input = new ArrayList<>();
            serverListener = new ServerTCPListener(serverIn,input);
            serverListener.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * socket input buffer
     * @return the read line of the buffer
     */
    public MessageTcp in(){
        MessageTcp message;
        while (input.isEmpty()){
            try {
                serverListener.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        synchronized (input) {
            message = input.get(0);
        }

        return message;

    }

    /**
     * send a message through socket connection
     * @param message is the message to send
     */
    public void out(String message){
        serverOut.println(message);
    }
}
