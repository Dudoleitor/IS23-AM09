package it.polimi.ingsw.client.Connection.TCPThread;

import it.polimi.ingsw.client.View.View;
import it.polimi.ingsw.client.controller.ClientController;
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

    public ServerTCP_IO(Socket server, ClientController clientController) {
        try {
            serverSocket = server;
            serverOut = new PrintWriter(serverSocket.getOutputStream(), true);
            serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            initializeThreads(clientController);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void initializeThreads(ClientController clientController){
        serverListener = new ServerTCPListener(serverIn, responses, updates);
        serverViewUpdater = new ServerTCPViewUpdater(clientController, updates);
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
                        responses.wait(NetworkSettings.WaitingTime - elapsedTime); //set to wait for remaining time
                    }
                    endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                    if (responses.isEmpty() && elapsedTime > NetworkSettings.WaitingTime)//check that wake up wasn't accidental
                        throw new RemoteException("Waited too much");

                }
        } catch (InterruptedException | RemoteException e) {
                throw new RemoteException(e.getMessage());
        }


        synchronized (responses) {
            message = responses.get(0);
            responses.remove(0);
        }

        if (message.getCommand() == MessageTcp.MessageCommand.Quit) {
            synchronized (serverViewUpdater) {
                serverViewUpdater.exit();
                serverViewUpdater.notifyAll();
            }
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
