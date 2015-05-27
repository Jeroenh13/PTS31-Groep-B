/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SharedInterfaces;

import RemoteObserver.RemotePublisher;
import dontcrash.Player;
import dontcrash.Room;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Bas
 */
public interface IAdministator  extends RemotePublisher {
    
    /**
     * Starts a new server
     * @param type of the server
     * @return 
     */
    public int startNewServer(String type) throws RemoteException;
    
    /**
     * Initializes a new room using the given name
     *
     * @param host owner of the room to initialize
     * @return null if the name is already taken, otherwise returns a new room.
     */
    public Room newRoom(Player host)throws RemoteException;
    
    /**
     * Gets a list of all available rooms
     *
     * @return List of all rooms
     */
    public List<Room> getRooms() throws RemoteException;
    
    /**
     * Adds the given player to the given room
     *
     * @param player to add to room
     * @param roomID to join
     * @return true if joined, false if already in that room
     */
    public boolean joinRoom(Player player, int roomID) throws RemoteException;
}
