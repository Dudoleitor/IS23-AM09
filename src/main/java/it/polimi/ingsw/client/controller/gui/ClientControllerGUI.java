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
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ClientControllerGUI extends Application implements ClientController {
    private ClientModelGUI model;
    private Server server;
    private Client client;
    private Stage stage;

    /**
     * newMatch true if the game is new,
     * false if it was loaded from a save or the player reconnected.
     */
    private boolean newMatch;

    private final Map<SceneEnum, FxmlController> sceneControllers = new HashMap<>();
    private SceneEnum currentScene = SceneEnum.login;

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
    public void setChatUpdate() {

    }
    public SceneEnum getCurrentScene() {
        return currentScene;
    }

    public void setNewMatch(boolean newMatch) {
        this.newMatch = newMatch;
    }
    public boolean isNewMatch() {
        return newMatch;
    }

    /**
     * This method is used to load a scene.
     * @param scene the scene to be loaded.
     */
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
        sceneControllers.put(scene, sceneController);
        currentScene = scene;
        stage.setScene(new Scene(parent, 800, 800));
    }

    /**
     * This method is used to get the controller of a scene.
     * @param scene the scene of which we want the controller.
     * @return FxmlController the controller of the scene.
     */
    public FxmlController getSceneController(SceneEnum scene) {
        return sceneControllers.get(scene);
    }

    public static Image loadImage(String fileName) {
        try {
            final URL url = ClientControllerGUI
                    .class
                    .getClassLoader()
                    .getResource("gui/gameGraphics/" + fileName);
            if(url == null) {
                throw new IOException("File not found");
            }
            return new Image(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException("Error while loading image " + fileName + " :" + e.getMessage());
        }
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
