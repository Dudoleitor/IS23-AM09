package it.polimi.ingsw.client.controller.gui.sceneControlles;
import it.polimi.ingsw.client.connection.LobbyException;
import it.polimi.ingsw.client.connection.ServerException;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.InputSanitizer;
import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.client.controller.gui.SceneEnum;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.server.clientonserver.Client;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class LoginController extends SceneController {
    @FXML
    Label welcome;
    @FXML
    TextField userName;
    @FXML
    VBox vbox;


    public LoginController(ClientControllerGUI controller) {
        super(controller);
    }

    @FXML
    protected void signInAction() throws IOException, ServerException {
        if (userName.getText().equals("")) {
            ClientControllerGUI.showError("Insert username");
            return;
        }
        if(!InputSanitizer.isValidName(userName.getText())){
            ClientControllerGUI.showError("Illegal characters in username");
            return;
        }
        final String username = userName.getText();
        final ClientModelGUI model = new ClientModelGUI(userName.getText(), controller);

        if (!ClientController.connect(controller, model)) { // Login failed
            return;
        }

        final Client client = controller.getClient();

        final int previousLobbyId = controller.getServer().disconnectedFromLobby(client.getPlayerName());
        System.out.println(previousLobbyId);
        if(previousLobbyId >= 0) {
            try {
                controller.getServer().joinSelectedLobby(client, previousLobbyId);  // Automatically joining the lobby
                controller.setModel(model);
                if(controller.gameIsStarted()) {
                    controller.loadScene(SceneEnum.home);
                } else {
                    controller.loadScene(SceneEnum.lobbyWaiting);
                }
                return;
            } catch (ServerException ignored) {
                // If auto join fails, the user will be asked to join a lobby
            }
        }

        welcome.setText(userName.getText() + " joined the game!");
        controller.setModel(model);
        controller.loadScene(SceneEnum.lobbySelection);
    }

    @FXML
    protected void enterLogin(KeyEvent keyEvent) throws IOException, ServerException {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            signInAction();
        }
    }
}