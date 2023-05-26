package it.polimi.ingsw.client.controller.gui.sceneControlles;

import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.client.controller.gui.GridHandler;
import it.polimi.ingsw.client.controller.gui.SceneEnum;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.shared.GameSettings;
import it.polimi.ingsw.shared.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.client.controller.gui.ClientControllerGUI.loadImage;

public class PlayerShelvesController extends SceneController implements Initializable {
    private final ClientModelGUI model;
    private Map<String,GridHandler> shelvesHandlers = new HashMap<>();
    private Map<String,ImageView> shelfImages = new HashMap<>();

    @FXML
    Text username1;
    @FXML
    Text username2;
    @FXML
    Text username3;

    @FXML
    Canvas canvasShelf1;

    @FXML
    Canvas canvasShelf2;

    @FXML
    Canvas canvasShelf3;

    @FXML
    AnchorPane anchor;

    @FXML
    ImageView shelf1;

    @FXML
    ImageView shelf2;

    @FXML
    ImageView shelf3;

    @FXML
    Button readChatButton;

    public PlayerShelvesController(ClientControllerGUI controller) {
        super(controller);
        this.model = controller.getModel();
    }

    /**
     * @param newMessage, if true there is a new message and the chat button gets blue
     */
    public void setNewMessage(boolean newMessage) {
        if(newMessage) {
            readChatButton.setStyle("-fx-background-color: blue;");
        } else {
            readChatButton.setStyle("-fx-background-color: #49be25;");
        }
    }

    private void setShelf(Shelf shelf, String playerName) {
        shelvesHandlers.get(playerName).resetGrid(shelf);
        shelvesHandlers.get(playerName).displayGrid();
    }

    /**
     * Refreshes the shelf of the player with the given name
     * @param playerName the name of the player whose shelf
     *                   has to be refreshed
     */
    public void refreshShelf(String playerName, Shelf shelf) {
        shelvesHandlers.get(playerName).resetGrid(shelf);
        shelvesHandlers.get(playerName).displayGrid();
    }

    public void putIntoShelf(String playerName, Position position, Tile tile) {
        shelvesHandlers.get(playerName).putTileBehind(shelfImages.get(playerName),position,tile);
    }
    private void displayAll(){
        shelvesHandlers.values().forEach(GridHandler::displayGrid);
        Arrays.stream(new ImageView[]{shelf1,shelf2,shelf3}).forEach(Node::toFront);
    }

    @FXML
    protected void getHome() {
        controller.loadScene(SceneEnum.home);
    }

    @FXML
    protected void readChat() {
        controller.loadScene(SceneEnum.chat);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Text[] userNames = new Text[]{username1,username2,username3};
        ImageView[] shelves = new ImageView[]{shelf1,shelf2,shelf3};
        Canvas[] shelvesCanvas = new Canvas[]{canvasShelf1,canvasShelf2,canvasShelf3};

        Arrays.stream(userNames).forEach(text -> text.setText(""));
        int i = 0;
        //for all the players but the user
        for(String playerName : model.getPlayers()){
            if(!playerName.equals(model.getPlayerName())){
                userNames[i].setText(playerName); //set a username

                //map the shelf png to the username
                shelfImages.put(playerName,
                        shelves[i]);

                //map the grid handler to the username
                shelvesHandlers.put(playerName,
                        new GridHandler(
                                anchor,
                                shelvesCanvas[i],
                                model.getPlayersShelves().get(playerName)));
                i++;
            }
        }
        //hide all the shelves and text fields with no username
        for(;i < shelves.length && i<userNames.length;i++){
            userNames[i].setOpacity(0.0);
            shelves[i].setOpacity(0.0);
        }
        displayAll();
    }
}