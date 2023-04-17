package it.polimi.ingsw.client;


import it.polimi.ingsw.client.LobbySelection.*;
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

        LobbySelection lobbySelection = null;
        switch (uiOption){
            case CLI:
                lobbySelection = new LobbySelection(server, new LobbySelectionCLI());
                break;
            case GUI:
                //lobbySelection = new JoinLobbyGUI(server);
                HelloApplication.startApp();
                break;
        }

        lobbySelection.start();
        try {
            lobbySelection.join();
        } catch (InterruptedException e) {}
    }
}