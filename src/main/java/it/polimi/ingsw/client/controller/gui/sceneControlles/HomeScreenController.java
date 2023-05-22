package it.polimi.ingsw.client.controller.gui.sceneControlles;
import it.polimi.ingsw.client.connection.LobbyException;
import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.client.controller.gui.SceneEnum;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.shared.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.controller.gui.ClientControllerGUI.loadImage;

public class HomeScreenController extends SceneController implements Initializable {
    boolean clicked = false;
    private final double iHeight = 85.0;
    private final double iWidth = 49.0;
    private final double iHeightShelf = 130.0;
    private final double iWidthShelf = 390.0;

    private List<Position> move = new ArrayList<>();
    private PartialMove partialMove = new PartialMove();
    Move actualMove = null;

    private final ClientModelGUI model;

    public HomeScreenController(ClientControllerGUI controller) {
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
        imgPersGoal.setImage(loadImage("personal_goal_cards/Personal_Goals" + number + ".png"));
    }

    private void getCommonGoals () {

        int number1 = model.getCommonGoalList().get(0).getID();
        int number2 = model.getCommonGoalList().get(1).getID();

        commonGoal1.setImage(loadImage( "common_goal_cards/" + number1 + ".jpg"));
        commonGoal2.setImage(loadImage("common_goal_cards/" + number2 + ".jpg"));

        imageScoring1.setImage(loadImage("scoring_tokens/scoring_" + model.getCommonGoalList().get(0).showPointsStack().get(model.getCommonGoalList().get(0).showPointsStack().size() - 1) + ".jpg"));
        imageScoring2.setImage(loadImage("scoring_tokens/scoring_" + model.getCommonGoalList().get(1).showPointsStack().get(model.getCommonGoalList().get(1).showPointsStack().size() - 1) + ".jpg"));
    }

    /**
     * @param turn: if turn == true, then it is my turn
     */
    public void setMyTurn(boolean turn) {
        if(turn) {
            turnFlag.setStyle("-fx-fill: yellow;");
        } else {
            turnFlag.setStyle("-fx-fill: grey;");
        }
    }

    @FXML
    protected void readChat() throws IOException {
        clicked = false;
        controller.loadScene(SceneEnum.chat);
    }

    /**
     * This method is used to completely refresh the board
     * @param board new Board object
     */
    public void setBoard(Board board) {
        try {
            for (int i = 0; i < board.getNumRows(); i++) {
                for (int j = 0; j < board.getNumColumns(); j++) {
                    if (!board.getTile(i, j).toString().equals("I") && !board.getTile(i, j).toString().equals("E")) {
                        ImageView imageView = new ImageView();
                        imageView.setImage(loadImage("item_tiles/" + board.getTile(i, j).toString() + "2.png"));
                        imageView.setFitHeight(25.0);
                        imageView.setFitWidth(25.0);
                        imageView.setLayoutX(iWidth + j * 25.0);
                        imageView.setLayoutY(iHeight + i * 25.0);

                        anchor.getChildren().add(imageView);
                    }
                }
            }
            canvasBoard.toFront();
        } catch (BadPositionException e) {
            throw new RuntimeException("Invalid board in setBoard: " + e.getMessage());
        }
    }

    //BOARD: 25 (column getX) x 25 (getY)
    //SHELF: 24 (getY) x 24 (getX)

    public void clickedMouseBoard(MouseEvent mouseEvent) throws InvalidMoveException, BadPositionException {
        System.out.println("Board:");

        if(!model.isItMyTurn()) {
            controller.errorMessage("Wait your turn");
            return;
        }

        if(move.size() >= 3) {
            ClientControllerGUI.showError("Max number of positions selected");
            return;
        }

            int column = (int) ((mouseEvent.getX())/25) - 1;
            int row = (int) ((mouseEvent.getY())/25) - 1;

            if(row < 0) row += 1;
            if(column < 0) column += 1;

            Position pos = new Position(row, column);

            if(model.getBoard().getValidPositions(partialMove).isEmpty()) {
                controller.errorMessage("There are no more tiles to pick");
            }

            if(model.getBoard().getValidPositions(partialMove).contains(pos)) {
               partialMove.addPosition(pos);
                move.add(pos);
            } else {
                controller.errorMessage("Please enter a valid position");
            }

            PartialMove pm = new PartialMove();
            pm.addPosition(new Position(row, column));

            //setBoardValidPositions(partialMove);

            //System.out.println(model.getBoard().getValidPositions(pm));

            System.out.println("Move: " + move);

        canvasBoard.toFront();

        System.out.println("\n");
    }

