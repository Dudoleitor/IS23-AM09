package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.Client_Settings;
import it.polimi.ingsw.client.connection.*;
import it.polimi.ingsw.client.controller.cli.LobbyCommand;
import it.polimi.ingsw.client.controller.cli.LobbySelectionCommand;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;
import it.polimi.ingsw.server.clientonserver.ClientSocket;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.NetworkSettings;
import it.polimi.ingsw.shared.model.Board;
import it.polimi.ingsw.shared.model.Move;
import it.polimi.ingsw.shared.model.Shelf;

import java.rmi.RemoteException;
import java.util.Map;

import static java.lang.Thread.sleep;

public interface ClientController {
    void startClient();
    Server getServer();
    void setServer(Server server);
    Client getClient();
    void setClient(Client client);
    void errorMessage(String msg);

    /**
     * Initiate all the objects that will handle the connection to serer
     */
    static void initConnectionInterface(ClientController controller, ClientModel model) throws ServerException {
        switch (Client_Settings.connection){
            case RMI:
                controller.setServer(new ServerRMI(NetworkSettings.serverIp, NetworkSettings.RMIport));
                try {
                    controller.setClient(new ClientRMI(model));
                } catch (RemoteException e) {
                    throw new ServerException("Impossible to create RMI client object");
                }
                break;
            case TCP:
                controller.setServer(new ServerTCP(NetworkSettings.serverIp, NetworkSettings.TCPport, model));
                final ClientSocket client = new ClientSocket();
                try {
                    client.setName(model.getPlayerName());
                } catch (RemoteException ignored) {
                }
                controller.setClient(client);
                break;
            case STUB:
                controller.setServer(new ConnectionStub());
                try {
                    controller.setClient(new ClientRMI(model)); //TODO create stub when completed the real one
                } catch (RemoteException e) {
                    throw new ServerException("Impossible to create RMI client object");
                }
        }
    }

    /**
     * Try login tries times
     * @param tries available to connect
     * @param seconds to wait in case of failure
     */
    static void tryConnect(int tries, int seconds, ClientController controller, ClientModel model) throws ServerException {
        try {
            ClientController.initConnectionInterface(controller, model);
        } catch (ServerException e) {
            controller.errorMessage("Connection Error, retying in "+seconds+" seconds");
            try {
                sleep(seconds * 1000);
            } catch (InterruptedException i) {
                return;
            }
            if (tries > 1)
                ClientController.tryConnect(tries - 1, seconds, controller, model);
            else throw new ServerException("Can't connect to the server");
        }
    }

    /**
     * Try login tries times
     * @param tries available to login
     * @param seconds to wait in case of failure
     * @return true if successful
     */
    static boolean tryLogin(int tries, int seconds, ClientController controller){
        boolean logged = false;
        for(int attempt = 0; attempt < tries && !logged; attempt++){
            logged = controller.getServer().login(controller.getClient()); //get previous sessions if present
            if(!logged){
                controller.errorMessage("Connection Error, retying in "+seconds+" seconds");
                try {
                    sleep(seconds*1000);
                } catch (InterruptedException e) {
                    return false;
                }
            }
        }
        return logged;
    }
    /**
     * Initiate the connection interface and attempt a login
     * @return true if login was successful
     */
    static boolean connect(ClientController controller, ClientModel model) {
        try{
            //Initiate the server connection interfaces according to settings
            ClientController.tryConnect(10,1, controller, model);
            //login
            return ClientController.tryLogin(3,2, controller);
        } catch (ServerException e) {
            return false;
        }
    }
}
