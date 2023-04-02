package it.polimi.ingsw.RMI;

import it.polimi.ingsw.shared.Color;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LiveChat extends ClientThread{
    List<ChatMessage> chat;
    LiveChat(String playerName, RemoteCall stub) {
        super(playerName, stub);
        chat = new ArrayList<>();
    }
    @Override
    public void run(){
        Scanner scanner = new Scanner(System.in);
        String command = "";
        System.out.println(Color.coloredString("Fell free to express yourself",Color.Yellow));
        while(!command.equals("exit")){
            try{
                command = scanner.nextLine();
                if(command.equals("exit")){
                    break;
                }
                else if(command.toLowerCase().equals("print")){
                    updateLiveChat();
                    printAllMessages();
                }
                else{
                    stub.postToLiveChat(playerName,command);
                }
            }
            catch (RemoteException e){
                System.out.println(Color.coloredString("Error in RMI",Color.Red));
            } catch (Exception e) {
                System.out.println(Color.coloredString("Error in Message Format",Color.Red));
            }
        }
    }

    public void updateLiveChat() throws RemoteException {
        chat.addAll(stub.updateLiveChat(chat.size()));
    }

    public void printAllMessages(){
        System.out.println(Color.coloredString("Printing Messages:",Color.Yellow));
        if(chat == null || chat.size() == 0){
            System.out.println("No messages Yet");
            return;
        }
        for(ChatMessage mes : chat){
            System.out.println(mes);
        }
    }
}
