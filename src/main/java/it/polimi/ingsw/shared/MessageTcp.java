package it.polimi.ingsw.shared;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

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
        JSONParser parser = new JSONParser();
        JSONObject jsonMessage;
        try {
            jsonMessage = (JSONObject) parser.parse(message);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.messageCommand = MessageCommand.valueOfLabel(jsonMessage.get("command").toString());
        this.content = (JSONObject) jsonMessage.get("content");
    }
    public enum MessageCommand { //this is a public enumeration of all possible commands sent over TCP
        Login("login"),
        GetJoinedLobbies("getJoined"),
        JoinRandomLobby("joinRandom"),
        CreateLobby("createLobby"),
        GetAvailableLobbies("availableLobbies"),
        JoinSelectedLobby("joinSelected"),

        ;

        private final String label;
        MessageCommand(String tag){this.label = tag;}
        public static MessageCommand valueOfLabel(String label) {
            for (MessageCommand e : values()) {
                if (e.label.equals(label)) {
                    return e;
                }
            }
            throw new RuntimeException("command not found");
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

    public String getContent(){
        if (content != null)
            return content.toJSONString();
        else
            return null;

    }
}
