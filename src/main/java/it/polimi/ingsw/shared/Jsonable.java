package it.polimi.ingsw.shared;

import it.polimi.ingsw.shared.model.PlayerGoal;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public interface Jsonable {
    JSONObject toJson();
    /**
     * This static method returns the JSONObject from a json file,
     * it uses the "board" attribute.
     * @param jsonPath path to the json file
     * @return JSONObject with the content
     */
    static JSONObject pathToJsonObject(String jsonPath, Class<?> cls) throws JsonBadParsingException{
        JSONParser jsonParser = new JSONParser(); //initialize parser
        try(
                InputStream stream = Jsonable.class.getClassLoader().getResourceAsStream(jsonPath)
                ) {
            if(stream==null)
                throw new JsonBadParsingException("Error while generating " + parseClassName1UC(cls) + " from JSON : file was not found");
            Reader reader = new InputStreamReader(stream);
            Object obj = jsonParser.parse(reader); //acquire JSON object file
            return (JSONObject) ((JSONObject) obj).get(parseClassName(cls)); //acquire board object
        } catch (IOException | NullPointerException e){
            throw new JsonBadParsingException("Error while generating " + parseClassName1UC(cls) + " from JSON : file was not found");
        } catch (ParseException e) {
            throw new JsonBadParsingException("Error while generating " + parseClassName1UC(cls) + " from JSON : bad json parsing");
        }
    }
    private static String parseClassName(Class<?> cls) {
        try {
            String[] subStrings = cls.toString().split("\\.");
            return subStrings[subStrings.length - 1].toLowerCase();
        } catch (NullPointerException e){
            return null;
        }
    }
    private static String parseClassName1UC(Class<?> cls){
        String str  =parseClassName(cls);
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Transform a String representing a JsonObject to the object
     * @param string is the input to parse
     * @return JsonObject corresponding to the String format
     */
    static JSONObject parseString(String string){
        if (string == null)
            return null;
        JSONParser parser = new JSONParser();
        JSONObject jsonObj;
        try {
            jsonObj = (JSONObject) parser.parse(string);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return jsonObj;
    }
    /**
     * transform a map to a jsonString
     * @param map is the boolean
     * @return JsonObject that corresponds to that boolean
     */
    static JSONObject map2json(Map<?,?> map) {
        if (map == null)
            return null;
        JSONObject jsonMap = new JSONObject();
        map.keySet()
                .stream()
                .forEach(x ->
                        jsonMap.put(
                                x.toString(),
                                map.get(x).toString()
                        )
                );
        return jsonMap;
    }
    /**
     * transform a jsonString to a map<Integer><Integer>
     * @param jsonMap is the string of JsonObject
     * @return a boolean corresponding to the Object
     */
    static Map<Integer,Integer> json2mapInt (JSONObject jsonMap){
        if (jsonMap == null)
            return null;
        Map<Integer,Integer> map = new HashMap<>();
        if(jsonMap == null)
            return null;
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

    /**
     * transform a boolean to a jsonString
     * @param value is the boolean
     * @return JsonObject that corresponds to that boolean
     */
    static JSONObject boolean2json(boolean value) {
        JSONObject jsonBool = new JSONObject();
        jsonBool.put("boolean",value);
        return jsonBool;
    }
    /**
     * transform a jsonString to a boolean
     * @param jsonBool is the string of JsonObject
     * @return a boolean corresponding to the Object
     */
    static boolean json2boolean (JSONObject jsonBool){
        return Boolean.parseBoolean(jsonBool.get("boolean").toString());
    }
    /**
     * transform an int to a jsonString
     * @param value is the int
     * @return JsonObject that corresponds to that int
     */
    static JSONObject int2json(int value) {
        JSONObject jsonInt = new JSONObject();
        jsonInt.put("int",value);
        return jsonInt;
    }
    /**
     * transform a jsonString to an int
     * @param jsonInt is the string of JsonObject
     * @return an int corresponding to the Object
     */
    static int json2int (JSONObject jsonInt){;
        return Integer.parseInt(jsonInt.get("int").toString());
    }
    /**
     * transform a string to a jsonString
     * @param value is the string
     * @return JsonObject that corresponds to that string
     */
    static JSONObject string2json(String value) {
        JSONObject jsonString = new JSONObject();
        jsonString.put("string",value);
        return jsonString;
    }
    /**
     * transform a jsonString to a string
     * @param jsonS is the string of JsonObject
     * @return a string corresponding to the Object
     */
    static String json2string (JSONObject jsonS){
        return jsonS.get("string").toString();
    }

}
