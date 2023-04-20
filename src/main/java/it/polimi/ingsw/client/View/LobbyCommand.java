package it.polimi.ingsw.client.View;

import java.util.Arrays;

public enum LobbyCommand {
        Exit("exit", "-q","Quit the game"),
        Print("print","-p","Print all messages in chat"),
        Secret("secret","-sm","Send a private message to a user"),
        Message("message","-msg","Send a message to all members of lobby"),
        Start("start","-s","Start the game"),
        Move("move","-m","Make your move"),
        Peek("peek","-p","See an element of the board"),
        Help("help","-h",""),
        Invalid("Invalid","","");
        private String code;
        private String description;
        private String shortcut;
        LobbyCommand(String code, String shortcut, String description){
            this.code = code;
            this.shortcut = shortcut;
            this.description = description;
        }
        public static LobbyCommand stringToCommand(String input){
            //return the command that matches the user input.
            //If no match return invalid
            return  Arrays.stream(LobbyCommand.values()).
                    filter(com ->
                            com.getCode().equals(input.toLowerCase()) ||
                            com.getShortcut().equals(input)).
                    findFirst().
                    orElse(LobbyCommand.Invalid);
        }
        public String getCode(){
                return code;
        }
        public String getDescription(){return description;}
        public String getShortcut(){return shortcut;}
}
