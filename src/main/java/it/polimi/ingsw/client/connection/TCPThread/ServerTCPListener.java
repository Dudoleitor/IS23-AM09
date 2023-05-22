package it.polimi.ingsw.client.connection.TCPThread;

import it.polimi.ingsw.shared.MessageTcp;

import java.io.BufferedReader;;
import java.io.IOException;
import java.util.ArrayList;

public class ServerTCPListener extends Thread{

    private final BufferedReader serverIn;
    private final ArrayList<MessageTcp> responses;
    private final ArrayList<MessageTcp> updates;

    private boolean exit = false;

    public ServerTCPListener(BufferedReader serverIn, ArrayList responses, ArrayList updates) {
        this.serverIn = serverIn;
        this.responses = responses;
        this.updates = updates;
    }
    @Override
    public void run() {
        while (!exit) {
            MessageTcp incomingMessage = in();
            System.out.println("Received: " + incomingMessage);
            if (incomingMessage.isReplyMessage()) {
                synchronized (responses) {
                    responses.add(incomingMessage);
                    responses.notifyAll();
                }
            } else if (incomingMessage.isUpdateMessage()) {
                synchronized (updates) {
                    updates.add(incomingMessage);
                    updates.notifyAll();
                }
            }
        }
    }

    public MessageTcp in() {
        boolean ready = false;
        try {
            while (!ready) {
                if (serverIn.ready())
                    ready = true;
            }
            return new MessageTcp(serverIn.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void terminate(){
        this.exit = true;
    }
}
