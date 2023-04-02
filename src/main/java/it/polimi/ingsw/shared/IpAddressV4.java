package it.polimi.ingsw.shared;

public class IpAddressV4 {
    byte Byte1;
    byte Byte2;
    byte Byte3;
    byte Byte4;

    public IpAddressV4(byte Byte1, byte Byte2, byte Byte3, byte Byte4){
        this.Byte1 = Byte1;
        this.Byte2 = Byte2;
        this.Byte3 = Byte3;
        this.Byte4 = Byte4;
    }
    public IpAddressV4(short Byte1, short Byte2, short Byte3, short Byte4){
        this.Byte1 = (byte) Byte1;
        this.Byte2 = (byte) Byte2;
        this.Byte3 = (byte) Byte3;
        this.Byte4 = (byte) Byte4;
    }
    @Override
    public String toString(){
        return  Byte1 + "." +
                Byte2 + "." +
                Byte3 + "." +
                Byte4;
    }
}
