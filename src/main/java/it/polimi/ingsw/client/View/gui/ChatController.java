package it.polimi.ingsw.client.View.gui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ChatController {

    private ClientGUI client = new ClientGUI();

    @FXML
    TextField message;

    @FXML
    TextArea textArea;

    @FXML
    protected void SendMsg() {
        textArea.appendText(message.getText() + "\n");
    }

    @FXML
    protected void backHome() throws IOException {
        client.changeScene("PlayerHomeScreen");
    }
}
