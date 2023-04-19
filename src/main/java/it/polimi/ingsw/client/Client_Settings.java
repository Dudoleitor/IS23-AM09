package it.polimi.ingsw.client;

import it.polimi.ingsw.shared.Color;

public class Client_Settings {
    /**
     * Connection Type
     */
    public static final Connection connection = Connection.TCP;
    /**
     * View Type
     */

    //Default color theme for CLI
    public static final Color messageColor = Color.Yellow;
    public static final Color GAMEColor = Color.Purple;
    public static final Color errorColor = Color.Red;
    public static final String gameLogo = Color.coloredString(
                    "\n\n"+
                            "███    ███ ██    ██     ███████ ██   ██ ███████ ██      ███████ ██ ███████ \n" +
                            "████  ████  ██  ██      ██      ██   ██ ██      ██      ██      ██ ██      \n" +
                            "██ ████ ██   ████       ███████ ███████ █████   ██      █████   ██ █████   \n" +
                            "██  ██  ██    ██             ██ ██   ██ ██      ██      ██      ██ ██      \n" +
                            "██      ██    ██        ███████ ██   ██ ███████ ███████ ██      ██ ███████ \n\n",
                    Client_Settings.messageColor);

    public enum Connection{
        TCP("tcp"),
        RMI("rmi");
        String tag;
        Connection(String tag){
            this.tag = tag;
        }
        String getTag(){
            return tag;
        }
    }

    public enum UI{
        CLI("cli"),
        GUI("gui");
        String tag;
        UI(String tag){
            this.tag = tag;
        }
        String getTag(){
            return tag;
        }
    }
}
