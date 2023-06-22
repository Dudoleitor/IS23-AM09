package it.polimi.ingsw.shared;

import it.polimi.ingsw.shared.model.Tile;
import it.polimi.ingsw.shared.model.TileGenericException;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum Color {
        Reset("\u001B[0m", "reset"),
        Black("\u001B[30m", "black"),
        Red("\u001B[31m", "red"),
        Green("\u001B[32m", "green"),
        Yellow("\u001B[33m", "yellow"),
        Blue("\u001B[34m", "blue"),
        Purple("\u001B[35m", "purple"),
        Cyan("\u001B[36m", "cyan"),
        White("\u001B[37m", "white"),
        ;
        public final String ANSICode;
        public final String color;

        Color(String ANSICode, String color) {
                this.ANSICode = ANSICode;
                this.color = color;
        }
        public String getColorAnsiCode(){
                return ANSICode;
        }
        public static String coloredString(String str, Color color){
                return color.getColorAnsiCode() + //set desired color
                        str +
                        Reset.getColorAnsiCode(); //reset to default
        }
        public static Color getRandomColor(){
                List<Color> validColors = List.of(Color.values())
                        .stream()
                        .filter(color -> color != Reset && color != White && color != Black)
                        .collect(Collectors.toList());
                Random rand = new Random();
                return validColors.get(rand.nextInt(validColors.size()));
        }
        @Override
        public String toString(){
                return this.color;
        }

        /**
         * @param label is the label to look for
         * @return the Color corresponding to the label otherwise reset
         */
        public static Color valueOfString(String label) { //gets a label and returns a Tile
                for (Color e : values()) {
                        if (e.toString().equals(label)) {
                                return e;
                        }
                }
                return Color.Reset;
        }

}
