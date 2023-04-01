package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.Color;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.Shelf;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class ClientMain {
    static int port = 1234;
    static List<ChatMessage> chat = null;
    public static void main(String argv[]) throws RemoteException, NotBoundException {
        RemoteCall stub;
        Registry registry;
        registry = LocateRegistry.getRegistry("127.0.0.1", port); //get remote registry that points to 127.0.0.1:port
        stub = (RemoteCall) registry.lookup("interface"); //get interface from remote registry
        Thread match = new Thread(() -> {
            try {
                doSomething(stub);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        Thread livechat = new Thread( ()-> {
            try {
                sendStuff(stub);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        try{
            match.run();
            livechat.run();
        }
        catch (RuntimeException e){
            System.out.println("Something went terribly wrong");
        }
    }
    public static void doSomething(RemoteCall stub) throws RemoteException {
        boolean logged = stub.login("Not_a_consulent");
        if(logged)
            System.out.println("Logged successfully");
        try {
            Shelf s = new Shelf(Jsonable.pathToJsonObject("src/test/resources/ShelfTests/ShelfGenericTest.json", Shelf.class));
            boolean feedback = stub.sendShelf(s.toJson()); //item passed must be a serializable object
            if (feedback)
                System.out.println("Shelf sent successfully");

            /*boolean end =stub.shutDown();
            if(end){
                System.out.println("Server is now down");
            }*/ //TODO fix bug
        } catch (JsonBadParsingException e) { //TODO to handle it better
            throw new RuntimeException(e);
        }
    }

    public static void sendStuff(RemoteCall stub) throws RemoteException {
        System.out.println(Color.coloredString("Fell free to express yourself",Color.Yellow));
        printAllMessages();
        stub.postToLiveChat("Not_a_consulent","Se non fa assaggiare?");
        updateLiveChat(stub);
        printAllMessages();
        stub.postToLiveChat("Not_a_consulent","BLOKKATA");
        stub.postToLiveChat("Not_a_consulent","KIAROWSKY BILLONE");
        stub.updateLiveChat(1);
        updateLiveChat(stub);
        printAllMessages();
    }

    public static void updateLiveChat(RemoteCall stub) throws RemoteException {
        if(chat == null){
            chat = new ArrayList<>();
        }
        chat.addAll(stub.updateLiveChat(chat.size()));
    }

    public static void printAllMessages(){
        System.out.println(Color.coloredString("Printing Messages:",Color.Yellow));
        if(chat == null || chat.size() == 0){
            System.out.println("No messages Yet");
            return;
        }
        for(ChatMessage mes : chat){
            System.out.println(mes);
        }
    }
}
