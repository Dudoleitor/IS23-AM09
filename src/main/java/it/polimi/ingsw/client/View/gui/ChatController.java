package it.polimi.ingsw.client.View.gui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(client.getController().getChat().getAllMessages().size() > 0) {
            for(int i = 0; i < client.getController().getChat().getAllMessages().size(); i++) {
                textArea.appendText(client.getController().getChat().getAllMessages().get(i).getSender() + ": " +
                        client.getController().getChat().getAllMessages().get(i).getMessage() + "\n");

            }
        }
    }

}
