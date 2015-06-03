/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SharedInterfaces;

import RemoteObserver.RemotePublisher;
import dontcrash.Character;
import dontcrash.Player;
import dontcrash.Room;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
    public IRoom newRoom(Player host)throws RemoteException;
    
    /**
     * Gets a list of all available rooms
     *
     * @return List of all rooms
     */
    public List<IRoom> getRooms() throws RemoteException;
    
    /**
     * Adds the given player to the given room
     *
     * @param player to add to room
     * @param roomID to join
     * @return true if joined, false if already in that room
     * @throws java.rmi.RemoteException
     */
    public boolean joinRoom(Player player, int roomID) throws RemoteException;
    
    public IRoom getRoom(int roomID) throws RemoteException;
    
    public List<Player>  getPlayers(int roomID) throws RemoteException;
    
    public void startNewGame(int roomID) throws RemoteException;
   
    public int getNextCharacterID() throws RemoteException;

    public Character newCharacter(int roomID, Player p, int nextCharacterID) throws RemoteException;
    
    public void UpdateCharacter(int roomID, dontcrash.Character c, Player player) throws RemoteException;
    
}
