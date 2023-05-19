package it.polimi.ingsw.client.connection.TCPThread;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.MessageTcp;
import it.polimi.ingsw.shared.model.Tile;
import it.polimi.ingsw.shared.model.TileGenericException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ServerTCPViewUpdater extends Thread{
    private boolean exit = false;
    private ClientModel clientModel;
    private final ArrayList<MessageTcp> update;
    ServerTCPViewUpdater(ClientModel clientModel, ArrayList<MessageTcp> update){
        this.clientModel = clientModel;
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
            synchronized (clientModel){
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
    public void changeView(ClientModel clientModel){
        this.clientModel = clientModel;
    }
    public void pickedFromBoard(JSONObject content) {
        try {
            clientModel.pickedFromBoard((JSONObject) content.get("position"));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshBoard(JSONObject content) {
        try {
            clientModel.refreshBoard((JSONObject) content.get("board"));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void putIntoShelf(JSONObject content) {
        try {
            String player = content.get("player").toString();
            int column = Integer.parseInt(content.get("column").toString());
            Tile tile = Tile.valueOfLabel(content.get("tile").toString());

            clientModel.putIntoShelf(player,column,tile);
        } catch (TileGenericException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshShelf(JSONObject content) {
        try {
            clientModel.refreshShelf(content.get("player").toString(),(JSONObject) content.get("shelf"));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void postChatMessage(JSONObject content) {
        try {
            clientModel.postChatMessage(content.get("sender").toString(), content.get("message").toString());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshChat(JSONObject content) {
        try {
            clientModel.refreshChat(new Chat((JSONObject) content.get("chat")));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void gameStarted() {
        try {
            clientModel.gameStarted();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
    public void updateTurn(JSONObject content) {
        try {
            clientModel.nextTurn(content.get("player").toString());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshCommonGoal(JSONObject content) {
        int id = Integer.parseInt(content.get("id").toString());
        List list = Jsonable.json2listInt((JSONArray) content.get("points"));
        try {
            clientModel.refreshCommonGoal(id,list);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPlayerGoal(JSONObject content) {
        int id = Integer.parseInt(content.get("id").toString());
        try {
            clientModel.setPlayerGoal(id);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        //TODO
    }

    public void ping() {
        try {
            clientModel.ping();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
}
