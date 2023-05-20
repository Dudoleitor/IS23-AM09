package it.polimi.ingsw;

import it.polimi.ingsw.client.Client_Settings;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ClientControllerDriver;
import it.polimi.ingsw.client.controller.cli.ClientControllerCLI;
import it.polimi.ingsw.client.controller.gui.ClientControllerGUI;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.shared.Color;
import it.polimi.ingsw.shared.IpAddressV4;
import it.polimi.ingsw.shared.NetworkSettings;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;
import java.util.Arrays;
import java.util.Scanner;

import static it.polimi.ingsw.OptionsParameters.*;
import static it.polimi.ingsw.client.Client_Settings.*;
import static it.polimi.ingsw.client.Client_Settings.Connection.RMI;
import static it.polimi.ingsw.client.Client_Settings.Connection.TCP;
import static it.polimi.ingsw.client.Client_Settings.UI.CLI;
import static it.polimi.ingsw.client.Client_Settings.UI.GUI;


public class main {
    static private Options options;
    static private CommandLine commandLine;
    static private void initOptions(){
        options = new Options();
        Arrays.stream(OptionsParameters.values())
                .forEach(opt -> options.addOption(Option.builder()
                        .longOpt(opt.getName())
                        .hasArg(opt.hasParam())
                        .desc(opt.getDescription())
                        .build()));
    }

