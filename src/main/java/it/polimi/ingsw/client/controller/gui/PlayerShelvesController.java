package it.polimi.ingsw.client.controller.gui;

import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.shared.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.controller.gui.ClientControllerGUI.loadImage;

public class PlayerShelvesController extends FxmlController implements Initializable {
    private final ClientModelGUI model;
    private final double iWidthShelf1 = 122.0;
    private final double iHeightShelf1 = 45.0;
    private final double iWidthShelf2 = 326.0;
    private final double iHeightShelf2 = 45.0;
    private final double iWidthShelf3 = 120.0;
    private final double iHeightShelf3 = 235.0;

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

    public PlayerShelvesController(ClientControllerGUI controller) {
        super(controller);
        this.model = controller.getModel();
    }

    //dimensione = 19x19

    public void setShelf1(String player) throws BadPositionException {
        /*for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 6; j++) {
                ImageView imageView = new ImageView();
                imageView.setImage(loadImage("item_tiles/B1.png"));
                imageView.setFitHeight(19.0);
                imageView.setFitWidth(19.0);
                imageView.setLayoutX(iWidthShelf1 + i*24.0);
                imageView.setLayoutY(iHeightShelf1 + j*22.0);
                anchor.getChildren().add(imageView);
            }
        }*/
        for(int i = 0; i < model.getPlayersShelves().get(player).getRows(); i++) {
            for(int j = 0; j < model.getPlayersShelves().get(player).getColumns(); j++) {
                ImageView imageView = new ImageView();
                if(!model.getPlayersShelves().get(player).getTile(i, j).toString().equals("I") &&
                        !model.getPlayersShelves().get(player).getTile(i, j).toString().equals("E")) {
                    imageView.setImage(loadImage("item_tiles/" + model.getPlayersShelves().get(player).getTile(i, j).toString() + "1.png"));
                    imageView.setFitHeight(19.0);
                    imageView.setFitWidth(19.0);
                    imageView.setLayoutX(iWidthShelf1 + j*24.0);
                    imageView.setLayoutY(iHeightShelf1 + i*22.0);
                    anchor.getChildren().add(imageView);
                }

            }
        }
        canvasShelf1.toFront();
    }

    public void setShelf2(String player) throws BadPositionException {

        for(int i = 0; i < model.getPlayersShelves().get(player).getRows(); i++) {
            for(int j = 0; j < model.getPlayersShelves().get(player).getColumns(); j++) {
                ImageView imageView = new ImageView();
                if(!model.getPlayersShelves().get(player).getTile(i, j).toString().equals("I") &&
                        !model.getPlayersShelves().get(player).getTile(i, j).toString().equals("E")) {
                    imageView.setImage(loadImage("item_tiles/" + model.getPlayersShelves().get(player).getTile(i, j).toString() + "1.png"));
                    imageView.setFitHeight(19.0);
                    imageView.setFitWidth(19.0);
                    imageView.setLayoutX(iWidthShelf2 + j*24.0);
                    imageView.setLayoutY(iHeightShelf2 + i*22.0);
                    anchor.getChildren().add(imageView);
                }

            }
        }
        canvasShelf2.toFront();

    }

    public void setShelf3(String player) throws BadPositionException {

        for(int i = 0; i < model.getPlayersShelves().get(player).getRows(); i++) {
            for(int j = 0; j < model.getPlayersShelves().get(player).getColumns(); j++) {
                ImageView imageView = new ImageView();
                if(!model.getPlayersShelves().get(player).getTile(i, j).toString().equals("I") &&
                        !model.getPlayersShelves().get(player).getTile(i, j).toString().equals("E")) {
                    imageView.setImage(loadImage("item_tiles/" + model.getPlayersShelves().get(player).getTile(i, j).toString() + "1.png"));
                    imageView.setFitHeight(19.0);
                    imageView.setFitWidth(19.0);
                    imageView.setLayoutX(iWidthShelf3 + j*25.0);
                    imageView.setLayoutY(iHeightShelf3 + i*22.0);
                    anchor.getChildren().add(imageView);
                }

            }
        }
        canvasShelf3.toFront();
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

        if(model.getPlayersShelves().size() == 2) {
            shelf2.setStyle("-fx-opacity:0.0");
            shelf3.setStyle("-fx-opacity:0.0");
            for(String player : model.getPlayersShelves().keySet()) {
                if(!player.equals(model.getPlayerName())) {
                    username1.setText(player);
                    try {
                        setShelf1(player);
                    } catch (BadPositionException e) {
                        e.printStackTrace();
                    }
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
                        try {
                            setShelf1(player);
                        } catch (BadPositionException e) {
                            e.printStackTrace();
                        }
                    } else {
                        username2.setText(player);
                        try {
                            setShelf2(player);
                        } catch (BadPositionException e) {
                            e.printStackTrace();
                        }
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
                    try {
                        setShelf1(player);
                    } catch (BadPositionException e) {
                        e.printStackTrace();
                    }
                } else if(username2.getText().equals("Username 2")) {
                    username2.setText(player);
                    try {
                        setShelf2(player);
                    } catch (BadPositionException e) {
                        e.printStackTrace();
                    }
                } else {
                    username3.setText(player);
                    try {
                        setShelf3(player);
                    } catch (BadPositionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }
}
