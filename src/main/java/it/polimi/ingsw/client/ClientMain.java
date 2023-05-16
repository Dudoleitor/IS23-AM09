package it.polimi.ingsw.client;


import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ClientControllerDriver;
import it.polimi.ingsw.client.controller.cli.ClientControllerCLI;
import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;

public class ClientMain {
    public static void startClient() {
        final ClientController controller;
        switch (Client_Settings.ui) {
            case CLI:
                controller = new ClientControllerCLI();
                break;
            case GUI:
                controller = new ClientControllerGUI();
                break;
            default:
                controller = new ClientControllerDriver();
                break;
        }
        controller.startClient();
    }
}
