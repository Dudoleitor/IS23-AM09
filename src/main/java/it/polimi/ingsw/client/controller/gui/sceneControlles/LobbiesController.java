package it.polimi.ingsw.client.controller.gui.sceneControlles;

import it.polimi.ingsw.client.connection.Server;
import it.polimi.ingsw.client.connection.ServerException;
import it.polimi.ingsw.shared.InputSanitizer;
import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.client.controller.gui.SceneEnum;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.server.clientonserver.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class LobbiesController extends SceneController implements Initializable {
    private final ClientModelGUI model;
    private final Server server;
    private final Client client;
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

    public LobbiesController(ClientControllerGUI controller) {
        super(controller);
        this.model = controller.getModel();
        this.server = controller.getServer();
        this.client = controller.getClient();
    }

    private void nextScene() throws IOException {
        controller.loadScene(SceneEnum.lobbyWaiting);
    }

    /**
     * creates the lobby from scratch by calling the method from the server
     * @throws IOException
     */
    @FXML
    protected void createLobby() throws IOException {
        try {
            server.createLobby(client);
        } catch (ServerException e) {
            ClientControllerGUI.showError("Server error while creating lobby");
            return;
        }

        System.out.println("Created Lobby!");
        nextScene();
    }

    /**
     * user joins random lobby
     * @throws IOException
     */
    public void randomLobby() throws IOException {
        try {
            server.joinRandomLobby(client);
        } catch (ServerException e) {
            ClientControllerGUI.showError("Error while joining or creating lobby");
            return;
        }

        System.out.println("Joining random Lobby!");
        nextScene();
    }

    /**
     * user joins the selected lobby. In this method we do checks on the available lobby
     * and the input
     * @throws IOException
     */
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
        try {
            server.joinSelectedLobby(client, lobbyToJoin);
        } catch (ServerException e) {
            ClientControllerGUI.showError("Server error while joining lobby");
            return;
        }
        nextScene();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lobbies.setEditable(false);
        updateLobbies();
    }

    /**
     * button method to refresh the available lobbies
     */
    @FXML
    protected void updateLobbies() {
        try {
            availableLobbies = server.getAvailableLobbies();
        } catch (ServerException e) {
            ClientControllerGUI.showError("Server error while obtaining lobbies");
            return;
        }

        lobbies.clear();
        for(int lobbyId : availableLobbies.keySet()) {
            lobbies.appendText("Lobby number: " + lobbyId + ", players in: " + availableLobbies.get(lobbyId) + "\n");
        }
    }

    /**
     * equals to the joinLobby. The user can join the selected lobby by pushing enter
     * @param keyEvent
     * @throws IOException
     */
    @FXML
    protected void enterUpdate(KeyEvent keyEvent) throws IOException {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            joinLobby();
        }
    }
}
