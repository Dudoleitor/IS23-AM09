package it.polimi.ingsw.client.controller.gui;
import it.polimi.ingsw.client.model.ClientModelGUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientGUI extends Application {

    private ClientModelGUI controller;
    private ClientControllerGUI clientControllerGui;

    public Parent loadScene(String scene) throws IOException {
        return FXMLLoader.load(ClientGUI.class.getClassLoader().getResource(String.format("gui/%s.fxml", scene)));
    }


    public void start(Stage stage) throws IOException {
        Parent root = loadScene("Start");
        Scene scene = new Scene(root, 800, 800, Color.GREEN);
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

    public ClientModelGUI getController() {
        return controller;
    }

    public void setController(ClientModelGUI controller) {
        this.controller = controller;
    }

    public ClientControllerGUI getGui() {
        return clientControllerGui;
    }

    public void setGui(ClientControllerGUI clientControllerGui) {
        this.clientControllerGui = clientControllerGui;
    }

    public static void startApp() {launch();}

    static void startMyProtocols() {
        return;
    }
}
