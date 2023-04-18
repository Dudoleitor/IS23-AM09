package it.polimi.ingsw.shared;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

public class MessageTcp {
    private MessageCommand messageCommand;
    private Object content;

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
        this.content = getObject((JSONObject) jsonMessage.get("content"));
    }
    public enum MessageCommand { //this is a public enumeration of all possible commands sent over TCP
        SetUsername("Username"),
        AvailableLobbies("AvailableLobbies")
        ;

        private String label;
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
    //here starts commands to format the object
    public void setCommand(MessageCommand messageCommand){
        this.messageCommand = messageCommand;
    }
    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        JSONObject message = new JSONObject();
        message.put("command", this.messageCommand.toString());
        message.put("content", this.content);
        return message.toJSONString();
    }

    public Object getContent(){
        return content;


    }





    //here start static commands used to support obj creation
    private Object getObject(JSONObject obj){
        switch (messageCommand) {
            case AvailableLobbies:
                return json2Map(obj);


            default:
                throw new RuntimeException("Illegal content for command");
        }

    }

    private JSONObject createPayload(){
        switch (messageCommand) {
            case AvailableLobbies:
                return map2json((Map<Integer, Integer>) content);


            default:
                throw new RuntimeException("Illegal content for command");
        }
    }

    /**
     *
     * @param map of <Integer,Integer>
     * @return JsonObject that corresponds to that map
     */
    private JSONObject map2json(Map<Integer,Integer> map) {
        JSONObject jsonMap = new JSONObject();
        map.keySet()
                .stream()
                .forEach(x ->
                        jsonMap.put(
                                x,
                                map.get(x)
                        )
                );
        return jsonMap;
    }

    /**
     *
     * @param jsonMap is the JsonObject of the data structure
     * @return a map of availableLobbies
     */
    private Map<Integer,Integer> json2Map (JSONObject jsonMap){
        Map<Integer,Integer> map = new HashMap<>();
        jsonMap.keySet()
                .stream()
                .forEach(x ->
                        map.put(
                                Integer.parseInt(x.toString()),
                                Integer.parseInt(jsonMap.get(x).toString())
                        )
                );
        return map;
    }
}
