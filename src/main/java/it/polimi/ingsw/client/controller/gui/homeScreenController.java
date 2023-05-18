package it.polimi.ingsw.client.controller.gui;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.shared.model.InvalidMoveException;
import it.polimi.ingsw.shared.model.Position;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class homeScreenController extends FxmlController implements Initializable {
    boolean clicked = false;
    private final double iHeight = 85.0;
    private final double iWidth = 49.0;
    private final double iHeightShelf = 130.0;
    private final double iWidthShelf = 390.0;

    private List<Position> move = new ArrayList<>();

    private final ClientModelGUI model;

    public homeScreenController(ClientControllerGUI controller) {
        super(controller);
        this.model = controller.getModel();
    }


    @FXML
    VBox vbox;

    @FXML
    AnchorPane anchor;

    @FXML
    ImageView board;

    @FXML
    ImageView imgPersGoal;

    @FXML
    Canvas canvasBoard;

    @FXML
    Canvas canvasShelf;


    @FXML
   ImageView commonGoal1;

    @FXML
    ImageView commonGoal2;

    @FXML
    ImageView imageScoring1;

    @FXML
    ImageView imageScoring2;

    @FXML
    javafx.scene.shape.Polygon turnFlag;


    protected void getPersonalGoal() {
        int number = model.getPlayerGoal().getGoalId() + 1;
        imgPersGoal.setImage(new Image("gui/gameGraphics/personal_goal_cards/Personal_Goals" + number + ".png"));
    }

    private void getCommonGoals () {

        int number1 = model.getCommonGoalList().get(0).getID() + 1;
        int number2 = model.getCommonGoalList().get(1).getID() + 1;

        commonGoal1.setImage(new Image("gui/gameGraphics/common_goal_cards/" + number1 + ".jpg"));
        commonGoal2.setImage(new Image("gui/gameGraphics/common_goal_cards/" + number2 + ".jpg"));

        imageScoring1.setImage(new Image("gui/gameGraphics/scoring_tokens/scoring_" + model.getCommonGoalList().get(0).showPointsStack().get(model.getCommonGoalList().get(0).showPointsStack().size() - 1) + ".jpg"));
        imageScoring2.setImage(new Image("gui/gameGraphics/scoring_tokens/scoring_" + model.getCommonGoalList().get(1).showPointsStack().get(model.getCommonGoalList().get(1).showPointsStack().size() - 1) + ".jpg"));
    }


    @FXML
    protected void readChat() throws IOException {
        clicked = false;
        controller.loadScene(SceneEnum.chat);
    }

    protected void setBoard() {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                if((j == 0 && i == 0) || (j == 0 && i == 1) || (j == 0 && i == 2) || (j == 0 && i == 5) || (j == 0 && i == 6) || (j == 0 && i == 7) || (j == 0 && i == 8)
                        || (j == 1 && i == 0) || (j == 1 && i == 1) || (j == 1 && i == 2) || (j == 1 && i == 6) || (j == 1 && i == 7) || (j == 1 && i == 8)) {
                    j++;
                }
                ImageView imageView = new ImageView();
                imageView.setImage(new Image("gui/gameGraphics/item_tiles/Piante1_3.png"));
                imageView.setFitHeight(25.0);
                imageView.setFitWidth(25.0);
                imageView.setLayoutX(iWidth + i*25.0);
                imageView.setLayoutY(iHeight + j*25.0);
                anchor.getChildren().add(imageView);
            }
        }

        //Alla fine dovrà essere tipo così
        /*for(int i = 0; i < client.getController().getBoard().getNumRows(); i++) {
            for (int j = 0; j < client.getController().getBoard().getNumColumns(); j++) {
                ImageView imageView = new ImageView();
                client.getController().getBoard().getTile(i, j).toString();
                imageView.setImage(new Image("gui/gameGraphics/item_tiles/Piante1_3.png"));
                imageView.setFitHeight(25.0);
                imageView.setFitWidth(25.0);
                imageView.setLayoutX(iWidth + i*25.0);
                imageView.setLayoutY(iHeight + j*25.0);
                anchor.getChildren().add(imageView);

            }
        }*/
        canvasBoard.toFront();
    }

    protected void setShelf() {
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 6; j++) {
                ImageView imageView = new ImageView();
                imageView.setImage(new Image("gui/gameGraphics/item_tiles/Giochi1.1.png"));
                imageView.setFitHeight(24.0);
                imageView.setFitWidth(24.0);
                imageView.setLayoutX(iWidthShelf + i*35.0);
                imageView.setLayoutY(iHeightShelf + j*30.0);
                anchor.getChildren().add(imageView);
            }
        }
        canvasShelf.toFront();
    }


    //BOARD: 25 (column getX) x 25 (getY)
    //SHELF: 24 (getY) x 24 (getX)

    public void clickedMouseBoard(MouseEvent mouseEvent) throws InvalidMoveException {
        System.out.println("Board:");

        if(move.size() >= 3) {
            ClientControllerGUI.showError("Max number of positions selected");
        } else {
            int column = (int) ((mouseEvent.getX())/25) - 1;
            int row = (int) ((mouseEvent.getY())/25) - 1;

            if(row < 0) row += 1;
            if(column < 0) column += 1;
            System.out.println(mouseEvent.getSource().toString());
            System.out.println("Row: " + row);
            System.out.println("Column: " + column);
        /*PartialMove pm = new PartialMove();
        pm.addPosition(new Position(row, column));
        System.out.println(client.getController().getBoard().getValidPositions(pm));*/

            Position pos = new Position(row, column);
            move.add(pos);
            System.out.println(move);

            ImageView imageView = new ImageView();
            imageView.setImage(new Image("gui/gameGraphics/item_tiles/Gatti1.1.png"));
            imageView.setFitHeight(25.0);
            imageView.setFitWidth(25.0);
            imageView.setLayoutX(iWidth + column*25.0);
            imageView.setLayoutY(iHeight + row*25.0);
            anchor.getChildren().add(imageView);

        }


        canvasBoard.toFront();

        System.out.println("\n");
    }

    public void clickedMouseShelf(MouseEvent mouseEvent) {
        System.out.println("Shelf:");

        int column = (int) ((mouseEvent.getX())/30) - 1;
        if(column < 0) column += 1;

        System.out.println("Column: " + column);
        System.out.println("\n");

    }

    @FXML
    protected void gameStatus() throws IOException {
        controller.loadScene(SceneEnum.playerShelves);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boolean turn = model.isItMyTurn();
        if(!turn) {
            turnFlag.setStyle("-fx-fill: grey;");
        }
        setBoard();
        setShelf();
        getPersonalGoal();
        getCommonGoals();
        if(model.isItMyTurn()) {
            ImageView Image = new ImageView();
            Image.setImage(new Image("gui/gameGraphics/misc/firstplayertoken.png"));
            Image.setLayoutX(318.0);
            Image.setLayoutX(176.0);
            Image.setFitHeight(76.0);
            Image.setFitWidth(45.0);
        }
    }
}
