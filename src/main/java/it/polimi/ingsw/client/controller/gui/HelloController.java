package it.polimi.ingsw.client.controller.gui;
import it.polimi.ingsw.client.model.ClientModelGUI;
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

    private static final ClientGUI client = new ClientGUI();
    private ClientModelGUI controller;
    private ClientControllerGUI clientControllerGui;

    public static ClientGUI getClient() {
        return client;
    }

    @FXML
    protected void signInAction() throws IOException {
        if(userName.getText().equals("")) {
            client.showError("Insert username");
        } else {
            welcome.setText(userName.getText() + " joined the game!");
            clientControllerGui = new ClientControllerGUI();
            client.setGui(clientControllerGui);
            controller = new ClientModelGUI(userName.getText(), clientControllerGui);
            client.setController(controller);
            Stage stage = (Stage) welcome.getScene().getWindow();
            stage.setScene(new Scene(client.loadScene("Lobbies"), 800, 800));
        }
    }

}
