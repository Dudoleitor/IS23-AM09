package it.polimi.ingsw.client.controller.gui.sceneControlles;

import it.polimi.ingsw.client.connection.LobbyException;
import it.polimi.ingsw.client.connection.Server;
import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.shared.Chat;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class WaitingLobbyController extends SceneController implements Initializable {
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

    @FXML
    CheckBox checkBox;

    public WaitingLobbyController(ClientControllerGUI controller) {
        super(controller);
        this.server = controller.getServer();
        this.playerName = controller.getClient().getPlayerName();
    }

    public void postChatMessage(String sender, String message){
        // TODO
    }
    public void refreshChat(Chat chat) {
        // TODO
    }
    @FXML
    protected void startMatch() throws LobbyException {
        Stage stage = (Stage) vbox.getScene().getWindow();
        if(server.isLobbyAdmin(playerName)) {
            server.startGame(playerName, false);
        }

        //TODO: here I need to set a flag which will be send to the server in order to load an existing match
        if(checkBox.isSelected()) {
            System.out.println("Selected");
        } else {
            System.out.println("Not selected");
        }
    }

    @FXML
    protected void sendMessage() throws LobbyException {
        if(message.getText().equals("")){
            controller.errorMessage("Insert a message");
            return;
        }
        controller.getServer().postToLiveChat(controller.getModel().getPlayerName(), message.getText());
        lobbyChat.appendText(controller.getModel().getPlayerName() + ": " + message.getText() + "\n");
        message.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            if(!server.isLobbyAdmin(playerName)) {
                startButton.setOpacity(0.0);
                checkBox.setOpacity(0.0);
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
