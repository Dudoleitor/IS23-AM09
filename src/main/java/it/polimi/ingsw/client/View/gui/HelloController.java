package it.polimi.ingsw.client.View.gui;
import it.polimi.ingsw.client.controller.ClientControllerGUI;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        welcome.setText(userName.getText() + " joined the lobby " +
                lobbyNumber.getText());
        controller = new ClientControllerGUI(userName.getText());
        client.setController(controller);
        Stage stage = (Stage) welcome.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("PlayerHomeScreen"), 800, 800));
        //client.changeScene("PlayerHomeScreen");
    }
}
