package it.polimi.ingsw.client.View.gui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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

    Text username = new Text();


    @FXML
    protected void SendMsg() {
        //System.out.println(client.getController().getChat().getAllMessages());
        client.getController().postChatMessage(client.getController().getPlayerName(), message.getText());
        username.setText(client.getController().getPlayerName());
        username.setStyle("-fx-text-background-color: green");
        textArea.appendText(username.getText() + ": " + message.getText() + "\n");
        message.setText("");
    }

    @FXML
    protected void backHome() throws IOException {
        Stage stage = (Stage) message.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("PlayerHomeScreen"), 800, 800));
    }
}
