package it.polimi.ingsw.client.View;

import it.polimi.ingsw.client.View.cli.InputSanitizer;

import java.util.Arrays;

public enum LobbySelectionCommand {
    Invalid(),
    Number(),
    Create(),
    Refresh(),
    Random();
    private int id = -1;
    public void setId(int id){
        this.id = id;
    }
    public int getID(){
        return id;
    }
    public boolean isValid(){
        return this != Invalid && this != Refresh;
    }
}
