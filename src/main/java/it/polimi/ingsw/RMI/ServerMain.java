package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Shelf;
import org.json.simple.JSONObject;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerMain implements RemoteCall{
    private static volatile boolean keepOn = true;
    private static int port = 1234;
    private static List<ChatMessage> messages;

    public static void main(String argv[]){
        ServerMain obj = new ServerMain();
        RemoteCall stub;
        try {
            stub = (RemoteCall) UnicastRemoteObject.exportObject(obj, port); //create an interface to export
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(port); //create a registry that accepts request on a defined port
        } catch (RemoteException e) {
            e.printStackTrace(); //TODO to handle correctly
        }
        try {
            registry.bind("interface", stub); //Binds a remote reference to the specified name in this registry
        } catch (RemoteException e) { //TODO to handle correctly
            throw new RuntimeException(e);
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        //init livechat
        messages = Collections.synchronizedList(new ArrayList<>());

        System.out.println("Server is on");
        while (keepOn) {
            Thread.onSpinWait(); //is used to suspend the process and make it wait
        } //to keep it online
        System.out.println("Server is shutting down :D, don't forget to save... oh no too late");
    }

    @Override
    public boolean login(String nick) throws RemoteException {
        System.out.println(nick + " has just logged in... but cannot talk");
        return true;
    }

    @Override
    public boolean shutDown() throws RemoteException {
        keepOn = false;
        return true;
    }

    @Override
    public boolean sendShelf(JSONObject s) throws JsonBadParsingException {
        System.out.println("Here's your shelf bro:\n" + new Shelf(s));
        return true;
    }

    @Override
    public boolean postToLiveChat(String playerName, String message) {
        if(playerName == null || message == null){
            return false;
        }
        messages.add(new ChatMessage(playerName,message));
        System.out.println("Posted:" + messages.get(messages.size()-1));
        return true;
    }

    @Override
    public List<ChatMessage> updateLiveChat(int alreadyReceived) throws RemoteException {
        List<ChatMessage> livechatUpdate = new ArrayList<>();
        for(int i = alreadyReceived; i < messages.size(); i++){
            livechatUpdate.add(messages.get(i));
        }
        System.out.println("updated client chat");
        return livechatUpdate;
    }
}
