package it.polimi.ingsw.client.controller.gui;

import it.polimi.ingsw.client.connection.LobbyException;
import it.polimi.ingsw.client.connection.Server;
import it.polimi.ingsw.client.connection.ServerException;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.server.clientonserver.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class WaitingLobbyController extends FxmlController implements Initializable {
    private final Server server;
    private final String playerName;

    @FXML
    VBox vbox;

    @FXML
    TextArea lobbyChat;

    @FXML
    Button startButton;

    @FXML
    TextField message;

    public WaitingLobbyController(ClientControllerGUI controller) {
        super(controller);
        this.server = controller.getServer();
        this.playerName = controller.getClient().getPlayerName();
    }

    @FXML
    protected void startMatch() throws LobbyException {
        Stage stage = (Stage) vbox.getScene().getWindow();
        if(server.isLobbyAdmin(playerName)) {
            server.startGame(playerName, false);
        }
    }

    @FXML
    protected void sendMessage() throws LobbyException {
        controller.getServer().postToLiveChat(controller.getModel().getPlayerName(), message.getText());
        lobbyChat.appendText(controller.getModel().getPlayerName() + ": " + message.getText() + "\n");
        message.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            if(!server.isLobbyAdmin(playerName)) {
                startButton.setOpacity(0.0);
            }
            if(controller.getModel().getChat().getAllMessages().size() == 0)
                return;
            for(int i = 0; i < controller.getModel().getChat().getAllMessages().size(); i++) {
                lobbyChat.appendText( controller.getModel().getChat().getAllMessages().get(i).getSender() + ": " +controller.getModel().getChat().getAllMessages().get(i).getMessage() + "\n");
            }
        } catch (LobbyException e) {
            e.printStackTrace();
        }
    }
}
