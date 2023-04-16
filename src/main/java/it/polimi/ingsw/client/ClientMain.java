package it.polimi.ingsw.client;


import it.polimi.ingsw.client.JoinLobby.*;
import it.polimi.ingsw.shared.Constants;

public class ClientMain{
    public static void main(String argv[]){
        Connection connection = Connection.RMI;
        UI ui = UI.CLI;

        ServerStub server = null;

        switch (connection){
            case RMI:
                server = new ServerStubRMI(Constants.serverIp,Constants.RMIport);
                break;
            case TCP:
                server = new ServerStubTCP(Constants.serverIp,Constants.RMIport);
                break;
        }

        JoinLobbyUI joinLobbyUI = null;
        switch (ui){
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

    private enum Connection{
        TCP("tcp"),
        RMI("rmi");
        String tag;
        Connection(String tag){
            this.tag = tag;
        }
        String getTag(){
            return tag;
        }
    }

    private enum UI{
        CLI("cli"),
        GUI("gui");
        String tag;
        UI(String tag){
            this.tag = tag;
        }
        String getTag(){
            return tag;
        }
    }
}