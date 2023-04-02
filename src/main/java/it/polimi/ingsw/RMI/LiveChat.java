package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.Color;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LiveChat extends ClientThread{
    /**
     * The client's copy of chat
     */
    List<ChatMessage> chat;
    LiveChat(String playerName, RemoteCall stub) {
        super(playerName, stub);
        chat = new ArrayList<>();
    }
    @Override
    public void run(){
        Scanner scanner = new Scanner(System.in);
        String command = "";
        lock.lock();
        System.out.println(Color.coloredString("Fell free to express yourself",Color.Yellow));
        lock.unlock();
        while(!command.equals("exit")){ //Receive commands until "exit" command is launched
            try{
                lock.lock();
                command = scanner.nextLine();
                lock.unlock();
                if(command.equals("exit")){ //Terminate thread
                    break;
                }
                else if(command.toLowerCase().equals("print")){ //update chat and print it
                    updateLiveChat();
                    printAllMessages();
                }
                else{
                    stub.postToLiveChat(playerName,command); //post message to server
                }
                sleep(500);
            }
            catch (RemoteException e){
                System.out.println(Color.coloredString("Error in RMI",Color.Red));
            } catch (Exception e) {
                System.out.println(Color.coloredString("Error in Message Format",Color.Red));
            }
        }
    }

    /**
     * Downloads all the messages that are present on server and missing in local copy
     * @throws RemoteException
     */
    public void updateLiveChat() throws RemoteException {
        chat.addAll(stub.updateLiveChat(chat.size()));
    }

    /**
     * Print all messages in local copy of chat. If none is present "No message yet" will be printed
     */
    public void printAllMessages(){
        lock.lock();
        System.out.println(Color.coloredString("Printing Messages:",Color.Yellow));
        if(chat == null || chat.size() == 0){
            System.out.println("No messages Yet");
            lock.unlock();
            return;
        }
        for(ChatMessage mes : chat){
            System.out.println(mes);
        }
        lock.unlock();
    }
}
