package it.polimi.ingsw.client.controller.gui.sceneControlles;

import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;

public abstract class SceneController {
    ClientControllerGUI controller;

    public SceneController(ClientControllerGUI controller){
        this.controller = controller;
    }
}
