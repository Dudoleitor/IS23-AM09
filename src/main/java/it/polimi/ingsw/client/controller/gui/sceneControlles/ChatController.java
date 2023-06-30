package it.polimi.ingsw.client.controller.gui.sceneControlles;

import it.polimi.ingsw.client.connection.LobbyException;
import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.client.controller.gui.SceneEnum;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.ChatMessage;
import it.polimi.ingsw.shared.InputSanitizer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController extends SceneController implements Initializable {
    @FXML
    TextField message;

    @FXML
    TextArea textArea;

    @FXML
    SplitMenuButton receiverMenu;

    Text username = new Text();

    private final ClientModelGUI model;
    private final String playerName;

    private String receiver = "Everybody";

    public ChatController(ClientControllerGUI controller) {
        super(controller);
        this.model = controller.getModel();
        this.playerName = controller.getClient().getPlayerName();
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
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
            controller.errorMessage("Invalid message format");
            return;
        }

        if(receiver.equals("Everybody")){
            controller.getServer().postToLiveChat(playerName, message.getText());
        }
        else{
            controller.getServer().postSecretToLiveChat(playerName,receiver,message.getText());
        }
        username.setText(model.getPlayerName());
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

    protected void setMenu(){
        List<String> players = model.getPlayers();
        players.remove(model.getPlayerName());
        List<MenuItem> menuItems = receiverMenu.getItems();

        for(int i = 1; i < menuItems.size(); i++){
            if(i-1 < players.size()){
                menuItems.get(i).setText(players.get(i-1));
            }
            else{
                menuItems.get(i).setVisible(false);
            }
        }
    }

    public String getReceiver(int i){
        return receiverMenu.getItems().get(i).getText();
    }

    public void setMenuText(String text){
        receiverMenu.setText(text);
    }

    public void setEveryOne(){
        receiver = "Everybody";
        setMenuText(receiver);
    }

    public void setP1(){
        receiver = getReceiver(1);
        setMenuText(receiver);
    }

    public void setP2(){
        receiver = getReceiver(2);
        setMenuText(receiver);
    }

    public void setP3(){
        receiver = getReceiver(3);
        setMenuText(receiver);
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
        setMenu();
    }

}
