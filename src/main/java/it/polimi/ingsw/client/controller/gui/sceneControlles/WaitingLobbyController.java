package it.polimi.ingsw.client.controller.gui.sceneControlles;

import it.polimi.ingsw.client.connection.LobbyException;
import it.polimi.ingsw.client.connection.Server;
import it.polimi.ingsw.client.controller.InputSanitizer;
import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.ChatMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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

    /**
     * This method is used to post a message in the chat,
     * it is called by the server when a new message is received
     * @param sender the sender of the message
     * @param message the message
     */
    public void postChatMessage(String sender, String message){
        lobbyChat.appendText(sender + ": " + message + "\n");
    }

    /**
     * This method is used to refresh the chat, it is called by the server
     * @param chat Chat object containing all the messages
     */
    public void refreshChat(Chat chat) {
        lobbyChat.clear();
        for (ChatMessage cm : chat.getAllMessages())
            postChatMessage(cm.getSender(), cm.getMessage());
    }

    /**
     * the admin can click the start button and start the match
     * @throws LobbyException
     */
    @FXML
    protected void startMatch() throws LobbyException {
        Stage stage = (Stage) vbox.getScene().getWindow();
        if(server.isLobbyAdmin(playerName)) {
            server.startGame(playerName, checkBox.isSelected());
        }
    }

    /**
     * button method that can be used to send messages and post them to the chat
     * @throws LobbyException
     */
    @FXML
    protected void sendMessage() throws LobbyException {
        if(message.getText().equals("")){
            controller.errorMessage("Insert a message");
            return;
        }
        if(!InputSanitizer.isValidMessage(message.getText())){
            controller.errorMessage("Illegal characters in message");
            return;
        }
        controller.getServer().postToLiveChat(playerName, message.getText());
        message.setText("");
    }

    /**
     * equals to the sendMessage() but instead of a button the user can push enter
     * @param keyEvent
     * @throws LobbyException
     */
    @FXML
    protected void enterSendMessage(KeyEvent keyEvent) throws LobbyException {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            sendMessage();
        }
    }

    /**
     * if the client is not the admin, he can't see startButton and checkBox
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lobbyChat.setEditable(false);
        try {
            if(!server.isLobbyAdmin(playerName)) {
                startButton.setOpacity(0.0);
                checkBox.setOpacity(0.0);
            }
            if(controller.getModel().getChat().getAllMessages().size() == 0)
                return;
            refreshChat(controller.getModel().getChat());
        } catch (LobbyException e) {
            e.printStackTrace();
        }
    }
}
