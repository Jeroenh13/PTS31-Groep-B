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

    public static int getRoomID() {
        System.out.println(roomID);
        return roomID;
    }

    public static void setRoomID(int aRoomID) {
        roomID = aRoomID;
        System.out.println(aRoomID);
    }
}
