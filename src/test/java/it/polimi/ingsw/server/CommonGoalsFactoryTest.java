package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.JsonBadParsingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalsFactoryTest {
    @Test
    void testIDCreation() throws JsonBadParsingException {
        for(int i = 1; i <= 12; i++){
            assertEquals(i,CommonGoalsFactory.create_goal_with_ID(i,2).getID());
        }
    }
    @Test
    void LoadFromJson()  {
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
        CommonGoal goal = CommonGoalsFactory.create_from_json(objGoal);
        assertEquals(6,goal.givePoints());
    }
    @Test
    void createWithNumberOfPlayers() throws JsonBadParsingException {
        CommonGoal test = CommonGoalsFactory.create_goal_with_ID(2,2);
        assertEquals(8,test.givePoints());
        assertEquals(4,test.givePoints());
        assertEquals(0,test.givePoints());

        test = CommonGoalsFactory.create_goal_with_ID(2,3);
        assertEquals(8,test.givePoints());
        assertEquals(6,test.givePoints());
        assertEquals(4,test.givePoints());
        assertEquals(0,test.givePoints());

        test = CommonGoalsFactory.create_goal_with_ID(2,4);
        assertEquals(8,test.givePoints());
        assertEquals(6,test.givePoints());
        assertEquals(4,test.givePoints());
        assertEquals(2,test.givePoints());
        assertEquals(0,test.givePoints());
    }
    @Test
    void invalidNumberOfPlayersException(){
        assertThrows(CommonGoalRuntimeException.class, () -> CommonGoalsFactory.create_goal_with_ID(2,0));
        assertThrows(CommonGoalRuntimeException.class, () -> CommonGoalsFactory.create_goal_with_ID(2,5));
    }

    @Test
    void exceptionInRandomNumbers(){
        assertThrows(CommonGoalRuntimeException.class, () -> CommonGoalsFactory.pickTwoRandomNumbers(-1));
    }
}