package it.polimi.ingsw.client.controller.gui;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.model.ClientModelGUI;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class loginController extends FxmlController {
    @FXML
    Label welcome;
    @FXML
    TextField userName;
    @FXML
    VBox vbox;

    public loginController(ClientControllerGUI controller) {
        super(controller);
    }

    @FXML
    protected void signInAction() throws IOException {
        if (userName.getText().equals("")) {
            ClientControllerGUI.showError("Insert username");
            return;
        }
        final ClientModelGUI model = new ClientModelGUI(userName.getText(), controller);

        if (!ClientController.connect(controller, model))  // Login failed
            return;

        welcome.setText(userName.getText() + " joined the game!");
        controller.setModel(model);
        controller.loadScene(SceneEnum.lobbySelection);
    }

}
