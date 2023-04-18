package it.polimi.ingsw.client.gui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    TextField userName;
    @FXML
    TextField lobbyNumber;


    @FXML
    protected void signInAction() {
        welcomeText.setText(userName.getText() + " ha effettuato il login nella lobby " +
                lobbyNumber.getText());
    }
}
