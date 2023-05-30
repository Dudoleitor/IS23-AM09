package it.polimi.ingsw.client.controller.gui.sceneControlles;
import it.polimi.ingsw.client.connection.LobbyException;
import it.polimi.ingsw.client.controller.InputSanitizer;
import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.client.controller.gui.SceneEnum;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.ChatMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController extends SceneController implements Initializable {
    @FXML
    TextField message;

    @FXML
    TextArea textArea;

    Text username = new Text();

    private final ClientModelGUI model;
    private final String playerName;

    public ChatController(ClientControllerGUI controller) {
        super(controller);
        this.model = controller.getModel();
        this.playerName = controller.getClient().getPlayerName();
    }

    /**
     * This method is used to post a message in the chat,
     * it is called by the server when a new message is received
     * @param sender the sender of the message
     * @param message the message
     */
    public void postChatMessage(String sender, String message){
        textArea.appendText(sender + ": " + message + "\n");
    }

    //TODO: EDO
    public void showTurnPopup() {
        ClientControllerGUI.showInfo("It's your turn.");
    }

    /**
     * This method is used to refresh the chat, it is called by the server
     * @param chat Chat object containing all the messages
     */
    public void refreshChat(Chat chat) {
        textArea.clear();
        for (ChatMessage cm : chat.getAllMessages())
            postChatMessage(cm.getSender(), cm.getMessage());
    }

    /**
     * this method is used to send messages and post them to the server.
     * @throws LobbyException
     */
    @FXML
    protected void SendMsg() throws LobbyException {
        if(message.getText().equals("")){
            controller.errorMessage("Insert a message");
            return;
        }
        if(!InputSanitizer.isValidMessage(message.getText())){
            controller.errorMessage("Illegal characters in message");
            return;
        }
        controller.getServer().postToLiveChat(playerName, message.getText());
        username.setText(model.getPlayerName());
        username.setStyle("-fx-text-background-color: green");
        message.setText("");
    }

    /**
     * button method that redirects to the home page
     * @throws IOException
     */
    @FXML
    protected void backHome() throws IOException {
        controller.loadScene(SceneEnum.home);
    }

    /**
     * equal to sendMsg() but user can use it by pushing enter
     * @param keyEvent
     * @throws LobbyException
     */
    @FXML
    protected void enterSendMessage(KeyEvent keyEvent) throws LobbyException {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            SendMsg();
        }
    }

    /**
     * this method is useful to refresh the chat when the scene is opened
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshChat(model.getChat());
        textArea.setEditable(false);
    }

}
