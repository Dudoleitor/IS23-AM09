package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.InputSanitizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputSanitizerTest {
    @Test
    void punctuation(){
        assertTrue(InputSanitizer.isPunctuation('.'));
        assertTrue(InputSanitizer.isPunctuation(','));
        assertTrue(InputSanitizer.isPunctuation(';'));
        assertTrue(InputSanitizer.isPunctuation(')'));
        assertTrue(InputSanitizer.isPunctuation('('));
        assertTrue(InputSanitizer.isPunctuation(' '));
        assertTrue(InputSanitizer.isPunctuation('!'));
        assertTrue(InputSanitizer.isPunctuation('?'));
        assertFalse(InputSanitizer.isPunctuation('a'));
        assertFalse(InputSanitizer.isPunctuation('A'));
        assertFalse(InputSanitizer.isPunctuation('-'));
    }
    @Test
    void symbol(){
        assertTrue(InputSanitizer.isValidSymbol('-'));
        assertTrue(InputSanitizer.isValidSymbol('_'));
        assertFalse(InputSanitizer.isValidSymbol('a'));
        assertFalse(InputSanitizer.isValidSymbol('A'));
        assertFalse(InputSanitizer.isValidSymbol(','));
    }
    @Test
    void letterUC(){
        assertTrue(InputSanitizer.isUpperCaseLetter('A'));
        assertTrue(InputSanitizer.isUpperCaseLetter('G'));
        assertTrue(InputSanitizer.isUpperCaseLetter('Z'));
        assertFalse(InputSanitizer.isUpperCaseLetter('a'));
        assertFalse(InputSanitizer.isUpperCaseLetter('z'));
        assertFalse(InputSanitizer.isUpperCaseLetter('-'));
        assertFalse(InputSanitizer.isUpperCaseLetter(' '));
    }
    @Test
    void letterLC(){
        assertTrue(InputSanitizer.isLowerCaseLetter('a'));
        assertTrue(InputSanitizer.isLowerCaseLetter('h'));
        assertTrue(InputSanitizer.isLowerCaseLetter('z'));
        assertFalse(InputSanitizer.isLowerCaseLetter('A'));
        assertFalse(InputSanitizer.isLowerCaseLetter('Z'));
        assertFalse(InputSanitizer.isLowerCaseLetter('-'));
        assertFalse(InputSanitizer.isLowerCaseLetter(' '));
    }
    @Test
    void integer(){
        assertTrue(InputSanitizer.isInteger('0'));
        assertTrue(InputSanitizer.isInteger('9'));
        assertTrue(InputSanitizer.isInteger("00"));
        assertTrue(InputSanitizer.isInteger("0123"));
        assertFalse(InputSanitizer.isInteger('a'));
        assertFalse(InputSanitizer.isInteger('!'));
        assertFalse(InputSanitizer.isInteger("123 456"));
    }
    @Test
    void name(){
        assertTrue(InputSanitizer.isValidName("Prova"));
        assertTrue(InputSanitizer.isValidName("Prova123"));
        assertTrue(InputSanitizer.isValidName("Pr-0v_a"));
        assertFalse(InputSanitizer.isValidName("Prova "));
        assertFalse(InputSanitizer.isValidName("Prova!"));
        assertFalse(InputSanitizer.isValidName("provà"));
    }
    @Test
    void message(){
        assertTrue(InputSanitizer.isValidMessage("Prova! 123,;.?"));
        assertFalse(InputSanitizer.isValidMessage("ç"));
        assertTrue(InputSanitizer.isValidMessage("ciao ù??"));
    }
}