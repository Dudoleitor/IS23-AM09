package it.polimi.ingsw.RMI;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentCLiPrinter extends CLIPrinter {
    Lock lock;
    ConcurrentCLiPrinter(Lock lock){
        this.lock = lock;
    }
    @Override
    public void printMessage(String str){
        lock.lock();
        super.printMessage(str);
        lock.unlock();
    }

    @Override
    public void printErrorMessage(String str){
        lock.lock();
        super.printErrorMessage(str);
        lock.unlock();
    }

    /**
     * Write a string on Std out without avoiding concurrent access
     * (No final backspace)
     * @param str the string to print
     */
    public void print(String str){
        lock.lock();
        System.out.print(str);
        lock.unlock();
    }

    /**
     * Write a string on Std out without avoiding concurrent access
     * (With final backspace)
     * @param str the string to print
     */
    public void println(String str){
        lock.lock();
        System.out.println(str);
        lock.unlock();
    }
    /**
     * Write some Strings on Std out without avoiding concurrent access
     * @param stringList the strings to print
     */
    public void multiPrint(List<String> stringList){
        lock.lock();
        for(String s : stringList){
            System.out.println(s);
        }
        lock.unlock();
    }
    /**
     * Lock the access to StdOut when you are waiting for strings
     * @param scanner scanner
     */
    public String scan(Scanner scanner){
        lock.lock();
        String command = scanner.nextLine();
        lock.unlock();
        return command;
    }
}
