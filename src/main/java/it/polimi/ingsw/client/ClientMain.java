package it.polimi.ingsw.client;


import it.polimi.ingsw.client.JoinLobby.*;
import it.polimi.ingsw.client.gui.HelloApplication;
import it.polimi.ingsw.shared.NetworkSettings;

public class ClientMain{
    public static void startClient(Client_Settings.UI uiOption){

        Server server = null;

        switch (Client_Settings.connection){
            case RMI:
                server = new ServerRMI(NetworkSettings.serverIp, NetworkSettings.RMIport);
                break;
            case TCP:
                server = new ServerTCP(NetworkSettings.serverIp, NetworkSettings.TCPport);
                break;
        }

        JoinLobbyUI joinLobbyUI = null;
        switch (uiOption){
            case CLI:
                joinLobbyUI = new JoinLobbyCLI(server);
                break;
            case GUI:
                //joinLobbyUI = new JoinLobbyGUI(server);
                HelloApplication.startApp();
                break;
        }

        joinLobbyUI.start();
        try {
            joinLobbyUI.join();
        } catch (InterruptedException e) {}
    }
}