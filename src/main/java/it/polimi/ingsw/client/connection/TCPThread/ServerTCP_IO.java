package it.polimi.ingsw.client.connection.TCPThread;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.shared.MessageTcp;
import it.polimi.ingsw.shared.NetworkSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ServerTCP_IO{
    private final Socket serverSocket;
    private final PrintWriter serverOut;
    private final BufferedReader serverIn;
    private final ArrayList<MessageTcp> responses = new ArrayList<>();
    private final ArrayList<MessageTcp> updates = new ArrayList<>();
    private ServerTCPListener serverListener;
    private ServerTCPViewUpdater serverViewUpdater;

    public ServerTCP_IO(Socket server, ClientModel clientModel) {
        try {
            serverSocket = server;
            serverOut = new PrintWriter(serverSocket.getOutputStream(), true);
            serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            initializeThreads(clientModel);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void initializeThreads(ClientModel clientModel){
        serverListener = new ServerTCPListener(serverIn, responses, updates);
        serverViewUpdater = new ServerTCPViewUpdater(clientModel, updates, serverOut);
        serverListener.start();
        serverViewUpdater.start();
    }

    /**
     * socket input buffer
     * @return the read line of the buffer
     */
    public MessageTcp in() throws RemoteException {
        MessageTcp message;
        try {
                long startTime = System.currentTimeMillis();
                long endTime;
                long elapsedTime = 0;
                while (responses.isEmpty()) {
                    synchronized (responses) {
                        responses.wait(NetworkSettings.WaitingTimeMillis - elapsedTime); //set to wait for remaining time
                    }
                    endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                    if (responses.isEmpty() && elapsedTime > NetworkSettings.WaitingTimeMillis)//check that wake up wasn't accidental
                        throw new RemoteException("Waited too much");

                }
        } catch (InterruptedException e) {
                throw new RemoteException(e.getMessage());
        }


        synchronized (responses) {
            message = responses.get(0);
            responses.remove(0);
        }

        return message;

    }
    public void terminate(){
        synchronized (serverListener){
            serverListener.terminate();
        }
        synchronized (serverViewUpdater){
            serverViewUpdater.terminate();
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * send a message through socket connection
     * @param message is the message to send
     */
    public void out(String message){
        synchronized (serverOut) {
            serverOut.println(message);
        }
    }
}
