package it.polimi.ingsw.client.controller.gui.sceneControlles;
import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.client.controller.gui.SceneEnum;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.shared.Chat;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

    public ChatController(ClientControllerGUI controller) {
        super(controller);
        this.model = controller.getModel();
    }

    public void postChatMessage(String sender, String message){
        // TODO
    }
    public void refreshChat(Chat chat) {
        // TODO
    }

    @FXML
    protected void SendMsg() {
        //System.out.println(client.getController().getChat().getAllMessages());
        if(message.getText().equals("")){
            controller.errorMessage("Insert a message");
            return;
        }
        model.postChatMessage(model.getPlayerName(), message.getText());
        username.setText(model.getPlayerName());
        username.setStyle("-fx-text-background-color: green");
        textArea.appendText(username.getText() + ": " + message.getText() + "\n");
        message.setText("");
    }

    @FXML
    protected void backHome() throws IOException {
        controller.loadScene(SceneEnum.home);
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
