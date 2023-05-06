package it.polimi.ingsw.client.View.gui;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Random;

public class homeScreenController {

    private final ClientGUI client = HelloController.getClient();
    boolean clicked = false;

    @FXML
    Button goalsButton;

    @FXML
    ImageView imgPersGoal;

   @FXML
   ImageView commonGoal1;

    @FXML
    ImageView commonGoal2;

    //qua mi servirà un metodo che mi dà il personal goal del player
    private void randPG() {
        imgPersGoal.setImage(new Image("gui/gameGraphics/personal_goal_cards/Personal_Goals" + client.getController().getPlayerGoal().getGoalId() + ".png"));
    }

    //qua mi servirà un metodo che mi dà i common goals della partita
    private void randCG () {
        Random rand = new Random();
        int int_random = rand.nextInt(12);
        int int_random_2 = rand.nextInt(12);
        while(int_random_2 == int_random) {
            int_random_2 = rand.nextInt(12);
        }
        int_random += 1;
        int_random_2 += 1;

        commonGoal1.setImage(new Image("gui/gameGraphics/common_goal_cards/" + int_random + ".jpg"));
        commonGoal2.setImage(new Image("gui/gameGraphics/common_goal_cards/" + int_random_2 + ".jpg"));

    }

    @FXML
    protected void randGoals() {
        clicked = true;
        if(clicked) {
            goalsButton.setOpacity(0.0);
        } else {
            goalsButton.setOpacity(1.0);
        }
        randPG();
        randCG();

    }

    @FXML
    protected void readChat() throws IOException {
        clicked = false;
        Stage stage = (Stage) imgPersGoal.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("Chat"), 800, 800));
    }

    //BOARD: 25 (column getX) x 25 (getY)
    //SHELF: 24 (getY) x 24 (getX)

    public void clickedMouseBoard(MouseEvent mouseEvent) {
        System.out.println("Board:");

        int column = (int) ((mouseEvent.getX())/25) - 1;
        int row = (int) ((mouseEvent.getY())/25) - 1;
        if(row < 0) row += 1;
        if(column < 0) column += 1;
        System.out.println("Row: " + row);
        System.out.println("Column: " + column);
        System.out.println("\n");
    }

    public void clickedMouseShelf(MouseEvent mouseEvent) {
        System.out.println("Shelf:");

        int column = (int) ((mouseEvent.getX())/25) - 1;
        int row = (int) ((mouseEvent.getY())/25) - 1;
        if(row < 0) row += 1;
        if(column < 0) column += 1;
        System.out.println("Row: " + row);
        System.out.println("Column: " + column);
        System.out.println("\n");
    }
}
