package it.polimi.ingsw.RMI;
import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Shelf;
import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteCall extends Remote {
    boolean login(String nick) throws RemoteException;
    boolean shutDown() throws RemoteException;

    boolean sendShelf(JSONObject s) throws RemoteException, JsonBadParsingException;
}