    public void clickedMouseShelf(MouseEvent mouseEvent) throws InvalidMoveException {
        System.out.println("Shelf:");

        int column = (int) ((mouseEvent.getX())/30) - 1;
        if(column < 0) column += 1;

        PartialMove pm = new PartialMove();
        for(int i = 0; i < move.size(); i++) {
            Position pos = new Position(move.get(i).getRow(), move.get(i).getColumn());
            pm.addPosition(pos);
        }

        actualMove = new Move(pm, column);

        System.out.println("Column: " + column);
        System.out.println("\n");

    }

    @FXML
    protected void deleteMove() {
        move.clear();
    }

    @FXML
    protected void confirmMove() throws InvalidMoveException, LobbyException, BadPositionException {
        System.out.println("Confirm Move");

        if(actualMove == null) {
            controller.errorMessage("Click a column");
            return;
        }

        controller.getServer().postMove(model.getPlayerName(), actualMove);
        //removeFromBoard(actualMove);

        updateShelf(model.getPlayersShelves().get(model.getPlayerName()));
        move.clear();
        actualMove = null;
        partialMove = null;
    }

    @FXML
    protected void gameStatus() throws IOException {
        controller.loadScene(SceneEnum.playerShelves);
    }

    public void updateShelf(Shelf shelf) {
        try {
            for (int i = 0; i < shelf.getRows(); i++) {
                for (int j = 0; j < shelf.getColumns(); j++) {

                    if (!shelf.getTile(i, j).toString().equals("I") && !shelf.getTile(i, j).toString().equals("E")) {

                        ImageView imageView = new ImageView();
                        imageView.setImage(loadImage("item_tiles/" + shelf.getTile(i, j) + "2.png"));
                        imageView.setFitHeight(24.0);
                        imageView.setFitWidth(24.0);
                        imageView.setLayoutX(iWidthShelf + j * 35.0);
                        imageView.setLayoutY(iHeightShelf + i * 30.0);
                        anchor.getChildren().add(imageView);
                    }
                }
            }
            canvasShelf.toFront();
        } catch (BadPositionException e) {
            throw new RuntimeException("Invalid shelf in updateShelf: " + e.getMessage());
        }
    }

    /**
     * This function is invoked from the server when the current
     * player puts a tile into his shelf.
     * @param column destination column of the shelf
     * @param tile   Tile to insert
     */
    public void putIntoShelf(int column, Tile tile) {
        //TODO
        updateShelf(model.getPlayersShelves().get(model.getPlayerName()));  // TEMPORARY
    }

    /**
     * This function is invoked when a player makes a move, it picks
     * a single tile from the board.
     * @param position the position of the tile to be removed
     */
    public void removeFromBoard(Position position) {
        for(int j = 0; j < anchor.getChildren().size(); j++) {
            if(anchor.getChildren().get(j).getLayoutX() == (iWidth + position.getColumn()*25.0)
            && anchor.getChildren().get(j).getLayoutY() == (iHeight + position.getRow()*25.0)) {
                anchor.getChildren().get(j).setOpacity(0.0);
                anchor.getChildren().get(j).setOpacity(0.0);
                break;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boolean turn = model.isItMyTurn();
        if(!turn) {
            turnFlag.setStyle("-fx-fill: grey;");
        }
        setBoard(model.getBoard());
        updateShelf(model.getPlayersShelves().get(model.getPlayerName()));

        getPersonalGoal();
        getCommonGoals();
        if(model.isItMyTurn()) {
            ImageView Image = new ImageView();
            Image.setImage(loadImage("misc/firstplayertoken.png"));
            Image.setLayoutX(318.0);
            Image.setLayoutX(176.0);
            Image.setFitHeight(76.0);
            Image.setFitWidth(45.0);
        }
    }
}
