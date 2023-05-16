package it.polimi.ingsw.client.controller.gui;

import it.polimi.ingsw.client.model.ClientModelGUI;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class lobbiesGuiController implements Initializable {
    private final ClientModelGUI model = ClientControllerGUI.model;
    List<String> avalaibleLobbies = new ArrayList<>();

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


    @FXML
    protected void createLobby() {
        System.out.println("Created Lobby!");
    }

    public void randomLobby(ActionEvent actionEvent) {
        System.out.println("Joining random Lobby!");
    }

    public void joinLobby() throws IOException {
        if(lobbyNumber.getText().equals("")) {
            ClientControllerGUI.showError("Insert lobby number!");
        } else {
            System.out.println("Joined lobby " + lobbyNumber.getText() + "!");
            if(!avalaibleLobbies.contains(lobbyNumber.getText())) {
                ClientControllerGUI.showError("Insert correct lobby number");
            } else {
                Stage stage = (Stage) joinButton.getScene().getWindow();
                stage.setScene(new Scene(ClientControllerGUI.loadScene("PlayerHomeScreen"), 800, 800));
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int i = 0; i < 10; i++) {
            avalaibleLobbies.add("Lobby " + i);
        }

        avalaibleLobbies.stream().forEach(lobby -> lobbies.appendText("Lobby number: " + lobby + "\n"));
    }
}
