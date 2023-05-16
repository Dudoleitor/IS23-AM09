package it.polimi.ingsw.client.controller.gui;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.model.ClientModelGUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientControllerGUI extends Application implements ClientController {

    public static ClientModelGUI model;

    public static Parent loadScene(String scene) throws IOException {
        return FXMLLoader.load(ClientControllerGUI.class.getClassLoader().getResource(String.format("gui/%s.fxml", scene)));
    }


    public void start(Stage stage) throws IOException {
        Parent root = loadScene("Start");
        Scene scene = new Scene(root, 800, 800, Color.GREEN);
        stage.setTitle("My Shelfie");
        stage.setScene(scene);
        stage.show();
    }

    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.show();
    }

    public void startClient() {launch();}
}
