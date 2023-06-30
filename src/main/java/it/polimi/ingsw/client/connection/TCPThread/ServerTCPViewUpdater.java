package it.polimi.ingsw.client.connection.TCPThread;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.shared.Chat;
import it.polimi.ingsw.shared.Jsonable;
import it.polimi.ingsw.shared.MessageTcp;
import it.polimi.ingsw.shared.PlayerWithPoints;
import it.polimi.ingsw.shared.model.Tile;
import it.polimi.ingsw.shared.model.TileGenericException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ServerTCPViewUpdater extends Thread{
    private boolean exit = false;
    private ClientModel clientModel;
    private final ArrayList<MessageTcp> update;
    private final PrintWriter serverOut;

    ServerTCPViewUpdater(ClientModel clientModel, ArrayList<MessageTcp> update, PrintWriter serverOut){
        this.clientModel = clientModel;
        this.update = update;
        this.serverOut = serverOut;
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
                gameStarted(content);
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
            case Ping:
                ping();
                break;
            case EndGame:
                endgame(content);
                break;
        }
    }

    public void changeView(ClientModel clientModel){
        this.clientModel = clientModel;
    }
    private void pickedFromBoard(JSONObject content) {
        try {
            clientModel.pickedFromBoard((JSONObject) content.get("position"));
        } catch (RemoteException e) {  // This should never happen, as the remote object is local
            throw new RuntimeException(e);
        }
    }

    private void refreshBoard(JSONObject content) {
        try {
            clientModel.refreshBoard((JSONObject) content.get("board"));
        } catch (RemoteException e) {  // This should never happen, as the remote object is local
            throw new RuntimeException(e);
        }
    }

    private void putIntoShelf(JSONObject content) {
        try {
            String player = content.get("player").toString();
            int column = Integer.parseInt(content.get("column").toString());
            Tile tile = Tile.valueOfLabel(content.get("tile").toString());

            clientModel.putIntoShelf(player,column,tile);
        } catch (TileGenericException | RemoteException e) {  // This should never happen, as the remote object is local
            throw new RuntimeException(e);
        }
    }

    private void refreshShelf(JSONObject content) {
        try {
            clientModel.refreshShelf(content.get("player").toString(),(JSONObject) content.get("shelf"));
        } catch (RemoteException e) {  // This should never happen, as the remote object is local
            throw new RuntimeException(e);
        }
    }

    private void postChatMessage(JSONObject content) {
        try {
            clientModel.postChatMessage(content.get("sender").toString(), content.get("message").toString());
        } catch (RemoteException e) {  // This should never happen, as the remote object is local
            throw new RuntimeException(e);
        }
    }

    private void refreshChat(JSONObject content) {
        try {
            clientModel.refreshChat(new Chat((JSONObject) content.get("chat")));
        } catch (RemoteException e) {  // This should never happen, as the remote object is local
            throw new RuntimeException(e);
        }
    }

    private void gameStarted(JSONObject content) {
        final boolean newMatch = Boolean.parseBoolean(content.get("newMatch").toString());
        try {
            clientModel.gameStarted(newMatch);
        } catch (RemoteException e) {  // This should never happen, as the remote object is local
            throw new RuntimeException(e);
        }

    }
    private void updateTurn(JSONObject content) {
        try {
            clientModel.nextTurn(content.get("player").toString());
        } catch (RemoteException e) {  // This should never happen, as the remote object is local
            throw new RuntimeException(e);
        }
    }

    private void refreshCommonGoal(JSONObject content) {
        int id = Integer.parseInt(content.get("id").toString());
        List list = Jsonable.json2listInt((JSONArray) content.get("points"));
        try {
            clientModel.refreshCommonGoal(id,list);
        } catch (RemoteException e) {  // This should never happen, as the remote object is local
            throw new RuntimeException(e);
        }
    }

    private void setPlayerGoal(JSONObject content) {
        int id = Integer.parseInt(content.get("id").toString());
        try {
            clientModel.setPlayerGoal(id);
        } catch (RemoteException e) {  // This should never happen, as the remote object is local
            throw new RuntimeException(e);
        }
    }


    public void terminate() {
        this.exit = true;
    }

    private void ping() {
        try {
            clientModel.ping();
        } catch (RemoteException e) {  // This should never happen, as the remote object is local
            throw new RuntimeException(e);
        }

        MessageTcp pongMessage = new MessageTcp();
        pongMessage.setCommand(MessageTcp.MessageCommand.Ping); //set command
        synchronized (serverOut) {
            serverOut.println(pongMessage);
        }
    }
    private void endgame (JSONObject content){
        final List<PlayerWithPoints> leaderboard = Jsonable.json2Leaderboard((JSONArray) content.get("leaderboard"));
        try {
            clientModel.endGame(leaderboard);
        } catch (RemoteException e) {  // This should never happen, as the remote object is local
            throw new RuntimeException(e);
        }
    }
}
