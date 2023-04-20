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
        JSONObject jsonMessage = Jsonable.parseString(message);
        MessageCommand command = MessageCommand.valueOfLabel(jsonMessage.get("command").toString());
        JSONObject content = (JSONObject) jsonMessage.get("content");
        this.messageCommand = command;
        if(content != null) {
            this.content = Jsonable.parseString(content.toString());
        }
    }
    public enum MessageCommand { //this is a public enumeration of all possible commands sent over TCP
        Login("login"),
        GetJoinedLobbies("getJoined"),
        JoinRandomLobby("joinRandom"),
        CreateLobby("createLobby"),
        GetAvailableLobbies("availableLobbies"),
        JoinSelectedLobby("joinSelected"),
        PostToLiveChat("postChat"),
        PostSecretToLiveChat("postSecret"),
        Quit("quit"),
        MatchHasStarted("matchStarted"),
        PostMove("move"),


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

    public JSONObject getContent(){
        if (content != null)
            return content;
        else
            return null;

    }
}
