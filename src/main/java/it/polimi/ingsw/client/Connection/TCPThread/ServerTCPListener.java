package it.polimi.ingsw.client.Connection.TCPThread;

import it.polimi.ingsw.shared.MessageTcp;

import java.io.BufferedReader;;
import java.io.IOException;
import java.util.ArrayList;

public class ServerTCPListener extends Thread{

    private final BufferedReader serverIn;
    private final ArrayList<MessageTcp> input;
    private boolean exit;

    public ServerTCPListener(BufferedReader serverIn, ArrayList input) {
        this.serverIn = serverIn;
        this.input = input;
    }
    @Override
    public void run(){
        exit = false;
        while (!exit){
            MessageTcp incomingMessage = in();
            if(incomingMessage.isReplyMessage()) {
                synchronized (input){
                    input.add(incomingMessage);
                }
                synchronized (this) {
                    this.notifyAll();
                }
            } else if (incomingMessage.isUpdateMessage()){
                //TODO Waiting for ClientSocket
                System.out.println("to implement yet");
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
        exit = true;
    }
}
