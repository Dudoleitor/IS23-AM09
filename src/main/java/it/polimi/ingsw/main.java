package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.Client_Settings;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.shared.IpAddressV4;
import it.polimi.ingsw.shared.NetworkSettings;
import org.apache.commons.cli.*;

import static it.polimi.ingsw.client.Client_Settings.Connection.RMI;
import static it.polimi.ingsw.client.Client_Settings.Connection.TCP;
import static it.polimi.ingsw.client.Client_Settings.UI.CLI;
import static it.polimi.ingsw.client.Client_Settings.UI.GUI;


public class main {
    static private Options options;
    static private CommandLine commandLine;
    static private void initOptions(){
        options = new Options();
        options.addOption(Option.builder()
                .longOpt("server")
                .desc("If set, the server is started")
                .build());
        options.addOption(Option.builder()
                .longOpt("client")
                .desc("If set, the client is started")
                .build());
        options.addOption(Option.builder()
                .longOpt("cli")
                .desc("If set, the client will be using the CLI")
                .build());
        options.addOption(Option.builder()
                .longOpt("gui")
                .desc("If set, the client will be using the GUI")
                .build());
        options.addOption(Option.builder()
                .longOpt("rmi")
                .hasArg(true)
                .desc("If set, the client will connect via RMI")
                .build());
        options.addOption(Option.builder()
                .longOpt("tcp")
                .hasArg(true)
                .desc("If set, the client will connect via TCP")
                .build());
        options.addOption(Option.builder()
                .longOpt("ip")
                .hasArg(true)
                .desc("If set, the client will connect via RMI")
                .build());
    }


    private static boolean isServer(){
        return commandLine.hasOption("server");
    }

    private static boolean isClient(){
        return commandLine.hasOption("client");
    }

    private static boolean isCLI(){
        return commandLine.hasOption(CLI.name().toLowerCase());
    }

    private static boolean isGUI(){
        return commandLine.hasOption(GUI.name().toLowerCase());
    }

    private static boolean hasRMIPort(){
        return commandLine.hasOption(RMI.name().toLowerCase());
    }

    private static boolean hasTCPPort(){
        return commandLine.hasOption(TCP.name().toLowerCase());
    }

    private static void startServer(){
        System.out.println("Starting server");
        ServerMain.startServer();
    }

    private static void setClientView(Client_Settings cs){
        if (isCLI() && isGUI()) {
            System.err.println("Options --" + CLI.name().toLowerCase() + " and --" + GUI.name().toLowerCase() + " are mutually exclusive, exiting.");
            System.exit(1);
        } else if (isCLI()) {
            System.out.println("Starting client with CLI");
            cs.setUI(CLI);
        } else if (isGUI()) {
            System.out.println("Starting client with GUI");
            cs.setUI(GUI);
        } else {
            // TODO add ask user and/or load from file
            System.err.println("Please specify either --" + CLI.name().toLowerCase() + " or --" + GUI.name().toLowerCase() + " when starting client");
            System.exit(1);
        }
    }

    private static void setClientConnection(Client_Settings cs) throws ParseException {
        setServerIp();
        if (hasTCPPort() && hasRMIPort()){
            System.err.println("Options --rmi and -- are tcp mutually exclusive, exiting.");
            System.exit(1);
        } else if (hasTCPPort()) {
            System.out.println("Connecting Client via TCP");
            cs.setConnection(TCP);
            String TCPport = commandLine.getOptionValue("tcp");
            NetworkSettings.TCPport = Integer.parseInt(TCPport);

        } else if (hasRMIPort()) {
            System.out.println("Connecting Client via RMI");
            cs.setConnection(RMI);
            String RMIport = commandLine.getOptionValue("rmi");
            NetworkSettings.TCPport = Integer.parseInt(RMIport);
        }
        else{
            // TODO add ask user and/or load from file
            System.err.println("Please specify either --" + RMI.name().toLowerCase() + " or --" + TCP.name().toLowerCase() + " when starting client");
            System.exit(1);
        }
    }

    private static void setServerConnection() throws ParseException {
        setServerIp();
        if(!commandLine.hasOption("rmi") || !commandLine.hasOption("tcp")){
            System.err.println("both rmi and tcp need to be specified in server");
            System.exit(1);
        }
        String TCPport = commandLine.getOptionValue("tcp");
        NetworkSettings.TCPport = Integer.parseInt(TCPport);
        String RMIport = commandLine.getOptionValue("rmi");
        NetworkSettings.RMIport = Integer.parseInt(RMIport);
    }

    private static void setServerIp() throws ParseException {
        String ipStr = commandLine.getOptionValue("ip");
        IpAddressV4 ip;
        if(ipStr.equals("localhost")){
            ip = new IpAddressV4("127.0.0.1");
        }
        else{
            ip = new IpAddressV4(ipStr);
        }
        NetworkSettings.serverIp = ip;
    }
    private static void startClient(){
        ClientMain.startClient();
    }
    public static void main(String[] args) {
        initOptions();

        try {
            commandLine = (new DefaultParser()).parse(options, args);

            if (isServer() && isClient()) {
                System.err.println("Options --client and --server are mutually exclusive, exiting.");
                System.exit(1);
            }
            if (isServer()) {
                setServerConnection();
                startServer();
                System.exit(0);

            } else if (isClient()) {
                Client_Settings cs = new Client_Settings();
                setClientView(cs);
                setClientConnection(cs);
                startClient();
                System.exit(0);

            } else {
                System.err.println("Specify either --client or --server, exiting.");
                System.exit(1);
            }

        } catch (ParseException e) {
            System.out.println("Parse error while reading command line options: ");
            e.printStackTrace();
        }

    }
}
