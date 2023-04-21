package it.polimi.ingsw.client.View.cli;

import it.polimi.ingsw.client.Client_Settings;
import it.polimi.ingsw.shared.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static it.polimi.ingsw.client.Client_Settings.*;

public class cli_IO {

    private Scanner scanner;
    private static Lock cli_Lock = null;

    private String placeHolder = stdPlaceHolder;

    public cli_IO(){
        scanner = new Scanner(System.in);
        if(cli_Lock == null){
            cli_Lock = new ReentrantLock();
        }
    }

    //String decorators

    /**
     * A decorator that formats a message
     * @param s the message
     * @return the decorated message (>GAME: mes with colors)
     */
    public String messageFormat(String s){
        return ">"+ Color.coloredString("GAME",GAMEColor)+": " + Color.coloredString(s,messageColor);
    }

    /**
     * Prints the placeholder
     */
    public void printPlaceHolder(){
        System.out.print(placeHolder);
    }

    public void setPlaceHolder(String placeHolder){
        this.placeHolder = placeHolder;
    }

    /**
     * A decorator that formats an error message
     * @param s the message
     * @return the decorated message (>GAME: mes with colors)
     */
    public String errorFormat(String s){
        return ">"+ Color.coloredString("GAME",GAMEColor)+": " + Color.coloredString(s,errorColor);
    }

    //Print methods

    /**
     * Prints the string (decorated as a message) in a synchronous way
     * @param s the message string
     */
    public void printMessage(String s){
        synchronized (cli_Lock) {
            System.out.println(messageFormat(s));
        }
    }

    /**
     * Prints the string (decorated as an error message) in a synchronous way
     * @param s the message string
     */
    public void printErrorMessage(String s){
        synchronized (cli_Lock) {
            System.out.println(errorFormat(s));
        }
    }
    /**
     * Write some Strings on Std out (no decorator) in a synchronous way
     * @param stringList the strings to print
     */
    public void multiPrint(List<String> stringList){
        synchronized (cli_Lock){
            for(String s : stringList){
                System.out.println(s);
            }
        }
    }

    //Scan methods

    /**
     * Locks the IO and waits for input to scan
     * @return the user input
     */
    public String scanSync(){
        String command = "";
        synchronized (cli_Lock){
            printPlaceHolder();
            command = scanner.nextLine();
        }
        return command;
    }
    /**
     * Scans the input without locking IO
     * @return the user input
     */
    public String scan(){
        printPlaceHolder();
        String command = scanner.nextLine();
        return command;
    }

    /**
     * Scans n Strings while locking IO
     * @param size
     * @return the scanned Stings
     */
    public String[] multiScan(int size){
        String[] result = new String[size];
        synchronized (cli_Lock){
            for(int i = 0; i< size; i++){
                printPlaceHolder();
                result[i] = scanner.nextLine();
            }
        }
        return result;
    }
    /**
     * Lock the IO and scans for all the fields
     * @param fields
     * @return a map that associates each field with the scanned String
     */
    public Map<String,String> multiScan(List<String> fields){
        Map<String,String> result = new HashMap();
        synchronized (cli_Lock){
            String value;
            for(String field : fields){
                printMessage(field+":");
                printPlaceHolder();
                value =  scanner.nextLine();
                result.put(field,value);
            }
        }
        return result;
    }
}

