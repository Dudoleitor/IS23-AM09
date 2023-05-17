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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class WaitingLobbyController implements Initializable {
    private final ClientControllerGUI controller = ClientControllerGUI.controller;
    private final ClientModelGUI model = controller.getModel();
    private final Server server = controller.getServer();
    private final Client client = controller.getClient();

    @FXML
    VBox vbox;

    @FXML
    TextArea lobbyPlayers;

    @FXML
    Button startButton;

    @FXML
    protected void startMatch() throws IOException, LobbyException {
        Stage stage = (Stage) vbox.getScene().getWindow();
        if(server.isLobbyAdmin(client.getPlayerName())) {
            stage.setScene(new Scene(ClientControllerGUI.loadScene("FirstPlayerHomeScreen"), 800, 800));
            return;
        }
        stage.setScene(new Scene(ClientControllerGUI.loadScene("PlayerHomeScreen"), 800, 800));
    }

    @Override
    //TODO: fix isLobbyAdmin() here
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            if(!server.isLobbyAdmin(client.getPlayerName())) {
                startButton.setOpacity(0.0);
                startButton.fire();
            }
        } catch (LobbyException e) {
            e.printStackTrace();
        }
    }
}
