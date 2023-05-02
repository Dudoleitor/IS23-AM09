package it.polimi.ingsw.client.View.gui;
import it.polimi.ingsw.client.controller.ClientControllerGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.IOException;
import java.util.Random;

public class homeScreenController {

    //HelloController helloController;
    private ClientGUI client = HelloController.getClient();
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
        Random rand = new Random();
        int int_random = rand.nextInt(12);

        if(int_random == 0) {
            imgPersGoal.setImage(new Image("gui/gameGraphics/personal_goal_cards/Personal_Goals.png"));
        } else {
            imgPersGoal.setImage(new Image("gui/gameGraphics/personal_goal_cards/Personal_Goals" + int_random + ".png"));
        }
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
    Button sh00;

    @FXML
    protected void hello() {
        sh00.setOpacity(1.0);
        sh00.setStyle("-fx-background-color: #9925be; ");
    }

    @FXML
    protected void readChat() throws IOException {
        clicked = false;
        client.changeScene("Chat");
    }

}
