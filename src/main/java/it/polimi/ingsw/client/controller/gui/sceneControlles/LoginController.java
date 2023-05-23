package it.polimi.ingsw.client.controller.gui.sceneControlles;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.InputSanitizer;
import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.client.controller.gui.SceneEnum;
import it.polimi.ingsw.client.model.ClientModelGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class LoginController extends SceneController {
    @FXML
    Label welcome;
    @FXML
    TextField userName;
    @FXML
    VBox vbox;

    public LoginController(ClientControllerGUI controller) {
        super(controller);
    }

    @FXML
    protected void signInAction() throws IOException {
        if (userName.getText().equals("")) {
            ClientControllerGUI.showError("Insert username");
            return;
        }
        if(!InputSanitizer.isValidName(userName.getText())){
            ClientControllerGUI.showError("Illegal characters in username");
            return;
        }
        final ClientModelGUI model = new ClientModelGUI(userName.getText(), controller);

        if (!ClientController.connect(controller, model)) { // Login failed
            ClientControllerGUI.showError("Failed connecting to server");
            return;
        }

        welcome.setText(userName.getText() + " joined the game!");
        controller.setModel(model);
        controller.loadScene(SceneEnum.lobbySelection);
    }
}