package it.polimi.ingsw.client.View;

public enum LobbySelectionCommand{
    Invalid(),
    Number(),
    Create(),
    Random();
    private int id = -1;
    public void setId(int id){
        this.id = id;
    }
    public int getID(){
        return id;
    }
    public boolean isValid(){
        return this != Invalid;
    }
}
