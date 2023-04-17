package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.Client_Settings;
import it.polimi.ingsw.server.ServerMain;
import org.apache.commons.cli.*;

import static it.polimi.ingsw.client.Client_Settings.UI.CLI;
import static it.polimi.ingsw.client.Client_Settings.UI.GUI;


public class main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder()
                .longOpt("server")
                .desc("If set, the server is started")
                .build());
        options.addOption(Option.builder()
                .longOpt("client")
                .desc("If set, the client is started")
                .build());
        options.addOption(Option.builder()
                .longOpt(CLI.name().toLowerCase())
                .desc("If set, the client will be using the CLI")
                .build());
        options.addOption(Option.builder()
                .longOpt(GUI.name().toLowerCase())
                .desc("If set, the client will be using the GUI")
                .build());

        try {
            CommandLine commandLine = (new DefaultParser()).parse(options, args);

            if (commandLine.hasOption("server") && commandLine.hasOption("client")) {
                System.err.println("Options --client and --server are mutually exclusive, exiting.");
                System.exit(1);
            }
            if (commandLine.hasOption("server")) {
                System.out.println("Starting server");
                ServerMain.startServer();
                System.exit(0);

            } else if (commandLine.hasOption("client")) {
                if (commandLine.hasOption(CLI.name().toLowerCase()) && commandLine.hasOption(GUI.name().toLowerCase())) {
                    System.err.println("Options --" + CLI.name().toLowerCase() + " and --" + GUI.name().toLowerCase() + " are mutually exclusive, exiting.");
                    System.exit(1);
                } else if (commandLine.hasOption(CLI.name().toLowerCase())) {
                    System.out.println("Starting client with CLI");
                    ClientMain.startClient(CLI);
                } else if (commandLine.hasOption(GUI.name().toLowerCase())) {
                    System.out.println("Starting client with GUI");
                    ClientMain.startClient(GUI);
                } else {
                    // TODO add ask user and/or load from file
                    System.err.println("Please specify either --" + CLI.name().toLowerCase() + " or --" + GUI.name().toLowerCase() + " when starting client");
                    System.exit(1);
                }
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
