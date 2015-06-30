/*
 * Interface for the administration.
 */
package SharedInterfaces;

import RemoteObserver.RemotePublisher;
import dontcrash.Character;
import dontcrash.Player;
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
     * @throws java.rmi.RemoteException 
     */
    public int startNewServer(String type) throws RemoteException;
    
    /**
     * Initializes a new room using the given name
     *
     * @param host Owner of the room to initialize
     * @return Null if the name is already taken, otherwise returns a new room.
     * @throws java.rmi.RemoteException
     */
    public IRoom newRoom(Player host)throws RemoteException;
    
    /**
     * Gets a list of all available rooms
     *
     * @return List of all rooms
     * @throws java.rmi.RemoteException
     */
    public List<IRoom> getRooms() throws RemoteException;
    
    /**
     * Adds the given player to the given room
     *
     * @param player To add to room
     * @param roomID To join
     * @return True if joined, false if already in that room
     * @throws java.rmi.RemoteException
     */
    public boolean joinRoom(Player player, int roomID) throws RemoteException;
    
    /**
     * gets a room from the server 
     * @param roomID roomID of the requested room.
     * @return The Requested room.
     * @throws RemoteException 
     */
    public IRoom getRoom(int roomID) throws RemoteException;
    
    /**
     * Gets the players from a certain room.
     * @param roomID Room the players are in.
     * @return List of players 
     * @throws RemoteException 
     */
    public List<Player>  getPlayers(int roomID) throws RemoteException;
    
    /**
     * Starts a new game
     * @param roomID room the game needs to be started in.
     * @param x
     * @param y
     * @param w
     * @param h
     * @throws RemoteException 
     */
    public void startNewGame(int roomID,double x, double y, double w, double h) throws RemoteException;
   
    /**
     * 
     * TODO CHECK
     * 
     * Gets the next character ID
     * @return The next CharacterID
     * @throws RemoteException 
     */
    public int getNextCharacterID() throws RemoteException;

    /**
     * 
     * TODO CHECK
     * 
     * Adds a new character to the room.
     * @param roomID RoomID
     * @param p Player
     * @param nextCharacterID Next character
     * @return A new Character
     * @throws RemoteException 
     */
    public Character newCharacter(int roomID, Player p, int nextCharacterID) throws RemoteException;
    
    /**
     * 
     * TODO Create JAVADOC
     * 
     * @param roomID
     * @param c
     * @param player
     * @throws RemoteException 
     */
    public void UpdateCharacter(int roomID, dontcrash.Character c, Player player) throws RemoteException;
    
    /**
     *  Leaves the given room.
     * @param player player that leaves the room.
     * @param roomID Room where the player should be.
     * @return If the leaving the room succeeded.
     * @throws RemoteException 
     */
    public boolean LeaveRoom(Player player, int roomID)throws RemoteException;

    /**
     * Informs the listeners
     * @param prop
     * @param object
     * @param start
     * @throws RemoteException 
     */
    public void AdminInform(String prop, Object object, Object start)throws RemoteException;

    public boolean LeaveGame(Player player, int roomId) throws RemoteException;
    
}
