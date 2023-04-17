package it.polimi.ingsw.client;


import it.polimi.ingsw.client.JoinLobby.*;
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

        JoinLobbyUI joinLobbyUI = null;
        switch (Client_Settings.ui){
            case CLI:
                joinLobbyUI = new JoinLobbyCLI(server);
                break;
            case GUI:
                //joinLobbyUI = new JoinLobbyGUI(server);
                break;
        }

        joinLobbyUI.start();
        try {
            joinLobbyUI.join();
        } catch (InterruptedException e) {}
    }
}