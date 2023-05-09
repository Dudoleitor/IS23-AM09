package it.polimi.ingsw.client.View.gui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ChoosePersonalGoalController {

    private final ClientGUI client = HelloController.getClient();

    @FXML
    VBox vbox;

    @FXML
    protected void goToMatch() {
        System.out.println("Go to match!");
    }

    @FXML
    protected void id1() throws IOException {
        client.getController().setPlayerGoal(1);
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("PlayerHomeScreen"), 800, 800));

    }
    @FXML
    protected void id2() throws IOException {
        client.getController().setPlayerGoal(2);
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("PlayerHomeScreen"), 800, 800));

    }
    @FXML
    protected void id3() throws IOException {
        client.getController().setPlayerGoal(3);
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("PlayerHomeScreen"), 800, 800));

    }
    @FXML
    protected void id4() throws IOException {
        client.getController().setPlayerGoal(4);
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("PlayerHomeScreen"), 800, 800));

    }
    @FXML
    protected void id5() throws IOException {
        client.getController().setPlayerGoal(5);
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("PlayerHomeScreen"), 800, 800));

    }
    @FXML
    protected void id6() throws IOException {
        client.getController().setPlayerGoal(6);
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("PlayerHomeScreen"), 800, 800));

    }
    @FXML
    protected void id7() throws IOException {
        client.getController().setPlayerGoal(7);
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("PlayerHomeScreen"), 800, 800));

    }
    @FXML
    protected void id8() throws IOException {
        client.getController().setPlayerGoal(8);
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("PlayerHomeScreen"), 800, 800));

    }
    @FXML
    protected void id9() throws IOException {
        client.getController().setPlayerGoal(9);
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("PlayerHomeScreen"), 800, 800));

    }
    @FXML
    protected void id10() throws IOException {
        client.getController().setPlayerGoal(10);
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("PlayerHomeScreen"), 800, 800));

    }
    @FXML
    protected void id11() throws IOException {
        client.getController().setPlayerGoal(11);
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("PlayerHomeScreen"), 800, 800));

    }
    @FXML
    protected void id12() throws IOException {
        client.getController().setPlayerGoal(2);
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.setScene(new Scene(client.loadScene("PlayerHomeScreen"), 800, 800));

    }

}
