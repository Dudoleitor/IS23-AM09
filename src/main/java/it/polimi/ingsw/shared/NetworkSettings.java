package it.polimi.ingsw.shared;

public class NetworkSettings {
    public static int RMIport;
    public static int TCPport;
    public static IpAddressV4 serverIp;
    public static final int WaitingTimeMillis = 3500; //the time to wait for a server answer
    public static final long serverPingIntervalSeconds = 3;
}
