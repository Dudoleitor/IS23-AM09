package it.polimi.ingsw.client.controller.gui;
import it.polimi.ingsw.client.model.ClientModelGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML
    TextField message;

    @FXML
    TextArea textArea;

    Text username = new Text();

    private final ClientModelGUI model = ClientControllerGUI.model;


    @FXML
    protected void SendMsg() {
        //System.out.println(client.getController().getChat().getAllMessages());
        model.postChatMessage(model.getPlayerName(), message.getText());
        username.setText(model.getPlayerName());
        username.setStyle("-fx-text-background-color: green");
        textArea.appendText(username.getText() + ": " + message.getText() + "\n");
        message.setText("");
    }

    @FXML
    protected void backHome() throws IOException {
        Stage stage = (Stage) message.getScene().getWindow();
        stage.setScene(new Scene(ClientControllerGUI.loadScene("PlayerHomeScreen"), 800, 800));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(model.getChat().getAllMessages().size() > 0) {
            for(int i = 0; i < model.getChat().getAllMessages().size(); i++) {
                textArea.appendText(model.getChat().getAllMessages().get(i).getSender() + ": " +
                        model.getChat().getAllMessages().get(i).getMessage() + "\n");

            }
        }
    }

}
