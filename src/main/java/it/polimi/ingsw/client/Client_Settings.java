package it.polimi.ingsw.client;

import it.polimi.ingsw.shared.Color;

public class Client_Settings {
    /**
     * Connection Type
     */
    public static Connection connection = Connection.RMI;
    /**
     * View Type
     */
    public static UI ui;

    public void setUI(Client_Settings.UI ui){
        this.ui = ui;
    }

    public void setConnection(Client_Settings.Connection connection){
        this.connection = connection;
    }

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
        RMI("rmi"),
        STUB("test");
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
        GUI("gui"),
        DRIVER("test");
        String tag;
        UI(String tag){
            this.tag = tag;
        }
        String getTag(){
            return tag;
        }
    }
}
