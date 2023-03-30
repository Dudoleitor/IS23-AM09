package it.polimi.ingsw.shared;

import it.polimi.ingsw.server.CommonGoalRuntimeException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public abstract class JsonableFactory {
    public static JSONObject pathToJsonObject(String jsonPath, Class<?> cls) throws JsonBadParsingException {
        Object obj = null;
        try {
            JSONParser jsonParser = new JSONParser(); //initialize parser
            obj = jsonParser.parse(new FileReader(jsonPath)); //acquire JSON object file
            JSONObject jsonObject = (JSONObject) ((JSONObject) obj).get(cls.toString().toLowerCase()); //get CommonGoal
            return jsonObject;
        } catch (IOException | NullPointerException e) {
            throw new CommonGoalRuntimeException("Error while creating CommonGoal : bad json parsing");
        } catch (ParseException | ClassCastException e ) {
            throw new JsonBadParsingException("Error while creating CommonGoal : bad JsonParsing");
        }
    }
    public abstract Jsonable create_from_json(JSONObject jsonObject);
    public abstract Jsonable create_with_player_number(int number_of_players);
}
