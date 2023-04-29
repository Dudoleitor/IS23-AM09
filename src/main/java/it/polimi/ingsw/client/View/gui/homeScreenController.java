package it.polimi.ingsw.client.View.gui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class homeScreenController {

    private ClientGUI client = new ClientGUI();

    @FXML
    protected void hello() {
        System.out.println("Hello!");
    }

    @FXML
    protected void readChat() throws IOException {
        client.changeScene("Chat");
    }

}
