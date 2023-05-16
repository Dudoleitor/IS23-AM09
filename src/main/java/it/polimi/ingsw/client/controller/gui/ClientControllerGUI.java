package it.polimi.ingsw.client.controller.gui;
import it.polimi.ingsw.client.connection.Server;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.server.clientonserver.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientControllerGUI extends Application implements ClientController {

    public static ClientControllerGUI controller;

    public static ClientModelGUI model;
    private static Server server;
    private static Client client;

    public Server getServer() {
        return ClientControllerGUI.server;
    }
    public void setServer(Server server) {
        ClientControllerGUI.server = server;
    }
    public Client getClient() {
        return ClientControllerGUI.client;
    }
    public void setClient(Client client) {
        ClientControllerGUI.client = client;
    }

    public static Parent loadScene(String scene) throws IOException {
        return FXMLLoader.load(ClientControllerGUI.class.getClassLoader().getResource(String.format("gui/%s.fxml", scene)));
    }


    public void start(Stage stage) throws IOException {
        ClientControllerGUI.controller = this;
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
    public void errorMessage(String message) {
        ClientControllerGUI.showError(message);
    }

    public void startClient() {launch();}
}
