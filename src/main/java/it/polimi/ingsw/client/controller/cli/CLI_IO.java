package it.polimi.ingsw.client.controller.cli;

import it.polimi.ingsw.client.Client_Settings;
import it.polimi.ingsw.shared.*;
import it.polimi.ingsw.shared.model.*;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static it.polimi.ingsw.client.Client_Settings.*;

public class CLI_IO {
    private final static Lock cli_lock = new ReentrantLock();
    private final Scanner scanner = new Scanner(System.in);
    private String placeHolder = stdPlaceHolder;
    private static boolean placeHolderToRestore = false;

    /**
     * Constructor. The cli_lock is built as a Singleton
     */
    public CLI_IO(){
        System.out.println(gameLogo);
    }

    protected LobbyCommand askCommand(){
            String input;
            input = scan();
            //Invalid command
            if(!InputSanitizer.isValidMessage(input)){
                synchronized (cli_lock){
                    printErrorMessage("Invalid format");
                }
                return LobbyCommand.Invalid;
            }
            else{
                return LobbyCommand.stringToCommand(input);
            }
    }

    protected void notifyExit() {
        synchronized (cli_lock){
            printErrorMessage("You quit!");
        }
    }

    /**
     * Print all messages in local copy of chat. If none is present "No message yet" will be printed
     */
    protected void showAllMessages(Chat chat){
        synchronized (cli_lock){
            skipPlaceHolder();
            if(chat == null || chat.size() == 0){
                printMessage("No messages yet");
                return;
            }
            chat.getAllMessages().stream().map(mes -> mes.toString()).forEach(System.out::println);
            restorePlaceHolder();
        }
    }
    protected Map<String,String> getMessageFromUser(){
        synchronized (cli_lock){
            List<String> fields = new ArrayList<>();
            fields.add("message");
            return fieldsScan(fields);
        }
    }

    protected Map<String,String> getPrivateMessageFromUser(){
        synchronized (cli_lock){
            List<String> fields = new ArrayList<>();
            fields.add("receiver");
            fields.add("message");
            return fieldsScan(fields);
        }
    }

    protected Move getMoveFromUser(Board board, Shelf shelf){
        PartialMove pm = new PartialMove();
        int column = 0;
        Move move = null;
        synchronized (cli_lock){
            printMessage("Write your move");
            int posNum = getNumberOfTilesFromUser(shelf);
            pm = getPartialMoveFromUser(board,posNum);
            column = getColumnFromUser(shelf,posNum);
            move = new Move(pm,column);
            printMessage("Your move:\n" + move.toString());
        }
        return move;
    }

    /**
     * Get from input a valid number of tiles to pick according to game status
     * @param shelf the player's sheld
     * @return the first valid number chosen by user
     */
    private int getNumberOfTilesFromUser(Shelf shelf){
        String posNumStr = "";
        int posNum = 0;
        int maxShelfCapacity = shelf.getHighestColumn();
        boolean validInput = false;

        while(!validInput){
            validInput = true;
            printMessage("How many tiles do you want to pick?:");
            posNumStr =  scan();

            //assure it's a number
            if(!InputSanitizer.isInteger(posNumStr)){
                printErrorMessage("Please insert an integer");
                validInput = false;
                continue;
            }

            posNum = Integer.parseInt(posNumStr);

            //assure it's a number that is valid and can fit in at least one column
            if(!(posNum > 0 && posNum <= 3 && posNum <= maxShelfCapacity)){
                printErrorMessage("Invalid number");
                validInput = false;
            }
        }
        return posNum;
    }

