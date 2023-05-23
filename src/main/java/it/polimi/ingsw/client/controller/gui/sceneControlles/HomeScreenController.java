package it.polimi.ingsw.client.controller.gui.sceneControlles;
import it.polimi.ingsw.client.connection.LobbyException;
import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.client.controller.gui.GridHandler;
import it.polimi.ingsw.client.controller.gui.SceneEnum;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.shared.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.controller.gui.ClientControllerGUI.loadImage;

public class HomeScreenController extends SceneController implements Initializable {
    boolean clicked = false;
    private GridHandler shelfHandler;
    private GridHandler boardHandler;
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

    @FXML
    Button readChatButton;

    @FXML
    Circle notificationCircle;

    @FXML
    Text newMatchText;

    //metto un label/text il cui testo sarà:
    //è una nuova partita o no

    protected void getPersonalGoal() {
        int number = model.getPlayerGoal().getGoalId() + 1;
        imgPersGoal.setImage(loadImage("personal_goal_cards/Personal_Goals" + number + ".png"));
    }

    private void getCommonGoals () {

        CommonGoal cg1 = model.getCommonGoalList().get(0);
        CommonGoal cg2 = model.getCommonGoalList().get(1);

        commonGoal1.setImage(loadImage( "common_goal_cards/" + cg1.getID() + ".jpg"));
        commonGoal2.setImage(loadImage("common_goal_cards/" + cg2.getID() + ".jpg"));

        imageScoring1.setImage(loadImage("scoring_tokens/scoring_" + cg1.peekTopOfPointsStack() + ".jpg"));
        imageScoring2.setImage(loadImage("scoring_tokens/scoring_" + cg2.peekTopOfPointsStack() + ".jpg"));
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

    /**
     * @param newMessage, if true there is a new message and the chat button gets blue
     */
    public void setNewMessage(boolean newMessage) {
        if(newMessage) {
            readChatButton.setStyle("-fx-background-color: #456938;");
            notificationCircle.setOpacity(1.0);
        } else {
            readChatButton.setStyle("-fx-background-color: #49be25;");
            notificationCircle.setOpacity(0.0);
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
        boardHandler.resetGrid(board);
        boardHandler.displayGrid();
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

            Position pos = boardHandler.getPosition(mouseEvent);

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
            pm.addPosition(pos);

            System.out.println("Move: " + move);

        canvasBoard.toFront();

        System.out.println("\n");
    }

    public void clickedMouseShelf(MouseEvent mouseEvent) throws InvalidMoveException {
        System.out.println("Shelf:");

        int column = shelfHandler.getPosition(mouseEvent).getColumn();

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
        shelfHandler.resetGrid(shelf);
        shelfHandler.displayGrid();
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
        int iWidth = 0;
        int iHeight = 0;
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
        boardHandler = new GridHandler(anchor,canvasBoard,model.getBoard());
        setBoard(model.getBoard());
        shelfHandler = new GridHandler(anchor,canvasShelf,model.getPlayersShelves().get(model.getPlayerName()));
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

        if(controller.isNewMatch()) {
            newMatchText.setText("You are playing a new match!");
        } else {
            newMatchText.setText("You are playing a loaded match!");
        }
    }
}
