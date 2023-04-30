package it.polimi.ingsw.client.View.gui;
import it.polimi.ingsw.client.controller.ClientControllerGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


import java.io.IOException;

public class homeScreenController {

    private ClientGUI client = new ClientGUI();

    @FXML
    Button sh00;

    @FXML
    protected void hello() {
        sh00.setOpacity(1.0);
        sh00.setStyle("-fx-background-color: #9925be; ");
    }

    @FXML
    protected void readChat() throws IOException {
        client.changeScene("Chat");
    }

}