    /**
     * Build the partila move from the user input
     * @param board the board
     * @param posNum the chosen number of positions. The choice of tiles can reduce it
     * @return the partial move
     */
    private PartialMove getPartialMoveFromUser(Board board, int posNum){
        PartialMove pm = new PartialMove();
        boolean validInput = false;
        while(!validInput){
            validInput = true;
            for(int i = 0; i < posNum; i++){
                try{

                    //assure that there are valid positions
                    if(board.getValidPositions(pm).isEmpty()){
                        printErrorMessage("There are no more tiles to pick");
                        posNum = i;
                        break;
                    }

                    //show valid moves
                    printMessage("Here is the list of all the valid moves:");
                    for(Position p : board.getValidPositions(pm)){
                        System.out.println("> "+p.toString());
                    }

                    //get position
                    printMessage("Position #"+String.valueOf(i+1)+":");
                    String stringPos = scan();
                    Position pos = Position.fromString(stringPos);

                    //if invalid
                    if(pos == null || !board.getValidPositions(pm).contains(pos)){
                        printErrorMessage("Please enter a valid position");
                        i--; //ask again the same position
                    }
                    else {
                        //add to move
                        pm.addPosition(pos);
                    }
                }
                catch (Exception e){
                    printErrorMessage("Invalid format");
                    validInput = false;
                }
            }
        }
        return decideOrder(pm,board);
    }

    private PartialMove decideOrder(PartialMove pm, Board board){
        boolean validInput = false;
        if(pm.size() > 1){
            List<Position> positions = pm.getBoardPositions();
            pm = new PartialMove();
            printMessage("Select insertion order:");

            while(positions.size() > 1 && distinctTiles(positions,board)){
                validInput = false;
                printMessage("Pick a tile:");
                for(int i = 0; i < positions.size(); i++){
                    try {
                        System.out.println( i+ "> " + board.getTile(positions.get(i)).toColorFulString() + " "+ positions.get(i));
                    } catch (BadPositionException e) {
                        //this should not happend
                    }
                }
                while(!validInput){
                    validInput = true;
                    String numStr = scan();
                    if(!InputSanitizer.isInteger(numStr)){
                        validInput = false;
                        printErrorMessage("Please enter an integer");
                        continue;
                    }
                    int num = Integer.parseInt(numStr);
                    if(num < 0 || num >= positions.size()){
                        validInput = false;
                        printErrorMessage("Input a valid integer");
                    }
                    else{
                        try {
                            pm.addPosition(positions.get(num));
                            positions.remove(num);
                        } catch (InvalidMoveException e) {
                            printErrorMessage("Input a valid integer");
                            validInput = false;
                        }
                    }
                }
            }
            try {
                for(Position p : positions){
                    pm.addPosition(p);
                }
            } catch (InvalidMoveException e) {
                //This should not happen
            }
        }
        return pm;
    }

    private boolean distinctTiles(List<Position> positions, Board board){
        return positions.stream().
                map(p -> {
                    try {
                        return board.getTile((Position) p);
                    } catch (BadPositionException e) {
                        return Tile.Invalid; //this should not happend
                    }
                }).distinct().count() > 1;
    }

    /**
     * Get from input a valid column from user
     * @param shelf the player's sheld
     * @param numPos the number of tiles to put in shelf
     * @return the first valid number column by user
     */
    private int getColumnFromUser(Shelf shelf, int numPos){
        String scannedColumn = "";
        boolean validInput = false;
        int column = 0;
        while(!validInput){

            validInput = true;
            printMessage("Choose the column");
            printMessage("Here is a list of the valid columns");
            for(int choice : columnsToChose(shelf,numPos)){
                System.out.println("> "+choice);
            }
            scannedColumn = scan(); //scan the input

            //assure it's an integer
            if(!InputSanitizer.isInteger(scannedColumn)){
                validInput = false;
                printErrorMessage("Please enter a number");
                continue;
            }

            column = Integer.valueOf(scannedColumn);

            //assure the number is inside the bounds og shelf
            if(!(column >= 0 && column < shelf.getColumns())){
                validInput = false;
                printErrorMessage("Please enter a valid number");
                continue;
            }

            //assure the tiles will fit int the column
            if(columnHeigth(shelf,column) + numPos > shelf.getRows()){
                printErrorMessage("You cannot insert "+numPos+ " tiles in column "+ column);
                validInput = false;
            }
        }
        return column;
    }

