package it.polimi.ingsw.client;


import it.polimi.ingsw.client.LobbySelection.*;
import it.polimi.ingsw.shared.NetworkSettings;

public class ClientMain{
    public static void main(String argv[]){

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
        switch (Client_Settings.ui){
            case CLI:
                lobbySelection = new LobbySelection(server,new LobbySelectionCLI());
                break;
            case GUI:
                //joinLobbyUI = new JoinLobbyGUI(server);
                break;
        }

        lobbySelection.start();
        try {
            lobbySelection.join();
        } catch (InterruptedException e) {}
    }
}