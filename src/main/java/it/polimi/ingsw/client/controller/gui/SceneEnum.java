package it.polimi.ingsw.client.controller.gui;

import java.net.URL;

public enum SceneEnum {
    login("Start", loginController.class),
    lobbySelection("Lobbies", lobbiesGuiController.class),
    lobbyWaiting("WaitingLobby", WaitingLobbyController.class),
    home("PlayerHomeScreen", homeScreenController.class),
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

    FxmlController getNewController(ClientControllerGUI clientControllerGUI) {
        try {
            return (FxmlController) this.controllerClass
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