    /**
     * @param shelf
     * @param column
     * @return the height of a column
     */
    private int columnHeigth(Shelf shelf, int column){
        return (int) shelf.allTilesInColumn(column).stream()
                .filter(x -> !x.equals(Tile.Empty)).count();
    }

    /**
     * @param shelf
     * @param posNum
     * @return a list of valid columns to choose from
     */
    private List<Integer> columnsToChose(Shelf shelf, int posNum){
        return  IntStream.range(0,shelf.getColumns()).
                filter(col -> columnHeigth(shelf,col) + posNum <= shelf.getRows()).
                boxed().
                collect(Collectors.toList());
    }

    /**
     * Display on CLI the whole model
     * @param b the board
     * @param commongoals the list og common goals
     * @param playerShelves the map of Player names and their shelves
     * @param goal the personal goal
     */
    public void showGameStatus(Board b,List<CommonGoal> commongoals ,Map<String, Shelf> playerShelves, PlayerGoal goal){
        synchronized (cli_lock){
            skipPlaceHolder();
            showBoard(b);
            showCommonGoals(commongoals);
            showShelves(playerShelves);
            showPersonalGoal(goal);
        }
    }

    /**
     * show the board on CLI
     * @param b the board
     */
    public void showBoard(Board b) {
        printMessage(b.toString());
    }

    /**
     * Show a list of common goals
     * @param commonGoalList
     */
    protected void showCommonGoals(List<CommonGoal> commonGoalList) {
        String str = "Common Goals:";
        if(commonGoalList.size() == 0){
            str = str.concat(Color.coloredString("None",Color.Yellow));
        }
        for(CommonGoal cg : commonGoalList){
            str = str.concat("\n"+cg.toString());
        }
        printMessage(str);
    }

    /**
     * Show the shelves of players on CLI
     * @param playerShelves the map of Player names and their shelves
     */
    public void showShelves(Map<String, Shelf> playerShelves) {
        String output = "Shelves:\n";
        final String spaceBetween = "     ";

        for (int k=0; k<shelfStringRows(playerShelves); k++) {  // Iterating over the rows
            for(String player : playerShelves.keySet()) {
                output = output.concat(
                        playerShelves
                                .get(player)
                                .toString()
                                .split("\n")[k]
                );
                output = output.concat(spaceBetween);
            }
            output = output.concat("\n");
        }
        final int shelfRowLength = shelfStringCols(playerShelves);
        for (String player : playerShelves.keySet()) {
            output = player.length()<=shelfRowLength ?
                    output.concat(player) :
                    output.concat(player).substring(0, shelfRowLength);

            int padding = shelfRowLength - player.length();

            for (int k=0; k<padding; k++)
                output = output.concat(" ");
        }
        printMessage(output);
    }

    private static int shelfStringRows(Map<String, Shelf> playerShelves) {
        Shelf shelf = playerShelves.get(playerShelves.keySet().stream().findFirst().get());

        return (shelf.toString().split("\n")).length;
    }
    private static int shelfStringCols(Map<String, Shelf> playerShelves) {
        Shelf shelf = playerShelves.get(playerShelves.keySet().stream().findFirst().get());

        return (shelf.toString().split("\n")[0].length());
    }

    /**
     * Show the personal goal og the player
     * @param goal personal goal
     */
    protected void showPersonalGoal(PlayerGoal goal) {
        printMessage(goal.toString());
    }

    /**
     * Display a chat message on CLI
     * @param message
     */
    public void showChatMessage(ChatMessage message) {
        synchronized (cli_lock){
            skipPlaceHolder();
            System.out.println(message);
            restorePlaceHolder();
        }
    }

    /**
     * Show on CLI the list of all the commands that the client can use
     */
    protected void showHelp() {
        synchronized (cli_lock){
            String help = "Here are all the commands:\n";
            List<String> commandList = Arrays.stream(LobbyCommand.values()).
                    filter(c -> c != LobbyCommand.Help && c != LobbyCommand.Invalid).
                    map(c -> "    -> " + c.getCode().toUpperCase() + " ["+c.getShortcut()+"]: "+c.getDescription()+"\n").
                    collect(Collectors.toList());
            for(String command : commandList){
                help = help.concat(command);
            }
            printMessage(help);
        }
    }

