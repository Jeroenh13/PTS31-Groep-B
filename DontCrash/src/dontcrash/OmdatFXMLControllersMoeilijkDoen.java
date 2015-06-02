/*
 * Added class because FXML is hating on its consturctors so you need a certain way to send stuff over 
 * SO YAY THIS CLASS!!!
 */
package dontcrash;

/**
 *
 * @author Bas
 */
public class OmdatFXMLControllersMoeilijkDoen {
 
    private static int roomID;
    private static Player player;
    public static int curPlayerID = 0;

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(Player player) {
        OmdatFXMLControllersMoeilijkDoen.player = player;
    }

    public static int getRoomID() {
        System.out.println(roomID);
        return roomID;
    }

    public static void setRoomID(int aRoomID) {
        roomID = aRoomID;
        System.out.println(aRoomID);
    }
}
