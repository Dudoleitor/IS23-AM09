package it.polimi.ingsw.server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalsFactoryTest {
    @Test
    void testIDCreation(){
        for(int i = 1; i <= 12; i++){
            assertEquals(i,CommonGoalsFactory.create_goal_with_ID(i,2).getID());
        }
    }
    @Test
    void testCreationWithArray(){
        ArrayList<Integer> stackState = new ArrayList<>();
        IntStream.range(1,10).forEach(x->stackState.add(x));
        AbstractCommonGoal goal = CommonGoalsFactory.create_goal_with_ID(1,stackState);
        assertEquals(1,goal.getID());
        assertEquals(9,goal.givePoints());
    }
    @Test
    void LoadFromJson() {
        JSONParser jsonParser = new JSONParser(); //initialize JSON parser
        Object obj;
        try {
            obj = jsonParser.parse(new FileReader("src/test/resources/Test_commonGoal_from_Json.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        JSONObject objGoal = (JSONObject) ((JSONObject) obj).get("CommonGoal");
        AbstractCommonGoal goal = CommonGoalsFactory.create_from_json(objGoal);
        assertEquals(6,goal.givePoints());
    }
}