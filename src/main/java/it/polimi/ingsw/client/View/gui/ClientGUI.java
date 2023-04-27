package it.polimi.ingsw.client.View.gui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientGUI extends Application {


    public static Parent loadScene(String scene) throws IOException {
        return FXMLLoader.load(ClientGUI.class.getClassLoader().getResource(String.format("gui/%s.fxml", scene)));
    }

    @Override
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

    public static void startApp() {launch();}
}
