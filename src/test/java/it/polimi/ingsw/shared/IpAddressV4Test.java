package it.polimi.ingsw.shared;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IpAddressV4Test {
    @Test
    void fromString(){
        IpAddressV4 ip = null;
        try {
            ip = new IpAddressV4("255.2.3.4");
        }
        catch (ParseException e){
            e.printStackTrace();
        }
    }
    @Test
    void wrongInput(){
        assertThrows(Exception.class,() -> new IpAddressV4("1.1.1"));
        assertThrows(Exception.class,() -> new IpAddressV4("256.1.1.1"));
        assertThrows(Exception.class,() -> new IpAddressV4("hello"));
        assertThrows(Exception.class,() -> new IpAddressV4(256,0,0,1));
        assertThrows(Exception.class,() -> new IpAddressV4(127,0,0,-1));
    }
    @Test
    void fromInt(){
        IpAddressV4 ip = null;
        try {
            ip = new IpAddressV4(200,0,0,1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void worksTheSame(){
        try {
            IpAddressV4 a = new IpAddressV4(127,0,0,1);
            IpAddressV4 b = new IpAddressV4("127.0.0.1");
            assertEquals(a,b);
        } catch (ParseException e) {
            fail();
        } catch (Exception e) {
            fail();
        }
    }
}