package it.polimi.ingsw.client.View.gui;
import it.polimi.ingsw.client.controller.ClientControllerGUI;
import it.polimi.ingsw.server.clientonserver.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientGUI extends Application {

    private ClientControllerGUI controller;

    public static Parent loadScene(String scene) throws IOException {
        return FXMLLoader.load(ClientGUI.class.getClassLoader().getResource(String.format("gui/%s.fxml", scene)));
    }


    public void start(Stage stage) throws IOException {
        Parent root = loadScene("Start");
        Scene scene = new Scene(root, 320, 240, Color.GREEN);
        stage.setTitle("My Shelfie");
        stage.setScene(scene);
        stage.show();
    }

    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.show();
    }

    public void changeScene(String sceneName) throws IOException {
        Parent root = loadScene(sceneName);
        Scene scene = new Scene(root, 320, 240, Color.GREEN);
        Stage stage = new Stage();
        stage.setTitle("My Shelfie - Home");
        stage.setScene(scene);
        stage.show();
    }

    public ClientControllerGUI getController() {
        return controller;
    }

    public ClientControllerGUI setController(ClientControllerGUI controller) {
        this.controller = controller;
        return controller;
    }

    public static void startApp() {launch();}

    static void startMyProtocols() {
        return;
    }
}
