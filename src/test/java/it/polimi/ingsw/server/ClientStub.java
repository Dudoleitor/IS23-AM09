package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Client;
import it.polimi.ingsw.shared.Position;
import it.polimi.ingsw.shared.Tile;

public class ClientStub implements Client {
    private String name;
    private boolean updated;

    public ClientStub(String name) {
        this.name = name;
        this.updated = false;
    }

    @Override
    public String getPlayerName() {
        return name;
    }

    @Override
    public void pickedFromBoard(Position position) {
        this.updated = true;
    }

    @Override
    public void refreshBoard(String board) {
        this.updated = true;
    }

    @Override
    public void putIntoShelf(String player, int column, Tile tile) {
        this.updated = true;
    }

    @Override
    public void refreshShelf(String player, String shelf) {
        this.updated = true;
    }

    public boolean wasUpdated() {
        return updated;
    }
    public void reset() {
        this.updated = false;
    }
}