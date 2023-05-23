package it.polimi.ingsw.client.controller.gui.sceneControlles;

import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.client.model.ClientModelGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class winnerSceneController extends SceneController implements Initializable {


    private final ClientModelGUI model;
    private final String playerName;

    @FXML
    Text winnerText;

    @FXML
    Text username1;
    @FXML
    Text username2;
    @FXML
    Text username3;
    @FXML
    Text username4;

    public winnerSceneController(ClientControllerGUI controller) {
        super(controller);
        this.model = controller.getModel();
        this.playerName = controller.getClient().getPlayerName();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
