package it.polimi.ingsw.client.controller.gui;

import it.polimi.ingsw.client.controller.gui.sceneControlles.*;

import java.net.URL;

public enum SceneEnum {
    login("Login", LoginController.class),
    lobbySelection("Lobbies", LobbiesController.class),
    lobbyWaiting("WaitingLobby", WaitingLobbyController.class),
    home("HomeScreen", HomeScreenController.class),
    chat("Chat", ChatController.class),
    playerShelves("PlayerShelves", PlayerShelvesController.class)
    ;

    private final String sceneFile;
    private final Class<?> controllerClass;

    SceneEnum(String sceneFile, Class<?> controllerClass) {
        this.sceneFile = sceneFile;
        this.controllerClass = controllerClass;
    }

    URL getResource() {
        return ClientControllerGUI
                .class
                .getClassLoader()
                .getResource(
                        String.format("gui/%s.fxml", sceneFile)
                );
    }

    SceneController getNewController(ClientControllerGUI clientControllerGUI) {
        try {
            return (SceneController) this.controllerClass
                    .getDeclaredConstructor(ClientControllerGUI.class)
                    .newInstance(clientControllerGUI);
        } catch (Exception e) {
            throw new RuntimeException("Invalid class in scene enum, " + e.getMessage());
        }
    }

    Class<?> getControllerClass() {
        return controllerClass;
    }
}
