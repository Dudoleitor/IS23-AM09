package it.polimi.ingsw.RMI;

import java.util.ArrayList;
import java.util.List;

public class InputSanitizer {
    public boolean isValidName(String name){
        return  name!= null &&
                name.chars().count() >= 1 &&
                name.chars().
                        allMatch(c ->
                                isValidSymbol((char) c) ||
                                        isInteger((char) c) ||
                                        isLowerCaseLetter((char) c) ||
                                        isUpperCaseLetter((char) c));
    }
    public boolean isInteger(String id){
        return id != null &&
                id.chars().count() >= 1 &&
                id.chars().allMatch(c ->
                        isInteger((char) c)
                );
    }
    public boolean isValidMessage(String mes){
        return  mes!= null &&
                mes.chars().count() >= 1 &&
                mes.chars().
                        allMatch(c -> isValidSymbol((char) c) ||
                                isInteger((char) c) ||
                                isLowerCaseLetter((char) c) ||
                                isUpperCaseLetter((char) c) ||
                                isPunctuation((char) c));
    }
    public boolean isLowerCaseLetter(Character c){
        return c.compareTo('a') >= 0 && c.compareTo('z') <= 0;
    }
    public boolean isUpperCaseLetter(Character c){
        return c.compareTo('A') >= 0 && c.compareTo('Z') <= 0;
    }
    public boolean isInteger(Character c){
        return c.compareTo('0') >= 0 && c.compareTo('9') <= 0;
    }
    public boolean isValidSymbol(Character c){
        return c.compareTo('_') == 0 || c.compareTo('-') == 0;
    }
    public boolean isPunctuation(Character c){
        List<Character> punctuation = new ArrayList<>();
        punctuation.add('.');
        punctuation.add(',');
        punctuation.add(';');
        punctuation.add(')');
        punctuation.add('(');
        punctuation.add(' ');
        punctuation.add('!');
        punctuation.add('?');
        return punctuation.stream().anyMatch(p -> c.compareTo(p) == 0);
    }
}

