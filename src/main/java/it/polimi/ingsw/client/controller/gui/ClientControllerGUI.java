package it.polimi.ingsw.client.controller.gui;
import it.polimi.ingsw.client.connection.LobbyException;
import it.polimi.ingsw.client.connection.Server;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.gui.sceneControlles.HomeScreenController;
import it.polimi.ingsw.client.controller.gui.sceneControlles.SceneController;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.server.clientonserver.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    private final Map<SceneEnum, FXMLSceneWithController> scenes = new HashMap<>();
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

    /**
     * This method is used to set the newMatch flag.
     */
    public void setNewMatch(boolean newMatch) {
        this.newMatch = newMatch;
    }

    /**
     * This method is used to check if the game was loaded
     * from a previous one or if it is a new game.
     * @return true if the game is new, false otherwise.
     */
    public boolean isNewMatch() {
        return newMatch;
    }

    /**
     * This method is used to load a scene.
     * @param scene the scene to be loaded.
     */
    public void loadScene(SceneEnum scene) {
        final Scene javafxScene;
        if (scenes.containsKey(scene)) { // Scene was previously loaded
            javafxScene = scenes.get(scene).getScene();
        } else {
            javafxScene = generateNewScene(scene);
        }
        currentScene = scene;
        stage.setScene(javafxScene);

        if(scene.equals(SceneEnum.chat)) {
            final HomeScreenController homeScreenController =
                    (HomeScreenController) scenes.get(SceneEnum.home).getController();
            if (homeScreenController!=null){
                homeScreenController.setNewMessage(false);
            }
        }
    }

    /**
     * This method is used to generate a new scene with its controller.
     * The method also adds the scene to the scenes map.
     * @param scene the scene to be generated
     * @return Scene object
     */
    private Scene generateNewScene(SceneEnum scene) {
        final FXMLLoader loader = new FXMLLoader(scene.getResource());
        final SceneController sceneController = scene.getNewController(this);
        loader.setController(sceneController);
        final Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Error while loading scene " + scene);
        }
        Scene javafxScene = new Scene(parent, 800, 800); //TODO decorate and remove magic numbers
        scenes.put(scene,
                new FXMLSceneWithController(javafxScene, sceneController));
        return javafxScene;
    }

    /**
     * This method is used to get the controller of a scene.
     * @param scene the scene of which we want the controller.
     * @return null if the scene was never loaded,
     *      FxmlController the controller of the scene otherwise
     */
    public SceneController getSceneController(SceneEnum scene) {
        FXMLSceneWithController sceneWithController = scenes.get(scene);
        if (sceneWithController == null)
            return null;
        return sceneWithController.getController();
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
        stage.getIcons().add(loadImage("/Publisher_material/Icon_50x50px.png"));
        stage.setOnCloseRequest((e) -> {
            if(server != null) {
                try {
                    System.out.println("Quit");
                    server.quitGame(client.getPlayerName());
                } catch (LobbyException ignored) {
                }
            }
            stage.close();
        });
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

/**
 * This class is used to bind together a javafx scene
 * with its controller
 */
class FXMLSceneWithController{
    private final Scene scene;
    private final SceneController controller;
    FXMLSceneWithController(Scene scene, SceneController controller) {
        this.scene = scene;
        this.controller = controller;
    }

    public Scene getScene() {
        return scene;
    }

    public SceneController getController() {
        return controller;
    }
}