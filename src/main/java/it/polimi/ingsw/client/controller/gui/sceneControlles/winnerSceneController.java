package it.polimi.ingsw.client.controller.gui.sceneControlles;

import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.client.controller.gui.SceneEnum;
import it.polimi.ingsw.client.model.ClientModelGUI;
import it.polimi.ingsw.shared.PlayerWithPoints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class winnerSceneController extends SceneController implements Initializable {


    private final ClientModelGUI model;
    private final String playerName;

    @FXML
    Text winnerText;

    @FXML
    Text username1;
    @FXML
    Text username2;
    @FXML
    Text username3;
    @FXML
    Text username4;

    @FXML
    AnchorPane anchor;

    public winnerSceneController(ClientControllerGUI controller) {
        super(controller);
        this.model = controller.getModel();
        this.playerName = controller.getClient().getPlayerName();
    }

    @FXML
    protected void playAgain() {
        controller.loadScene(SceneEnum.lobbySelection);
    }

    /**
     * creates a list of players with their points, sort it and put them (in the correct order)
     * in the podium
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final List<PlayerWithPoints> players = model.getLeaderBoard();
        Text[] userNames = new Text[]{username1,username2,username3, username4};

        int i = 0;
        for(PlayerWithPoints player : players) {
           userNames[i].setText(player.getPlayerName() + ": " + player.getPoints());
           i++;
        }

        for(;i < players.size() && i < userNames.length;i++){
            userNames[i].setStyle("-fx-opacity:0.0");
        }

        if(!players.get(0).getPlayerName().equals(playerName))
            winnerText.setText("YOU LOST");
    }
}
