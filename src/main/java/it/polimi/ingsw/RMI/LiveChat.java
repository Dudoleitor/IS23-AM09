package it.polimi.ingsw.RMI;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

public class LiveChat extends ClientThread{
    /**
     * The client's copy of chat
     */
    boolean exit = false;
    InputSanitizer inputSanitizer = new InputSanitizer();
    List<ChatMessage> chat;
    LiveChat(String playerName, LobbyRemoteInterface stub) {
        super(playerName, stub);
        chat = new ArrayList<>();
    }
    @Override
    public void run(){
        String command;
        io.printMessage("Feel free to express your self!"); //introduction message after login
        while(!exit){ //Receive commands until "exit" command is launched
            try{
                command = io.scan();
                executeUserCommand(command);
            }
            catch (RemoteException e){
                io.printErrorMessage("Error in RMI");
            } catch (Exception e) {
                io.printErrorMessage("Error in Message Format");
            }
        }
    }

    private void executeUserCommand(String command) throws Exception {
        //Invalid command
        if(!inputSanitizer.isValidMessage(command)){
            io.printErrorMessage("Invalid format");
            return;
        }
        //execute action for every command
        switch (command.toLowerCase()){
            case("exit"): //quit game
                io.printMessage("You quit");
                exit = true;
                break;
            case("print"): //print all messages
                updateLiveChat();
                printAllMessages();
            case(""): //refresh updates
                io.printMessage("Your updates");
                //give time to others threads to print their status updates
                sleep(100);
                break;
            case("secret"): //send private message
                postToPrivateChat();
            default: //post message to chat
                stub.postToLiveChat(playerName,command);
                break;
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

    public static boolean checkValidReceiver(ChatMessage message, String receiverName){
        if (message.getClass().equals(PrivateChatMessage.class)){
            if(!((PrivateChatMessage) message).getReciever().equals(receiverName))
                return false;
        }
        return true;
    }

    public void postToPrivateChat() throws Exception {
        List<String> fields = new ArrayList<>();
        fields.add("receiver");
        fields.add("message");
        Map<String,String> header = io.multiScan(fields);
        stub.postSecretToLiveChat(
                playerName,
                header.get("receiver"),
                header.get("message"));
    }
}
