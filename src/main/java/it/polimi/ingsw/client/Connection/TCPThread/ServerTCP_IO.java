package it.polimi.ingsw.client.Connection.TCPThread;

import it.polimi.ingsw.shared.MessageTcp;
import it.polimi.ingsw.shared.NetworkSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.RemoteException;
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
    public MessageTcp in() throws RemoteException {
        MessageTcp message;
        try {

            synchronized (serverListener) {
                serverSocket.setSoTimeout(NetworkSettings.WaitingTime); //TODO seems not working
                while (input.isEmpty()) {
                    serverListener.wait();
                }
            }
        } catch (InterruptedException | SocketException e) {
                //serverSocket.close(); U SURE?
                synchronized (serverListener){
                    serverListener.terminate();
                }
                throw new RemoteException();
        }


        synchronized (input) {
            message = input.get(0);
            input.remove(0);
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
