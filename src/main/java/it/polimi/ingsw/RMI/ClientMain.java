package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.Shelf;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain {
    static int port = 1234;
    public static void main(String argv[]) throws RemoteException, NotBoundException {
        RemoteCall stub;
        Registry registry;
        registry = LocateRegistry.getRegistry("127.0.0.1", port); //get remote registry that points to 127.0.0.1:port
        stub = (RemoteCall) registry.lookup("interface"); //get interface from remote registry
        doSomething(stub);
    }
    public static void doSomething(RemoteCall stub) throws RemoteException {
        boolean logged = stub.login("Not_a_consulent");
        if(logged)
            System.out.println("Logged successfully, but don't forget to do not talk");
        try {
            Shelf s = new Shelf(Jsonable.pathToJsonObject("src/test/resources/ShelfTests/ShelfGenericTest.json", Shelf.class));
            boolean feedback = stub.sendShelf(s.toJson()); //item passed must be a serializable object
            if (feedback)
                System.out.println("Shelf sent successfully");
            boolean end =stub.shutDown();
            if(end){
                System.out.println("Server is now down");
            }


        } catch (JsonBadParsingException e) { //TODO to handle it better
            throw new RuntimeException(e);
        }
    }
}
