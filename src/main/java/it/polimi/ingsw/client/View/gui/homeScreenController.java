package it.polimi.ingsw.client.View.gui;
import it.polimi.ingsw.client.controller.ClientControllerGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.IOException;
import java.util.Random;

public class homeScreenController {

    private ClientGUI client = new ClientGUI();

    @FXML
    ImageView imgPersGoal;

    //qua mi servirà una funzione che mi dà il personal goal del player
    private void randPG() {
        Random rand = new Random();
        int int_random = rand.nextInt(12);

        if(int_random == 0) {
            imgPersGoal.setImage(new Image("gui/gameGraphics/personal_goal_cards/Personal_Goals.png"));
        } else {
            imgPersGoal.setImage(new Image("gui/gameGraphics/personal_goal_cards/Personal_Goals" + int_random + ".png"));
        }
    }

    @FXML
    protected void randPersonalGoal() {
        randPG();
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
        client.changeScene("Chat");
    }

}
