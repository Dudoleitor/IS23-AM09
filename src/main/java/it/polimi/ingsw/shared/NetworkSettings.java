package it.polimi.ingsw.shared;

import org.apache.commons.cli.ParseException;

public class NetworkSettings {
    public static int RMIport = 1234;
    public static int TCPport = 1235;
    public static IpAddressV4 serverIp = createIp();
    private static IpAddressV4 createIp(){
        try{
            return new IpAddressV4("127.0.0.1");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public static final int WaitingTime = 2000; //the time to wait for a server answer
}
