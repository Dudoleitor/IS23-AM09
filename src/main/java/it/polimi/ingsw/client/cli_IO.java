package it.polimi.ingsw.client;

import it.polimi.ingsw.shared.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class cli_IO {
    //Default Colors
    public static Color messageColor = Color.Yellow;
    public static Color GAMEColor = Color.Purple;
    public static Color errorColor = Color.Red;
    public static Scanner scanner = new Scanner(System.in);

    //Methods to format CLI messages

    /**
     * A decorator that formats a message
     * @param s the message
     * @return the decorated message (>GAME: mes with colors)
     */
    public String messageFormat(String s){
        return ">"+ Color.coloredString("GAME",GAMEColor)+": " + Color.coloredString(s,messageColor);
    }

    public void printPlaceHolder(){
        System.out.print("$:");
    }

    /**
     * A decorator that formats an error message
     * @param s the message
     * @return the decorated message (>GAME: mes with colors)
     */
    public String errorFormat(String s){
        return ">"+ Color.coloredString("GAME",GAMEColor)+": " + Color.coloredString(s,errorColor);
    }
    public void printMessage(String s){
        System.out.println(messageFormat(s));
    }
    public void printErrorMessage(String s){
        System.out.println(errorFormat(s));
    }
    /**
     * Write some Strings on Std out without avoiding concurrent access
     * @param stringList the strings to print
     */
    public void multiPrint(List<String> stringList){
        for(String s : stringList){
            System.out.println(s);
        }
    }
    /**
     * Lock the access to StdOut when you are waiting for strings
     */
    public String scan(){
        printPlaceHolder();
        String command = scanner.nextLine();
        return command;
    }
    public String[] multiScan(int size){
        String[] result = new String[size];
        for(int i = 0; i< size; i++){
            printPlaceHolder();
            result[i] = scanner.nextLine();
        }
        return result;
    }
    public Map<String,String> multiScan(List<String> fields){
        Map<String,String> result = new HashMap();
        String value;
        for(String field : fields){
            printMessage(field+":");
            printPlaceHolder();
            value =  scanner.nextLine();
            result.put(field,value);
        }
        return result;
    }
}

