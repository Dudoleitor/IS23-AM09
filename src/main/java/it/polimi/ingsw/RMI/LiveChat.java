package it.polimi.ingsw.RMI;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LiveChat extends ClientThread{
    /**
     * The client's copy of chat
     */
    List<ChatMessage> chat;
    LiveChat(String playerName, LobbyRemoteInterface stub) {
        super(playerName, stub);
        chat = new ArrayList<>();
    }
    @Override
    public void run(){
        String command = "";
        io.printMessage("Feel free to express your self!"); //introduction message after login
        while(!command.equals("exit")){ //Receive commands until "exit" command is launched
            try{
                command = io.scan();
                if(command.equals("exit")){ //Terminate thread
                    break;
                }
                else if(command.toLowerCase().equals("print")){ //update chat and print it
                    updateLiveChat();
                    printAllMessages();
                }
                else if(command.equals("")){
                    io.printMessage("Your updates");
                    sleep(100); //give time to others threads to print their status updates
                }
                else{
                    stub.postToLiveChat(playerName,command); //post message to server
                }
            }
            catch (RemoteException e){
                io.printErrorMessage("Error in RMI");
            } catch (Exception e) {
                io.printErrorMessage("Error in Message Format");
            }
        }
    }

    /**
     * Downloads all the messages that are present on server and missing in local copy
     * @throws RemoteException
     */
    public void updateLiveChat() throws RemoteException {
        chat.addAll(stub.updateLiveChat(playerName, chat.size()));
    }

    /**
     * Print all messages in local copy of chat. If none is present "No message yet" will be printed
     */
    public void printAllMessages(){
        if(chat == null || chat.size() == 0){
            io.printMessage("No messages yet");
            return;
        }
        List<String> toPrint = chat.stream().map(mes -> mes.toString()).collect(Collectors.toList());
        toPrint.add(0, io.messageFormat("Here are all messages"));
        io.multiPrint(toPrint);
    }
}
