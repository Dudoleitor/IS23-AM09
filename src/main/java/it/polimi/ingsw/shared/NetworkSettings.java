package it.polimi.ingsw.shared;

import org.apache.commons.cli.ParseException;

public class NetworkSettings {
    public static int RMIport;
    public static int TCPport;
    public static IpAddressV4 serverIp;
    public static final int WaitingTime = 2000; //the time to wait for a server answer
    public static final long serverPingIntervalSeconds = 3;
}
