package it.polimi.ingsw.client.LobbySelection;

public enum LobbySelectionCommand{
    Invalid(),
    Number(),
    Random();
    private int id = -1;
    public void setId(int id){
        this.id = id;
    }
    public int getID(){
        return id;
    }
}