    private static void setSystemProps(){
        final String timeout = String.valueOf(NetworkSettings.WaitingTime);
        System.getProperties().setProperty("sun.rmi.transport.connectionTimeout", timeout);
        System.getProperties().setProperty("sun.rmi.transport.tcp.handshakeTimeout", timeout);
        System.getProperties().setProperty("sun.rmi.transport.tcp.responseTimeout", timeout);
        System.getProperties().setProperty("sun.rmi.transport.tcp.readTimeout", timeout);
        System.getProperties().setProperty("sun.rmi.transport.proxy.connectTimeout", timeout);
        try {
            RMISocketFactory.setSocketFactory(new SocketFactory());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void chooseBetween(OptionsParameters a, OptionsParameters b){
        System.out.println(messageFormat("Select "+a.getName()+" or "+b.getName()));
        String response = scan();
        if(a.getName().equals(response.toLowerCase())){
            a.set();
        }
        else if(b.getName().equals(response.toLowerCase())){
            b.set();
        }
        else{
            System.out.println(errorFormat("Please choose "+a.getName()+" or "+b.getName()));
        }
    }

    private static void setClientView(Client_Settings cs){
        while(!(Cli.isSet() || Gui.isSet())){
            chooseBetween(Cli,Gui);
        }
        if (Cli.isSet()) {
            System.out.println(messageFormat("Starting client with CLI"));
            cs.setUI(CLI);
        } else if (Gui.isSet()){
            System.out.println(messageFormat("Starting client with GUI"));
            cs.setUI(GUI);
        }
    }

    private static void setClientConnection(Client_Settings cs){
        setServerIp();
        while(!(Tcp.isSet() || Rmi.isSet())){
            chooseBetween(Tcp,Rmi);
        }
        if (Tcp.isSet()) {
            System.out.println(messageFormat("Connecting Client via TCP"));
            cs.setConnection(TCP);
            setPort(Tcp);

        } else if (Rmi.isSet()) {
            System.out.println(messageFormat("Connecting Client via RMI"));
            cs.setConnection(RMI);
            setPort(Rmi);
        }
    }

    private static void setServerConnection(){
        setServerIp();
        setPort(Tcp);
        setPort(Rmi);
    }

    private static boolean isValidPort(int port){
        return port < 65535 && port > 1024;
    }

    private static int askPort(OptionsParameters opt){
        boolean isValid = false;
        int port = 0;
        while(!isValid){
            System.out.println(messageFormat("Select a port for "+opt.getName()));
            String resp = scan();
            try{
                port = Integer.parseInt(resp);
                if(isValidPort(port)){
                    opt.set();
                    isValid = true;
                }
                else{
                    System.out.println(errorFormat("Insert a valid port"));
                }
            }
            catch (Exception e){
                System.out.println(errorFormat("Insert a valid port"));
            }
        }
        return port;
    }

    private static void setPort(OptionsParameters opt){
        int port = 0;
        if(opt.isSet()){
            try{
                port = Integer.parseInt(commandLine.getOptionValue(opt.getName()));
                if(!isValidPort(port)){
                    opt.reset();
                }
                else{
                    opt.reset();
                }
            }
            catch (Exception e){
                opt.reset();
            }
        }
        if(!opt.isSet()){
            port = askPort(opt);
            opt.set();
        }

        switch (opt){
            case Rmi:
                NetworkSettings.RMIport = port;
                break;
            case Tcp:
                NetworkSettings.TCPport = port;
                break;
            default:
                //do nothing
        }
    }

    private static void setServerIp(){
        String ipStr = "";
        IpAddressV4 ip = null;
        if(Ip.isSet()){
            try{
                ipStr = commandLine.getOptionValue("ip");
                ip = new IpAddressV4(ipStr);
            } catch (ParseException e) {
                Ip.reset();
            }
        }
        if(!Ip.isSet()) {
            ip = askIP();
        }
        Ip.set();
        NetworkSettings.serverIp = ip;
    }

    private static IpAddressV4 askIP(){
        boolean set = false;
        IpAddressV4 res = null;
        while(!set){
            System.out.println(messageFormat("Insert the server IP address"));
            String resp = scan();
            try{
                set = true;
                res = new IpAddressV4(resp);
            } catch (ParseException e) {
                set = false;
                System.out.println(errorFormat("invalid IP"));
            }
        }
        return res;
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

    private static void startServer(){
        System.out.println("Starting server");
        ServerMain.startServer();
    }


    public static void main(String[] args) {
        setSystemProps();
        initOptions();

        try {
            commandLine = (new DefaultParser()).parse(options, args);
            checkParameters(commandLine);
        } catch (ParseException e) {
            System.out.println(messageFormat("Error while parsing CLI, proceed manually"));
        }
        while(!Client.isSet() && !Server.isSet()) {
                chooseBetween(Client,Server);
            }
        if (Server.isSet()) {
            setServerConnection();
            startServer();
            System.exit(0);
        }
        else if (Client.isSet()) {
            Client_Settings cs = new Client_Settings();
            setClientView(cs);
            setClientConnection(cs);
            startClient();
            System.exit(0);
        }
    }

    //UI
    private static String errorFormat(String s){
        return ">"+ Color.coloredString("GAME",GAMEColor)+": " + Color.coloredString(s,errorColor);
    }
    private static String messageFormat(String s){
        return ">"+ Color.coloredString("SETTINGS",GAMEColor)+": " + Color.coloredString(s,messageColor);
    }
    private static void printPlaceHolder(){
        System.out.print("$:");
    }
    private static Scanner scanner = new Scanner(System.in);
    private static String scan(){
        printPlaceHolder();
        String command = scanner.nextLine();
        return command;
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

enum OptionsParameters{
    Server("server","If set, the server is started",false),
    Client("client","If set, the client is started",false),
    Cli("cli","If set, the client will be using the CLI",false),
    Gui("gui","If set, the client will be using the GUI",false),
    Rmi("rmi","If set, the client will connect via RMI",true),
    Tcp("tcp","If set, the client will connect via TCP",true),
    Ip("ip","Set the server IP address",true);
    OptionsParameters(String name, String description,boolean hasParam){
        this.name = name;
        this.description = description;
        this.isSet = false;
        this.hasParam = hasParam;
    }
    private String name;
    private String description;
    private boolean isSet;
    private boolean hasParam;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSet() {
        return isSet;
    }

    public void set(){
        isSet = true;
    }

    public void reset(){
        isSet = false;
    }

    public boolean hasParam() {
        return hasParam;
    }

    public static void checkParameters(CommandLine cl){
        Arrays.stream(OptionsParameters.values()).
                filter(par -> cl.hasOption(par.getName())).
                forEach(par -> par.set());

        //check for mutually exclusive
        if(Server.isSet && Client.isSet){
            Server.reset();
            Client.reset();
        }
        if(Cli.isSet && Gui.isSet){
            Cli.reset();
            Gui.reset();
        }
        if(!Server.isSet && Rmi.isSet && Tcp.isSet){
            Rmi.reset();
            Tcp.reset();
        }
    }

}

