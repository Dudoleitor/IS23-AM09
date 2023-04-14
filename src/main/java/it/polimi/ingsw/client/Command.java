package it.polimi.ingsw.client;

import java.util.Arrays;

public enum Command {
        Exit("exit"),
        Print("print"),
        Refresh(""),
        Secret("secret"),
        Message("message"),
        Start("start"),
        Move("move"),
        Peek("peek"),
        Invalid("Invalid");
        private String code;
        Command(String code){
            this.code = code;
        }
        public static Command stringToCommand(String input){
            //return the command that matches the user input.
            //If no match return invalid
            return  Arrays.stream(Command.values()).
                    filter(com -> com.getCode()
                            .equals(input.toLowerCase())).
                    findFirst().
                    orElse(Command.Invalid);
        }
        public String getCode(){
                return code;
        }
}
