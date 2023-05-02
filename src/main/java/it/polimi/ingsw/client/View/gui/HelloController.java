package it.polimi.ingsw.client.View.gui;
import it.polimi.ingsw.client.controller.ClientControllerGUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class HelloController {
    @FXML
    Label welcome;
    @FXML
    TextField userName;
    @FXML
    TextField lobbyNumber;

    private static ClientGUI client = new ClientGUI();
    private ClientControllerGUI controller;

    public static ClientGUI getClient() {
        return client;
    }

    @FXML
    protected void signInAction() throws IOException {
        welcome.setText(userName.getText() + " ha effettuato il login nella lobby " +
                lobbyNumber.getText());
        controller = new ClientControllerGUI(userName.getText());
        client.setController(controller);
        client.changeScene("PlayerHomeScreen");
    }
}
