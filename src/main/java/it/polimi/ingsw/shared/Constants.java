package it.polimi.ingsw.shared;

public class Constants {
    public static final int port = 1234;
    public static final IpAddressV4 serverIp = new IpAddressV4((short) 127, (short) 0, (short) 0, (short) 1);
    public static final int shelfRows = 6;
    public static final int shelfColumns = 5;
    public static final int maxSupportedPlayers = 4;
    public static final int minSupportedPlayers = 2;
    public static final String jsonPathForPlayerGoals = "src/main/resources/personalGoals.json";
    public static final String jsonPathForBoard = "src/main/resources/BoardInit?players.json";
    public static final String jsonPathForCommonGoals = "src/main/resources/?_Players_CommonGoal.json";
}