    /**
     * Notify the client that the command was not valid
     */
    protected void notifyInvalidCommand(){
        printErrorMessage("Please input a valid command");
    }

    /**
     * Ask for username
     * @return username
     */
    protected String askUserName(){
        synchronized (cli_lock){
            String name = "";
            while(!InputSanitizer.isValidName(name)){
                printMessage("Enter your username");
                name = scan();
                if(!InputSanitizer.isValidName(name)){
                    printErrorMessage("Please enter a valid name");
                }
            }
            printMessage("Hello "+name+"!");
            return name;
        }
    }

    /**
     * Show the available lobbies
     * @param availableLobbies a map of lobby ID and the numbers of players in it
     * @param description a message that the CLI will display before the lobbies
     */
    public void showLobbies(Map<Integer,Integer> availableLobbies, String description){
        synchronized (cli_lock){
            String lobbyMessage = description;
            if (!(availableLobbies == null) && !availableLobbies.isEmpty()) {
                List<String> lobbyList = (List<String>)
                        availableLobbies.keySet().stream().
                                map(x -> "\n    -> Lobby " + x + ":  " + availableLobbies.get(x) + " players in").
                                collect(Collectors.toList());
                for(String mes : lobbyList){
                    lobbyMessage = lobbyMessage.concat(mes);
                }
            } else {
                lobbyMessage = lobbyMessage + "\n       None";
            }
            printMessage(lobbyMessage);
        }
    }

    /**
     * Show the lobby where the client is in
     * @param availableLobby the lobby where the client is in
     * @param description a message that the CLI will display before the lobbies
     */
    public void showJoinedLobby(int availableLobby, String description){
        synchronized (cli_lock){
            String lobbyMessage = description;
            if (availableLobby > 0) {
                lobbyMessage = " -> Lobby :" + availableLobby;
            } else {
                lobbyMessage = lobbyMessage + "\n       None";
            }
            printMessage(lobbyMessage);
        }
    }

    /**
     * Ask for lobby ID
     * @return lobby ID
     */
    protected LobbySelectionCommand askLobby(){
        int lobbyID;
        String id;
        LobbySelectionCommand command;
        synchronized (cli_lock){
            printMessage("Choose a Lobby (ENTER for random, NEW to create a new one, REFRESH to update lobby list):");
            id = scan();
        }
        if(id.isEmpty()){
            return LobbySelectionCommand.Random;
        }
        else if (id.equalsIgnoreCase("new")) {
            return LobbySelectionCommand.Create;
        }
        else if (id.equalsIgnoreCase("refresh")) {
            return LobbySelectionCommand.Refresh;
        }
        else if(InputSanitizer.isInteger(id)){
            lobbyID = Integer.parseInt(id);
            command = LobbySelectionCommand.Number;
            command.setId(lobbyID);
        }
        else{
            command = LobbySelectionCommand.Invalid;
        }
        return command;
    }

    /**
     * Set the player as admin (change placeholder)
     * @param isAdmin
     */
    protected void setLobbyAdmin(boolean isAdmin){
        if(isAdmin){
            synchronized (cli_lock){
                printMessage("You are the lobby admin!");
            }
            setPlaceHolder(Client_Settings.adminPlaceHolder);
        }
        else{
            setPlaceHolder(Client_Settings.stdPlaceHolder);
        }
    }

    /**
     * Set the player as first (change placeholder)
     * @param firstPlayer
     */
    public void setFirstPlayer(boolean firstPlayer){
        if(firstPlayer){
            synchronized (cli_lock){
                printMessage("You are the first player!");
            }
            setPlaceHolder(Client_Settings.fistPlayerPlaceHolder);
        }
        else{
            setPlaceHolder(Client_Settings.stdPlaceHolder);
        }
    }

