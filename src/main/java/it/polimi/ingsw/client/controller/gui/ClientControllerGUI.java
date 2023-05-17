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

    private ClientModelGUI model;
    private Server server;
    private Client client;

    public ClientModelGUI getModel(){
        return model;
    }
    public void setModel(ClientModelGUI model) {
        this.model = model;
    }
    public Server getServer() {
        return server;
    }
    public void setServer(Server server) {
        this.server = server;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public boolean gameIsStarted() {
        return model.gameIsStarted();
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
