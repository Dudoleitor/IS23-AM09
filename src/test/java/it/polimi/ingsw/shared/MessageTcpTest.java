package it.polimi.ingsw.shared;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTcpTest {
    @Test
    public void availableLobbyTest() throws ParseException {
        Map<Integer,Integer> map = new HashMap<>();
        map.put(1,1);
        map.put(2,2);
        map.put(3,3);
        map.put(4,5);
        MessageTcp message = new MessageTcp();
        message.setCommand(MessageTcp.MessageCommand.AvailableLobbies);
        message.setContent(map);
        MessageTcp jsonMessage = new MessageTcp(message.toString());
        assertEquals(map,message.getContent());
        assertEquals(map,jsonMessage.getContent());


    }
}
