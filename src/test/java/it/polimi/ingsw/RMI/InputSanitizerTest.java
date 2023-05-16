package it.polimi.ingsw.RMI;

import it.polimi.ingsw.client.controller.cli.InputSanitizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputSanitizerTest {
    static InputSanitizer inputSanitizer = new InputSanitizer();
    @Test
    void punctuation(){
        assertTrue(inputSanitizer.isPunctuation('.'));
        assertTrue(inputSanitizer.isPunctuation(','));
        assertTrue(inputSanitizer.isPunctuation(';'));
        assertTrue(inputSanitizer.isPunctuation(')'));
        assertTrue(inputSanitizer.isPunctuation('('));
        assertTrue(inputSanitizer.isPunctuation(' '));
        assertTrue(inputSanitizer.isPunctuation('!'));
        assertTrue(inputSanitizer.isPunctuation('?'));
        assertFalse(inputSanitizer.isPunctuation('a'));
        assertFalse(inputSanitizer.isPunctuation('A'));
        assertFalse(inputSanitizer.isPunctuation('-'));
    }
    @Test
    void symbol(){
        assertTrue(inputSanitizer.isValidSymbol('-'));
        assertTrue(inputSanitizer.isValidSymbol('_'));
        assertFalse(inputSanitizer.isValidSymbol('a'));
        assertFalse(inputSanitizer.isValidSymbol('A'));
        assertFalse(inputSanitizer.isValidSymbol(','));
    }
    @Test
    void letterUC(){
        assertTrue(inputSanitizer.isUpperCaseLetter('A'));
        assertTrue(inputSanitizer.isUpperCaseLetter('G'));
        assertTrue(inputSanitizer.isUpperCaseLetter('Z'));
        assertFalse(inputSanitizer.isUpperCaseLetter('a'));
        assertFalse(inputSanitizer.isUpperCaseLetter('z'));
        assertFalse(inputSanitizer.isUpperCaseLetter('-'));
        assertFalse(inputSanitizer.isUpperCaseLetter(' '));
    }
    @Test
    void letterLC(){
        assertTrue(inputSanitizer.isLowerCaseLetter('a'));
        assertTrue(inputSanitizer.isLowerCaseLetter('h'));
        assertTrue(inputSanitizer.isLowerCaseLetter('z'));
        assertFalse(inputSanitizer.isLowerCaseLetter('A'));
        assertFalse(inputSanitizer.isLowerCaseLetter('Z'));
        assertFalse(inputSanitizer.isLowerCaseLetter('-'));
        assertFalse(inputSanitizer.isLowerCaseLetter(' '));
    }
    @Test
    void integer(){
        assertTrue(inputSanitizer.isInteger('0'));
        assertTrue(inputSanitizer.isInteger('9'));
        assertTrue(inputSanitizer.isInteger("00"));
        assertTrue(inputSanitizer.isInteger("0123"));
        assertFalse(inputSanitizer.isInteger('a'));
        assertFalse(inputSanitizer.isInteger('!'));
        assertFalse(inputSanitizer.isInteger("123 456"));
    }
    @Test
    void name(){
        assertTrue(inputSanitizer.isValidName("Prova"));
        assertTrue(inputSanitizer.isValidName("Prova123"));
        assertTrue(inputSanitizer.isValidName("Pr-0v_a"));
        assertFalse(inputSanitizer.isValidName("Prova "));
        assertFalse(inputSanitizer.isValidName("Prova!"));
    }
    @Test
    void message(){
        assertTrue(inputSanitizer.isValidMessage("Prova! 123,;.?"));
        assertFalse(inputSanitizer.isValidMessage("ç"));
        assertFalse(inputSanitizer.isValidMessage("ù"));
    }
}