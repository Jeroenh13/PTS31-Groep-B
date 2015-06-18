/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SharedInterfaces;

import dontcrash.Player;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Bas
 */
public interface IRoom extends Serializable 
{
    /**
     * Gets the players of the room.
     * @return List of current players in this room.
     * @throws RemoteException 
     */
    public ArrayList<Player> getPlayers()throws RemoteException;
    
    /**
     * Starts the game.
     * @return the started game.
     * @throws RemoteException 
     */
    public IGame startGame()throws RemoteException;

    /**
     * Adds player to this room, if the player isnt already in this room
     * @param player
     * @return 
     * @throws java.rmi.RemoteException 
     */
    public boolean addPlayer(Player player)throws RemoteException;
    
    /**
     * removes the given player from this room
     * @param player to remove from room
     * @throws java.rmi.RemoteException
     */
    public void exitRoom(Player player)throws RemoteException;
    
    /**
     * returns the roomID in string format
     * @return returns roomID
     */
    @Override
    public String toString();
    
    /**
     * Gets the roomID
     * @return roomId
     * @throws RemoteException 
     */
    public int getRoomId() throws RemoteException;
    
    /**
     * Returns the RoomChatPort
     * @return Port where the chat can be found.
     * @throws RemoteException 
     */
    public int getRoomChatPort() throws RemoteException;

    /**
     * Gets the current game
     * @return current game.
     */
    public IGame getCurrentGame();

    /**
     * Returns who the host is
     * @return Host of the room.
     */
    public Player getHost();

    /**
     * Removes a player from a room.
     * @param player player that needs to be removed.
     * @return if the statements succeeded
     */
    public boolean removePlayer(Player player);
}
