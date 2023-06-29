package it.polimi.ingsw.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputSanitizer {

    private static final List<Character> punctuation = Arrays.asList('.',',',';',')','(',' ','!','?');
    private static final List<Character> specialLetters = Arrays.asList('à','è','é','ì','ò','ù');
    private static final List<String> invalidUsernames = Arrays.asList("server", "private", "everybody");


    /**
     * Check if the sting is a valid player name
     * @param name the string to test
     * @return true if valid
     */
    public static boolean isValidName(String name){
        return  name!= null &&
                !invalidUsernames.contains(name.toLowerCase()) &&
                !name.isEmpty() &&
                !name.isBlank() &&
                name.length() <= GameSettings.maxNameLength &&
                name.chars().
                        allMatch(c ->
                                isValidSymbol((char) c) ||
                                        isInteger((char) c) ||
                                        isLowerCaseLetter((char) c) ||
                                        isUpperCaseLetter((char) c));
    }
    /**
     * Check if the sting is a valid integer
     * @param id the string to test
     * @return true if valid
     */
    public static boolean isInteger(String id){
        return  id != null &&
                !id.isBlank() &&
                !id.isEmpty() &&
                id.length() < 6 &&
                id.chars().allMatch(c ->
                        isInteger((char) c)
                );
    }
    /**
     * Check if the sting is a valid message or command
     * @param mes the string to test
     * @return true if valid
     */
    public static boolean isValidMessage(String mes){
        return  mes!= null &&
                !mes.isEmpty() &&
                !mes.isBlank() &&
                mes.length() < GameSettings.maxMessageLength &&
                mes.chars().
                        allMatch(c -> isValidSymbol((char) c) ||
                                isInteger((char) c) ||
                                isLowerCaseLetter((char) c) ||
                                isUpperCaseLetter((char) c) ||
                                isPunctuation((char) c) ||
                                isSpecialLetter((char) c));
    }
    /**
     * Check if the char is a lowercase letter
     * @param c the char to test
     * @return true if lowercase letter
     */
    public static boolean isLowerCaseLetter(Character c){
        return (c.compareTo('a') >= 0 && c.compareTo('z') <= 0);
    }

    /**
     * Check if the char is a special letter
     * @param c the char to test
     * @return true if special letter
     */
    public static boolean isSpecialLetter(Character c){
        return specialLetters.contains(c);
    }

    /**
     * Check if the char is an uppercase letter
     * @param c the char to test
     * @return true if uppercase letter
     */
    public static boolean isUpperCaseLetter(Character c){
        return c.compareTo('A') >= 0 && c.compareTo('Z') <= 0;
    }
    /**
     * Check if the char is an integer
     * @param c the char to test
     * @return true if c is an integer
     */
    public static boolean isInteger(Character c){
        return c.compareTo('0') >= 0 && c.compareTo('9') <= 0;
    }
    /**
     * Check if the char is a symbol
     * @param c the char to test
     * @return true if is _ or -
     */
    public static boolean isValidSymbol(Character c){
        return c.compareTo('_') == 0 || c.compareTo('-') == 0;
    }
    /**
     * Check if the char is a punctuation mark
     * @param c the char to test
     * @return true if punctuation mark
     */
    public static boolean isPunctuation(Character c){
        return punctuation.contains(c);
    }
}

