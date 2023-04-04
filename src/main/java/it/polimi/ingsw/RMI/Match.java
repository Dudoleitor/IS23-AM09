package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.JsonBadParsingException;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.Shelf;

import java.rmi.RemoteException;

public class Match extends ClientThread{
    Match(String playerName, LobbyRemoteInterface stub) {
        super(playerName, stub);
    }
    @Override
    public void run() {
        try {
            //sleep(2000);
            Shelf s = new Shelf(Jsonable.pathToJsonObject("src/test/resources/ShelfTests/ShelfGenericTest.json", Shelf.class));
            printer.printErrorMessage("Shelf created\n" + s);
            stub.sendShelf(s.toJson()); //item passed must be a serializable object
            printer.printErrorMessage("Shelf sent successfully");
        } catch (JsonBadParsingException e) { //TODO to handle it better
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
