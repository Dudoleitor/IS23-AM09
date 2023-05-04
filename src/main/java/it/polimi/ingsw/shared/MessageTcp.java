package it.polimi.ingsw.shared;
import org.json.simple.JSONObject;

import javax.management.remote.JMXServerErrorException;
import java.rmi.RemoteException;

public class MessageTcp {
    private MessageCommand messageCommand;
    private JSONObject content;

    public MessageTcp(){
        super();
    }

    /**
     * Creates an object that parse a String corresponding to a JsonObject and expose get methods to see the command and the content of message
     * @param message is the message to parse
     */
    public MessageTcp(String message){
        JSONObject jsonMessage = Jsonable.parseString(message);
        MessageCommand command = MessageCommand.valueOfLabel(jsonMessage.get("command").toString());
        JSONObject content = (JSONObject) jsonMessage.get("content");
        this.messageCommand = command;
        if(content != null) {
            this.content = Jsonable.parseString(content.toString());
        }
    }
    public enum MessageCommand { //this is a public enumeration of all possible commands sent over TCP
        Login("login","ReplyWait"),
        GetJoinedLobbies("getJoined", "ReplyWait"),
        JoinRandomLobby("joinRandom", "ReplyWait"),
        CreateLobby("createLobby", "ReplyWait"),
        GetAvailableLobbies("availableLobbies", "ReplyWait"),
        JoinSelectedLobby("joinSelected", "ReplyWait"),
        PostToLiveChat("postChat", "ReplyWait"),
        PostSecretToLiveChat("postSecret", "ReplyWait"),
        Quit("quit", "ReplyWait"),
        MatchHasStarted("matchStarted", "ReplyWait"),
        PostMove("move", "ReplyWait"),
        StartGame("start", "ReplyWait"),
        IsLobbyAdmin("isAdmin", "ReplyWait"),
        PickedFromBoard("pickedTile", "Update"),
        RefreshBoard("refreshBoard", "Update"),
        PutIntoShelf("refreshShelf","Update"),

        ;

        private final String label;
        private final String type;
        MessageCommand(String tag, String type){
            this.label = tag;
            this.type = type;
        }
        public static MessageCommand valueOfLabel(String label) {
            for (MessageCommand e : values()) {
                if (e.label.equals(label)) {
                    return e;
                }
            }
            throw new RuntimeException("command not found");
        }
        public String typeOfCommand(){
            return type;
        }

        @Override
        public String toString() {return label;}
    }


    public void setCommand(MessageCommand messageCommand){
        this.messageCommand = messageCommand;
    }
    public void setContent(JSONObject content) {
        this.content = content;
    }
    public boolean isReplyMessage(){
        boolean result;
        if(messageCommand == null){
            result = false;
        } else {
            result = messageCommand.typeOfCommand().equals("ReplyWait");

        }
        return result;
    }
    public boolean isUpdateMessage(){
        boolean result;
        if(messageCommand == null){
            result = false;
        } else {
            result = messageCommand.typeOfCommand().equals("Update");

        }
        return result;

    }

    @Override
    public String toString() {
        JSONObject message = new JSONObject();
        message.put("command", this.messageCommand.toString());
        message.put("content", this.content);
        return message.toJSONString();
    }

    public MessageCommand getCommand(){
        return messageCommand;
    }

    public JSONObject getContent(){
        if (content != null)
            return content;
        else
            return null;

    }
}
