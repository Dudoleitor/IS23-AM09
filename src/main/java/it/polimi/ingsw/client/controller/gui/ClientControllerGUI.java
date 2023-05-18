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
    private ClientModelGUI model;
    private Server server;
    private Client client;
    private Stage stage;

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
    public Stage getStage() {
        return stage;
    }
    public boolean gameIsStarted() {
        return model.gameIsStarted();
    }

    public void loadScene(SceneEnum scene) {
        final FXMLLoader loader = new FXMLLoader(scene.getResource());
        final FxmlController sceneController = scene.getNewController(this);
        loader.setController(sceneController);
        final Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Error while loading scene " + scene);
        }
        stage.setScene(new Scene(parent, 800, 800, Color.GREEN));
    }


    public void start(Stage stage) throws IOException {
        this.stage = stage;
        stage.setTitle("My Shelfie");
        loadScene(SceneEnum.login);
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
