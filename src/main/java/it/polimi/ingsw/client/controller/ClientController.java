package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.controller.cli.LobbyCommand;
import it.polimi.ingsw.client.controller.cli.LobbySelectionCommand;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.model.Board;
import it.polimi.ingsw.shared.model.Move;
import it.polimi.ingsw.shared.model.Shelf;

import java.util.Map;

public interface ClientController {
    void startClient();
}
