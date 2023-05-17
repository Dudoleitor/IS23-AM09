package it.polimi.ingsw.client.controller.gui;

import it.polimi.ingsw.client.model.ClientModelGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerShelvesController implements Initializable {
    private final ClientModelGUI model = ClientControllerGUI.controller.getModel();
    @FXML
    Text username1;
    @FXML
    Text username2;
    @FXML
    Text username3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