    /**
     * Show the status at the end of the game
     * @param leaderBoard the leaderboard
     * @param playername the name of the player
     * @param playerShelves the map of player names and their shelves
     * @param board the board
     */
    public void endGame(List<PlayerWithPoints> leaderBoard, String playername, Map<String, Shelf> playerShelves, Board board) {

        synchronized (cli_lock) {
            String msg = "LeaderBoard:\n";

            for (PlayerWithPoints player : leaderBoard) {
                msg = msg.concat("      -" + player.getPlayerName() + ": " + player.getPoints() + "\n");
            }

            if (leaderBoard.get(0).getPlayerName().equals(playername)) {
                System.out.println(victoryBanner);
            } else {
                System.out.println(defeatBanner);
            }
            printMessage(msg);
            showBoard(board);
            showShelves(playerShelves);
        }
    }


    /**
     * Prints the string (decorated as an error message) in a synchronous way
     * => never to be called internally!!
     * @param  message the error message string
     */
    protected void errorMessage(String message) {
        synchronized (cli_lock){
            skipPlaceHolder();
            printErrorMessage(message);
            restorePlaceHolder();
        }
    }

    /**
     * Prints the string (decorated as a message) in a synchronous way
     * => never to be called internally!!
     * @param message the message string
     */
    public void message(String message) {
        synchronized (cli_lock){
            skipPlaceHolder();
            printMessage(message);
            restorePlaceHolder();
        }
    }

    /**
     * Ask the user if he wants to reload an old game
     * @return
     */
    public boolean askToLoadOldMatch(){
        boolean resp = false;
        synchronized (cli_lock){
            skipPlaceHolder();
            printMessage("Do you want to create a new game over writing all your previous matches with this lobby?");
            String answer = scan();
            if(("yes").equals(answer.toLowerCase())){
                resp = true;
            }
            restorePlaceHolder();
        }
        return resp;
    }

    //String decorators
    private void printErrorMessage(String message){
        System.out.println(errorFormat(message));
    }

    private void printMessage(String message){
        System.out.println(messageFormat(message));
    }

    private void skipPlaceHolder(){
        if(placeHolderToRestore){
            System.out.println("");
        }
    }

    private void restorePlaceHolder(){
        if(placeHolderToRestore){
            printPlaceHolder();
        }
    }

    /**
     * A decorator that formats a message
     * @param s the message
     * @return the decorated message (>GAME: mes with colors)
     */
    private String messageFormat(String s){
        return ">"+ Color.coloredString("GAME",GAMEColor)+": " + Color.coloredString(s,messageColor);
    }

    /**
     * Prints the placeholder
     */
    private void printPlaceHolder(){
        placeHolderToRestore = true;
        System.out.print(placeHolder);
    }

    protected void setPlaceHolder(String placeHolder){
        this.placeHolder = placeHolder;
    }

    /**
     * A decorator that formats an error message
     * @param s the message
     * @return the decorated message (>GAME: mes with colors)
     */
    private String errorFormat(String s){
        return ">"+ Color.coloredString("GAME",GAMEColor)+": " + Color.coloredString(s,errorColor);
    }

    //Scan methods

    /**
     * Scans the input without locking IO
     * @return the user input
     */
    private String scan(){
        printPlaceHolder();
        String command = scanner.nextLine();
        placeHolderToRestore = false;
        return command;
    }

    /**
     * Lock the IO and scans for all the fields
     * @param fields
     * @return a map that associates each field with the scanned String
     */
    private Map<String,String> fieldsScan(List<String> fields){
        Map<String,String> result = new HashMap();
        String value;
        for(String field : fields){
            printMessage(field+":");
            printPlaceHolder();
            value =  scanner.nextLine();
            while(field.equals("message") && !InputSanitizer.isValidMessage(value)){
                printErrorMessage("Invalid message format");
                restorePlaceHolder();
                value =  scanner.nextLine();
            }
            placeHolderToRestore = false;
            result.put(field,value);
        }
        return result;
    }
}