package it.polimi.ingsw.client.controller.gui.sceneControlles;

import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.client.controller.gui.SceneEnum;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.shared.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.controller.gui.ClientControllerGUI.loadImage;

public class PlayerShelvesController extends SceneController implements Initializable {
    private final ClientModelGUI model;
    private final double iWidthShelf1 = 122.0;
    private final double iHeightShelf1 = 45.0;
    private final double iWidthShelf2 = 326.0;
    private final double iHeightShelf2 = 45.0;
    private final double iWidthShelf3 = 120.0;
    private final double iHeightShelf3 = 235.0;
    private final Map<String, shelfPosition> shelvesPositions = new HashMap<>();

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

    //dimensione = 19x19
    //TODO portare a fattor comune
    private void setShelf(Shelf shelf, double width, double height) throws BadPositionException {
        for(int i = 0; i < shelf.getRows(); i++) {
            for(int j = 0; j < shelf.getColumns(); j++) {
                ImageView imageView = new ImageView();
                if(!shelf.getTile(i, j).toString().equals("I") && //TODO use enum instance
                        !shelf.getTile(i, j).toString().equals("E")) {
                    imageView.setImage(loadImage("item_tiles/" + shelf.getTile(i, j).toString() + "1.png"));
                    imageView.setFitHeight(19.0);
                    imageView.setFitWidth(19.0);
                    imageView.setLayoutX(width + j*24.0);
                    imageView.setLayoutY(height + i*22.0);
                    anchor.getChildren().add(imageView);
                }

            }
        }
    }

    /**
     * Refreshes the shelf of the player with the given name
     * @param playerName the name of the player whose shelf
     *                   has to be refreshed
     */
    public void refreshShelf(String playerName, Shelf shelf) {
        if(!shelvesPositions.containsKey(playerName)) {
            System.err.println("Player " + playerName + " not found in refreshShelf");
            return;
        }

        final double iWidth = shelvesPositions.get(playerName).getiWidthShelf();
        final double iHeight = shelvesPositions.get(playerName).getiHeightShelf();
        try {
            setShelf(shelf, iWidth, iHeight);
        } catch (BadPositionException e) {
            throw new RuntimeException("Bad position in shelf of " + playerName + ": " + e.getMessage());
        }
    }

    public void putIntoShelf(String playerName, int column, Tile tile) {
        if(!shelvesPositions.containsKey(playerName)) {
            System.err.println("Player " + playerName + " not found in putIntoShelf");
            return;
        }
        final double iWidth = shelvesPositions.get(playerName).getiWidthShelf();
        final double iHeight = shelvesPositions.get(playerName).getiHeightShelf();

        // TODO: implement
        try {
            setShelf(model.getPlayersShelves().get(playerName), iWidth, iHeight);  // TEMPORARY
        } catch (BadPositionException e) {
            throw new RuntimeException("Bad position in shelf of " + playerName + ": " + e.getMessage());
        }
    }

    @FXML
    protected void getHome() {
        controller.loadScene(SceneEnum.home);
    }

    @FXML
    protected void readChat() {
        controller.loadScene(SceneEnum.chat);
    }

    //TODO refactor
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(model.getPlayersShelves().size() == 2) {
            shelf2.setStyle("-fx-opacity:0.0");
            shelf3.setStyle("-fx-opacity:0.0");
            for(String player : model.getPlayersShelves().keySet()) {
                if(!player.equals(model.getPlayerName())) {
                    username1.setText(player);
                    shelvesPositions.put(
                            player,
                            new shelfPosition(iWidthShelf1, iHeightShelf1));
                    refreshShelf(player, model.getPlayersShelves().get(player));
                }
            }
            username2.setOpacity(0.0);
            username3.setOpacity(0.0);

            return;
        }

        if(model.getPlayersShelves().size() == 3) {
            shelf3.setStyle("-fx-opacity:0.0");
            for(String player : model.getPlayersShelves().keySet()) {
                if(!player.equals(model.getPlayerName())) {
                    if(username1.getText().equals("Username 1")) {
                        username1.setText(player);
                        shelvesPositions.put(
                                player,
                                new shelfPosition(iWidthShelf1, iHeightShelf1));
                        refreshShelf(player, model.getPlayersShelves().get(player));
                    } else {
                        username2.setText(player);
                        shelvesPositions.put(
                                player,
                                new shelfPosition(iWidthShelf2, iHeightShelf2));
                        refreshShelf(player, model.getPlayersShelves().get(player));
                    }
                }
            }
            username3.setOpacity(0.0);


            return;

        }

        for(String player : model.getPlayersShelves().keySet()) {
            if(!player.equals(model.getPlayerName())) {
                if(username1.getText().equals("Username 1")) {
                    username1.setText(player);
                    shelvesPositions.put(
                            player,
                            new shelfPosition(iWidthShelf1, iHeightShelf1));
                    refreshShelf(player, model.getPlayersShelves().get(player));
                } else if(username2.getText().equals("Username 2")) {
                    username2.setText(player);
                    shelvesPositions.put(
                            player,
                            new shelfPosition(iWidthShelf2, iHeightShelf2));
                    refreshShelf(player, model.getPlayersShelves().get(player));
                } else {
                    username3.setText(player);
                    shelvesPositions.put(
                            player,
                            new shelfPosition(iWidthShelf3, iHeightShelf3));
                    refreshShelf(player, model.getPlayersShelves().get(player));
                }
            }
        }


    }
}

class shelfPosition {
    private final double iWidthShelf;
    private final double iHeightShelf;

    shelfPosition(double iWidthShelf, double iHeightShelf) {
        this.iWidthShelf = iWidthShelf;
        this.iHeightShelf = iHeightShelf;
    }

    public double getiWidthShelf() {
        return iWidthShelf;
    }

    public double getiHeightShelf() {
        return iHeightShelf;
    }
}