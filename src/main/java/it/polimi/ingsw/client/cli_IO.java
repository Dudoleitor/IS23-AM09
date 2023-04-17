package it.polimi.ingsw.client;

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

    //Print methods
    public void printMessage(String s){
        synchronized (cli_Lock) {
            System.out.println(messageFormat(s));
        }
    }
    public void printErrorMessage(String s){
        synchronized (cli_Lock) {
            System.out.println(errorFormat(s));
        }
    }
    /**
     * Write some Strings on Std out without avoiding concurrent access
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
    public String scanSync(){
        String command = "";
        synchronized (cli_Lock){
            printPlaceHolder();
            command = scanner.nextLine();
        }
        return command;
    }
    public String scan(){
        printPlaceHolder();
        String command = scanner.nextLine();
        return command;
    }
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

