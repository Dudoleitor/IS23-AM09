package it.polimi.ingsw.client.View.gui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    Label welcome;
    @FXML
    TextField userName;
    @FXML
    TextField lobbyNumber;


    @FXML
    protected void signInAction() {
        welcome.setText(userName.getText() + " ha effettuato il login nella lobby " +
                lobbyNumber.getText());
    }
}
