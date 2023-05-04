package it.polimi.ingsw.client.Connection.TCPThread;

import it.polimi.ingsw.client.View.View;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.MessageTcp;
import it.polimi.ingsw.shared.model.Position;
import it.polimi.ingsw.shared.model.Tile;
import it.polimi.ingsw.shared.model.TileGenericException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ServerTCPViewUpdater extends Thread{
    private boolean exit = false;
    private ClientController clientController;
    private final ArrayList<MessageTcp> update;
    ServerTCPViewUpdater(ClientController clientController, ArrayList<MessageTcp> update){
        this.clientController = clientController;
        this.update = update;
    }
    @Override
    public void run() {
        MessageTcp updateMessage;
        while (!exit) {
            try {
                while (update.isEmpty()) {
                    synchronized (update) {
                        update.wait();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (update){
                updateMessage = update.get(0);
                update.remove(0);
            }
            synchronized (clientController){
                executeViewUpdate(updateMessage.getCommand(), updateMessage.getContent());
            }
        }
    }

    private void executeViewUpdate(MessageTcp.MessageCommand command, JSONObject content){
        switch(command){
            case PickedFromBoard:
                pickedFromBoard(content);
                break;
            case RefreshBoard:
                refreshBoard(content);
                break;
            case PutIntoShelf:
                putIntoShelf(content);
                break;
            case RefreshShelf:
                refreshShelf(content);
                break;
            case ChatMessageUpdate:
                postChatMessage(content);
                break;
            case RefreshChat:
                refreshChat(content);
                break;
            case NotifyStart:
                gameStarted();
                break;
            case UpdateTurn:
                updateTurn(content);
                break;
            case RefreshCommonGoals:
                refreshCommonGoal(content);
                break;
            case SetPlayerGoal:
                setPlayerGoal(content);
                break;
            case Disconnect:
                disconnect();
                break;
            case Ping:
                ping();
                break;
        }
    }

    public void exit(){
        exit = true;
    }
    public void changeView(ClientController clientController){
        this.clientController = clientController;
    }
    public void pickedFromBoard(JSONObject content) {
        try {
            clientController.pickedFromBoard((JSONObject) content.get("position"));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshBoard(JSONObject content) {
        try {
            clientController.refreshBoard((JSONObject) content.get("board"));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void putIntoShelf(JSONObject content) {
        try {
            String player = content.get("player").toString();
            int column = Integer.parseInt(content.get("column").toString());
            Tile tile = Tile.valueOfLabel(content.get("tile").toString());

            clientController.putIntoShelf(player,column,tile);
        } catch (TileGenericException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshShelf(JSONObject content) {
        try {
            clientController.refreshShelf(content.get("player").toString(),(JSONObject) content.get("board"));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void postChatMessage(JSONObject content) {
        try {
            clientController.postChatMessage(content.get("sender").toString(), content.get("message").toString());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshChat(JSONObject content) {
        try {
            clientController.refreshChat(new Chat((JSONObject) content.get("chat")));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void gameStarted() {
        try {
            clientController.gameStarted();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
    public void updateTurn(JSONObject content) {
        try {
            clientController.nextTurn(content.get("player").toString());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshCommonGoal(JSONObject content) {
        int id = Integer.parseInt(content.get("id").toString());
        List list = Jsonable.json2listInt((JSONArray) content.get("points"));
        try {
            clientController.refreshCommonGoal(id,list);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPlayerGoal(JSONObject content) {
        int id = Integer.parseInt(content.get("id").toString());
        try {
            clientController.setPlayerGoal(id);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        //TODO
    }

    public String ping() {
        try {
            clientController.ping();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
