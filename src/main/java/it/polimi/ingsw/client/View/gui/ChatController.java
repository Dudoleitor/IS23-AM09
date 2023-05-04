package it.polimi.ingsw.client.View.gui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.rmi.RemoteException;

public class ChatController {

    private final ClientGUI client = HelloController.getClient();

    @FXML
    TextField message;

    @FXML
    TextArea textArea;

    @FXML
    protected void SendMsg() {
        client.getController().postChatMessage(client.getController().getPlayerName(), message.getText());
        textArea.appendText(client.getController().getPlayerName() + ": " + message.getText() + "\n");
        message.setText("");
    }

    @FXML
    protected void backHome() throws IOException {
        Stage stage = (Stage) message.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("PlayerHomeScreen"), 800, 800));
    }
}
