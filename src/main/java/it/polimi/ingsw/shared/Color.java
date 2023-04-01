package it.polimi.ingsw.shared;

public enum Color {
        Reset("\u001B[0m"),
        Black("\u001B[30m"),
        Red("\u001B[31m"),
        Green("\u001B[32m"),
        Yellow("\u001B[33m"),
        Blue("\u001B[34m"),
        Purple("\u001B[35m"),
        Cyan("\u001B[36m"),
        White("\u001B[37m"),
        ;
        public final String ANSICode;
        Color(String color) {
                this.ANSICode = color;
        }
        public String getColorAnsiCode(){
                return ANSICode;
        }
        public static String coloredString(String str, Color color){
                return color.getColorAnsiCode() + //set desired color
                        str +
                        Reset.getColorAnsiCode(); //reset to default
        }
}
