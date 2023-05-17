package it.polimi.ingsw;

import it.polimi.ingsw.client.Client_Settings;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ClientControllerDriver;
import it.polimi.ingsw.client.controller.cli.ClientControllerCLI;
import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.shared.IpAddressV4;
import it.polimi.ingsw.shared.NetworkSettings;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

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
                .required()
                .hasArg(true)
                .desc("If set, the client will connect via RMI")
                .build());
    }

    private static void setSystemProps(){
        final String timeout = String.valueOf(NetworkSettings.WaitingTime);
        System.setProperty("sun.rmi.transport.connectionTimeout", timeout);
        System.setProperty("sun.rmi.transport.tcp.handshakeTimeout", timeout);
        System.setProperty("sun.rmi.transport.tcp.responseTimeout", timeout);
        System.setProperty("sun.rmi.transport.tcp.readTimeout", timeout);
        System.setProperty("sun.rmi.transport.proxy.connectTimeout", timeout);
        try {
            RMISocketFactory.setSocketFactory(new SocketFactory());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            setTCPPort();

        } else if (hasRMIPort()) {
            System.out.println("Connecting Client via RMI");
            cs.setConnection(RMI);
            setRMIPort();
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
        setTCPPort();
        setRMIPort();
    }

    private static boolean isValidPort(int port){
        return port < 65535 && port > 1024;
    }

    private static void setRMIPort() throws ParseException {
        String RMIport = commandLine.getOptionValue("rmi");
        int rmiPort = Integer.parseInt(RMIport);
        if(isValidPort(rmiPort)){
            NetworkSettings.RMIport = rmiPort;
        }
        else{
            throw new ParseException("Invalid port number for rmi");
        }
    }

    private static void setTCPPort() throws ParseException {
        String TCPport = commandLine.getOptionValue("tcp");
        int tcpPort = Integer.parseInt(TCPport);
        if(isValidPort(tcpPort)){
            NetworkSettings.TCPport = tcpPort;
        }
        else{
            throw new ParseException("Invalid port number for tcp");
        }
    }

    private static void setServerIp() throws ParseException {
        String ipStr = "";
        IpAddressV4 ip;
        if(commandLine.hasOption("ip")){
            ipStr = commandLine.getOptionValue("ip");
        }
        else {
            throw new ParseException("Specify the server ip using --ip");
        }
        if(("localhost").equals(ipStr)){
            ip = new IpAddressV4("127.0.0.1");
        }
        else{
            ip = new IpAddressV4(ipStr);
        }
        NetworkSettings.serverIp = ip;
    }
    private static void startClient(){
        final ClientController controller;
        switch (Client_Settings.ui) {
            case CLI:
                controller = new ClientControllerCLI();
                break;
            case GUI:
                controller = new ClientControllerGUI();
                break;
            default:
                controller = new ClientControllerDriver();
                break;
        }
        controller.startClient();
    }
    public static void main(String[] args) {
        setSystemProps();
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

class SocketFactory extends RMISocketFactory {
    private final int timeout = NetworkSettings.WaitingTime;

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), timeout);
        return socket;
    }

    @Override
    public ServerSocket createServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }

}
