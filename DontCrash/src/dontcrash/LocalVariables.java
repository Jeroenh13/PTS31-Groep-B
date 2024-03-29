/*
 * Added class because FXML is hating on its consturctors so you need a certain way to send stuff over 
 * SO YAY THIS CLASS!!!
 */
package dontcrash;

/**
 *
 * @author Bas
 */
public class LocalVariables {

    private static int roomID;
    private static Player player;
    private static int scoreToWin;
    private static Options options;
    
    /**
     * gets the options
     * @return options that are set
     */
    public static Options getOptions() {
        return options;
    }
    
        /**
     * sets the options
     * @param options 
     */
    public static void setOptions(Options options) {
        LocalVariables.options = options;
    }
    
    /**
     *
     * TODO JAVADOC
     *
     */
    public static int curPlayerID;

    /**
     * Gets the player of the client
     *
     * @return player of the client
     */
    public static Player getPlayer() {
        return player;
    }

    /**
     * Sets the player of the client
     *
     * @param player player to be set
     */
    public static void setPlayer(Player player) {
        LocalVariables.player = player;
        curPlayerID = player.playerID;
        LocalVariables.player.character =  player.character;
    }

    /**
     * Gets the roomID
     *
     * @return roomID where the player is in
     */
    public static int getRoomID() {
        //System.out.println(roomID);
        return roomID;
    }

    /**
     * Sets the roomID where the player is in.
     *
     * @param aRoomID roomID to be set
     */
    public static void setRoomID(int aRoomID) {
        roomID = aRoomID;
        //System.out.println(aRoomID);
    }

    public static int getScore() {
        return scoreToWin;
    }

    public static void setScoreNeeded(int score) {
        scoreToWin = score;
    }

    //host functies toevoegen hier?
}
