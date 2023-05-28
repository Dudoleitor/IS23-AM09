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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.controller.gui.ClientControllerGUI.loadImage;
import static it.polimi.ingsw.client.controller.gui.ClientControllerGUI.showError;

public class HomeScreenController extends SceneController implements Initializable {
    boolean clicked = false;
    private GridHandler shelfHandler;
    private GridHandler boardHandler;
    private MoveBuilder moveBuilder;
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

    @FXML
    ImageView shelfImage;
    @FXML
    ImageView Tile1;
    @FXML
    ImageView Tile2;
    @FXML
    ImageView Tile3;

    private ImageView[] tileImages;

    //metto un label/text il cui testo sarà:
    //è una nuova partita o no

    protected void getPersonalGoal() {
        int number = model.getPlayerGoal().getGoalId() + 1;
        imgPersGoal.setImage(loadImage("personal_goal_cards/Personal_Goals" + number + ".png"));
    }

    public void updateCommonGoals(CommonGoal cg1, CommonGoal cg2) {
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

    /**
     * button that redirects to chat screen
     * @throws IOException
     */
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

    /**
     * creates a position based on mouseEvent and adds it to move builder
     * in order to create the partialMove
     * @param mouseEvent
     * @throws InvalidMoveException
     * @throws BadPositionException
     */
    public void clickedMouseBoard(MouseEvent mouseEvent) throws InvalidMoveException, BadPositionException {
        //check if it is player's turn
        if(!model.isItMyTurn()) {
            controller.errorMessage("Wait your turn");
            return;
        }

        Position pos = boardHandler.getPosition(mouseEvent);

        //check if move is valid
        if(!moveBuilder.validPositions(model.getBoard()).contains(pos)) {
            controller.errorMessage("Please enter a valid position");
            return;
        }

        //check if position fits in partial move
        try {
            moveBuilder.addPosition(pos);
            Image image = boardHandler.removeTile(pos);
            Tile tile = model.getBoard().getTile(pos);
            int n = moveBuilder.getPartialMove().getBoardPositions().size();
            tileImages[n-1].setImage(image);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /**
     * by clicking on the shelf, the user can choose the column
     * @param mouseEvent
     * @throws InvalidMoveException
     */
    public void clickedMouseShelf(MouseEvent mouseEvent) throws InvalidMoveException {
        System.out.println("Shelf:");

        int column = shelfHandler.getPosition(mouseEvent).getColumn();
        moveBuilder.setColumn(column);

        System.out.println("Column: " + column);
    }

    /**
     * button that resets the move
     */
    @FXML
    protected void deleteMove() {
        moveBuilder.resetMove();
        boardHandler.resetGrid(model.getBoard());
        boardHandler.displayGrid();
        Arrays.stream(tileImages).forEach(img -> img.setImage(null));
    }

    /**
     * button that confirms the move
     * if the user confirms: the controller posts the move to the server
     * and the shelf is updated
     * @throws LobbyException
     */
    @FXML
    protected void confirmMove() throws LobbyException{
        System.out.println("Confirm Move");
        try {
            controller.getServer().postMove(model.getPlayerName(), moveBuilder.getMove());
        } catch (Exception e) {
            deleteMove();
            showError(e.getMessage());
        }
        finally {
            moveBuilder.resetMove();
            Arrays.stream(tileImages).forEach(img -> img.setImage(null));
        }

    }

    /**
     * it redirects to the shelves page where the user can see opponents shelves
     * @throws IOException
     */
    @FXML
    protected void gameStatus() throws IOException {
        controller.loadScene(SceneEnum.playerShelves);
    }

    public void updateShelf(Shelf shelf) {
        shelfHandler.resetGrid(shelf);
        shelfHandler.displayGridBehind(shelfImage);
    }

    /**
     * This function is invoked from the server when the current
     * player puts a tile into his shelf.
     * @param pos destination in the shelf
     * @param tile   Tile to insert
     */
    public void putIntoShelf(Position pos, Tile tile) {
        shelfHandler.putTileBehind(shelfImage,pos,tile);
    }

    /**
     * This function is invoked when a player makes a move, it picks
     * a single tile from the board.
     * @param position the position of the tile to be removed
     */
    public void removeFromBoard(Position position) {
        boardHandler.removeTile(position);
    }

    /**
     * sets the turn, prepares the board and the shelf, prepares personal and common goals
     * if the user is the first player, the first player token it is shown
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boolean turn = model.isItMyTurn();
        if(!turn) {
            turnFlag.setStyle("-fx-fill: grey;");
        }
        boardHandler = new GridHandler(anchor,canvasBoard,model.getBoard());
        boardHandler.displayGrid();
        shelfHandler = new GridHandler(anchor,canvasShelf,model.getPlayersShelves().get(model.getPlayerName()));
        shelfHandler.displayGridBehind(shelfImage);

        getPersonalGoal();
        updateCommonGoals(model.getCommonGoalList().get(0), model.getCommonGoalList().get(1));
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
        moveBuilder = new MoveBuilder();

        tileImages = new ImageView[]{Tile1,Tile2,Tile3};
    }
}

class MoveBuilder{
    private PartialMove  pm;
    private Move move;
    private int column;
    public MoveBuilder(){
        pm = new PartialMove();
        move = null;
        column = -1;
    }
    public void addPosition(Position pos) throws Exception {
        if(pm.size() >= 3){
            throw new Exception("Max number of positions selected");
        }
        else{
            pm.addPosition(pos);
        }
    }
    public List<Position> validPositions(Board board){
        try {
            return board.getValidPositions(pm);
        } catch (InvalidMoveException e) {
            return new ArrayList<>();
        }
    }
    public void setColumn(int column){
        this.column = column;
    }
    public Move getMove() throws Exception{
        if(column == -1){
            throw new Exception("Please select a column");
        }
        if(pm.size() == 0){
            throw new Exception("Please select at least one tile");
        }
        return new Move(pm,column);
    }
    public PartialMove getPartialMove() {
        return pm;
    }
    public int getColumn(){
        return column;
    }
    public void resetMove(){
        pm = new PartialMove();
        move = null;
        column = -1;
    }
}
