package it.polimi.ingsw.shared;

import it.polimi.ingsw.shared.model.PlayerGoal;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        JSONObject jsonMap = new JSONObject();
        jsonMap.putAll(map);

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
        for(Object k : jsonMap.keySet()){
            map.put(Integer.parseInt(k.toString()),
                    Integer.parseInt(jsonMap.get(k).toString()));

        }
        return map;
    }


    /**
     * transform a list to a jsonString
     * @param list is the boolean
     * @return JsonObject that corresponds to that boolean
     */
    static JSONArray list2json(List<?> list) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(list);

        return jsonArray;
    }
    /**
     * transform a jsonString to a List<Integer>
     * @param jsonList is the string of JsonObject
     * @return a boolean corresponding to the Object
     */
    static List<Integer> json2listInt (JSONArray jsonList){
        if (jsonList == null)
            return null;
        List<Integer> list = new ArrayList<>();
        for(Object l : jsonList){
            list.add(Integer.parseInt(l.toString()));
        }
        return list;
    }
}
