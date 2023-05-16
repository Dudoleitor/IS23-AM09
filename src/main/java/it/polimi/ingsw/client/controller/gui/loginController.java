package it.polimi.ingsw.client.controller.gui;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.model.ClientModelGUI;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class loginController {
    @FXML
    Label welcome;
    @FXML
    TextField userName;

    @FXML
    protected void signInAction() throws IOException {
        if(userName.getText().equals("")) {
            ClientControllerGUI.showError("Insert username");
        } else {
            welcome.setText(userName.getText() + " joined the game!");
            final ClientModelGUI model = new ClientModelGUI(userName.getText());
            ClientControllerGUI.controller.setModel(model);

            ClientController.connect(ClientControllerGUI.controller, model);

            Stage stage = (Stage) welcome.getScene().getWindow();
            stage.setScene(new Scene(ClientControllerGUI.loadScene("Lobbies"), 800, 800));
        }
    }

}
