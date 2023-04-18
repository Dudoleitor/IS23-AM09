package it.polimi.ingsw.shared;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public static JSONObject parseString(String string){
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
    public static JSONObject map2json(Map<Integer,Integer> map) {
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
     * transform a jsonString to a map<Integer><Integer>
     * @param jsonString is the string of JsonObject
     * @return a boolean corresponding to the Object
     */
    public static Map<Integer,Integer> json2mapInt (String jsonString){
        Map<Integer,Integer> map = new HashMap<>();
        JSONObject jsonMap = parseString(jsonString);
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
    public static JSONObject boolean2json(boolean value) {
        JSONObject jsonBool = new JSONObject();
        jsonBool.put("boolean",value);
        return jsonBool;
    }
    /**
     * transform a jsonString to a boolean
     * @param jsonString is the string of JsonObject
     * @return a boolean corresponding to the Object
     */
    public static boolean json2boolean (String jsonString){
        JSONObject jsonBool = parseString(jsonString);
        boolean value = Boolean.parseBoolean(jsonBool.get("boolean").toString());
        return value;
    }
    /**
     * transform an int to a jsonString
     * @param value is the int
     * @return JsonObject that corresponds to that int
     */
    public static JSONObject int2json(int value) {
        JSONObject jsonInt = new JSONObject();
        jsonInt.put("int",value);
        return jsonInt;
    }
    /**
     * transform a jsonString to an int
     * @param jsonString is the string of JsonObject
     * @return a int corresponding to the Object
     */
    public static int json2int (String jsonString){
        JSONObject jsonInt = parseString(jsonString);
        int value = Integer.parseInt(jsonInt.get("int").toString());
        return value;
    }
    /**
     * transform a string to a jsonString
     * @param value is the string
     * @return JsonObject that corresponds to that string
     */
    public static JSONObject string2json(String value) {
        JSONObject jsonString = new JSONObject();
        jsonString.put("string",value);
        return jsonString;
    }
    /**
     * transform a jsonString to a string
     * @param jsonString is the string of JsonObject
     * @return a string corresponding to the Object
     */
    public static String json2string (String jsonString){
        JSONObject jsonS = parseString(jsonString);
        String value = jsonS.get("string").toString();
        return value;
    }

}
