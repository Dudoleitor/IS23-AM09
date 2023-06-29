package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.Client_Settings;
import it.polimi.ingsw.client.connection.*;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.clientonserver.Client;
import it.polimi.ingsw.server.clientonserver.ClientRMI;
import it.polimi.ingsw.server.clientonserver.ClientSocket;
import it.polimi.ingsw.shared.NetworkSettings;

import java.rmi.RemoteException;

import static java.lang.Thread.sleep;

public interface ClientController {
    void startClient();
    Server getServer();
    void setServer(Server server);
    Client getClient();
    void setClient(Client client);
    void errorMessage(String msg);
    ClientModel getModel();
    boolean gameIsStarted();

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
                    controller.setClient(new ClientRMI(model));
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
     * Try login
     * @return true if successful
     */
    static boolean tryLogin(ClientController controller) throws ServerException {
        final boolean logged;
        logged = controller.getServer().login(controller.getClient()); //get previous sessions if present

        if(!logged){
            controller.errorMessage("Login error, username already taken");
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
            ClientController.tryConnect(5,1, controller, model);
            //login
            return ClientController.tryLogin(controller);
        } catch (ServerException e) {
            controller.errorMessage(e.getMessage());
            return false;
        }
    }

    static void start(ClientController controller,boolean erasePreviousMatches){
        boolean admin = false;
        boolean started = false;

        if (controller.gameIsStarted()) {
            controller.errorMessage("Game already started");
            return;
        }

        final Server server = controller.getServer();
        String playerName = "";
        try {
            playerName = controller.getModel().getPlayerName();
        } catch (RemoteException ignored) {
        }
        try {
            admin = server.isLobbyAdmin(playerName);
            if(!admin){
                controller.errorMessage("You are not lobby admin");
                return;
            }
            started = server.startGame(playerName, erasePreviousMatches);
        } catch (LobbyException e) {
            started = false;
        }
        if(!started){
            controller.errorMessage("You can not start lobby now");
        }
    }

    static Thread getThread(Object pingLock) {
        return new PingRunnable(pingLock);
    }
}

class PingRunnable extends Thread {
    private final Object pingLock;
    private final int waitTime = ((int) NetworkSettings.serverPingIntervalSeconds) * 2000;
    protected PingRunnable(Object pingLock){
        this.pingLock = pingLock;
    }

    @Override
    public void run() {
        long waitStart;
        synchronized (pingLock) {
            while (true) {
                waitStart = System.currentTimeMillis();
                try {
                    pingLock.wait(waitTime);
                } catch (InterruptedException e) {
                    return;
                }
                if (System.currentTimeMillis() >=
                        waitStart + waitTime) {
                    System.err.println("Server not responding, closing");
                    System.exit(1);
                }
            }
        }
    }
}
