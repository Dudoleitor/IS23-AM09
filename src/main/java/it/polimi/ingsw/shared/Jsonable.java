package it.polimi.ingsw.shared;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public interface Jsonable {
    public abstract JSONObject toJson();
    /**
     * This static method returns the JSONObject from a json file,
     * it uses the "board" attribute.
     * @param jsonPath path to the json file
     * @return JSONObject with the content
     */
    public static JSONObject pathToJsonObject(String jsonPath, Class<?> cls) throws JsonBadParsingException{
        try {
            JSONParser jsonParser = new JSONParser(); //initialize parser
            Object obj = jsonParser.parse(new FileReader(jsonPath)); //acquire JSON object file
            return (JSONObject) ((JSONObject) obj).get(parseClassName(cls)); //acquire board object
        } catch (IOException e){
            throw new JsonBadParsingException("Error while generating " + parseClassName1UC(cls) + " from JSON : file was not found");
        } catch (ParseException e) {
            throw new JsonBadParsingException("Error while generating " + parseClassName1UC(cls) + " from JSON : bad json parsing");
        }
    }
    private static String parseClassName(Class<?> cls){
        String[] subStrings = cls.toString().split("\\.");
        return subStrings[subStrings.length-1].toLowerCase();
    }
    private static String parseClassName1UC(Class<?> cls){
        String str  =parseClassName(cls);
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
