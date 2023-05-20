package it.polimi.ingsw.shared;

import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.List;

public class IpAddressV4 {
    int Byte1;
    int Byte2;
    int Byte3;
    int Byte4;

    private boolean correctByte(int n){
        return n >= 0 && n <= 255;
    }

    public IpAddressV4(int byte1, int byte2, int byte3, int byte4) throws Exception {
        if(!correctByte(byte1) || !correctByte(byte2) ||
                !correctByte(byte3) || !correctByte(byte4)){
            throw new Exception();
        }
        this.Byte1 = byte1;
        this.Byte2 = byte2;
        this.Byte3 = byte3;
        this.Byte4 = byte4;
    }

    public IpAddressV4(String str) throws ParseException{
        if("localhost".equals(str)){
            this.Byte1 = 127;
            this.Byte2 = 0;
            this.Byte3 = 0;
            this.Byte4 = 1;
            return;
        }
        List<String> bytesStr = List.of(str.split("\\."));
        if(bytesStr.size() != 4){
            throw new ParseException("wrong IP parsing");
        }
        List<Integer> bytes = new ArrayList<>();
        try{
            for(String byteStr : bytesStr){
                int field = Integer.parseInt(byteStr);
                if(correctByte(field)){
                    bytes.add(field);
                }
                else{
                    throw new Exception();
                }
            }
        }
        catch (Exception e){
            throw new ParseException("wrong IP parsing");
        }
        this.Byte1 = bytes.get(0);
        this.Byte2 = bytes.get(1);
        this.Byte3 = bytes.get(2);
        this.Byte4 = bytes.get(3);
    }

    @Override
    public boolean equals(Object o){
        if(o == null || this.getClass() != o.getClass()){ //check if they are not same class
            return false;
        } else if (this == o) { //check if it's the same object
            return true;
        }
        IpAddressV4 ip = (IpAddressV4) o;
        return toString().equals(o.toString());
    }

    @Override
    public String toString(){
        return  Byte1 + "." +
                Byte2 + "." +
                Byte3 + "." +
                Byte4;
    }
}
