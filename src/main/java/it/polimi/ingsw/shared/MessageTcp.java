package it.polimi.ingsw.shared;
import org.json.simple.JSONObject;

import javax.management.remote.JMXServerErrorException;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class MessageTcp {
    private MessageCommand messageCommand;
    private JSONObject content;
    private String requestID;
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
        String requestID =  (String) jsonMessage.get("ID");
        this.messageCommand = command;
        this.requestID = requestID;
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
    public void setRequestID(String id){this.requestID = id; }
    public void setContent(JSONObject content) {
        this.content = content;
    }

    public void generateRequestID(){
        Random random = new SecureRandom();
        byte[] sessionIDByte = new byte[8];
        random.nextBytes(sessionIDByte);
        this.requestID = Base64.getEncoder().encodeToString(sessionIDByte);

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
        message.put("ID", requestID);
        message.put("content", this.content);
        return message.toJSONString();
    }

    public MessageCommand getCommand(){
        return messageCommand;
    }

    public String getRequestID() {return requestID; }

    public JSONObject getContent(){
        if (content != null)
            return content;
        else
            return null;

    }
}
