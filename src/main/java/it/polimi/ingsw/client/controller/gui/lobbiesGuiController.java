package it.polimi.ingsw.client.controller.gui;

import it.polimi.ingsw.client.connection.Server;
import it.polimi.ingsw.client.connection.ServerException;
import it.polimi.ingsw.client.controller.InputSanitizer;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.server.clientonserver.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class lobbiesGuiController implements Initializable {
    private final ClientControllerGUI controller = ClientControllerGUI.controller;
    private final ClientModelGUI model = controller.getModel();
    private final Server server = controller.getServer();
    private final Client client = controller.getClient();
    Map<Integer, Integer> availableLobbies = null;

    @FXML
    Button createButton;
    @FXML
    Button randomButton;
    @FXML
    Button joinButton;
    @FXML
    TextArea lobbies;
    @FXML
    TextField lobbyNumber;

    private void nextScene() throws IOException {
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.setScene(new Scene(ClientControllerGUI.loadScene("WaitingLobby"), 800, 800));
    }

    @FXML
    protected void createLobby() throws IOException {
        try {
            server.createLobby(client);
        } catch (ServerException e) {
            ClientControllerGUI.showError("Server error while creating lobby");
            return;  // TODO Handle exception
        }

        System.out.println("Created Lobby!");
        nextScene();
    }

    public void randomLobby(ActionEvent actionEvent) {
        System.out.println("Joining random Lobby!");
    }

    public void joinLobby() throws IOException {
        if(availableLobbies==null) {
            throw new RuntimeException("Lobbies not initialized!");
        }

        final String userInput = lobbyNumber.getText();

        if(userInput.equals("")) {
            ClientControllerGUI.showError("Insert lobby number!");
            return;
        }

        if(!InputSanitizer.isInteger(userInput)) {
            ClientControllerGUI.showError("Insert a valid lobby number!");
            return;
        }

        final int lobbyToJoin = Integer.parseInt(userInput);

        System.out.println("Joined lobby " + lobbyToJoin + "!");
        if(!availableLobbies.containsKey(lobbyToJoin)) {
            ClientControllerGUI.showError("Insert an existing lobby number!");
            return;
        }
        nextScene();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            availableLobbies = server.getAvailableLobbies();
        } catch (ServerException e) {
            ClientControllerGUI.showError("Server error while obtaining lobbies");
            return;  // TODO Handle exception
        }

        for(int lobbyId : availableLobbies.keySet()) {
            lobbies.appendText("Lobby number: " + lobbyId + ", players in: " + availableLobbies.get(lobbyId) + "\n");
        }
    }
}
