/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SharedInterfaces;

import dontcrash.Game;
import dontcrash.Player;
import dontcrash.portsAndIps;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bas
 */
public interface IRoom extends Serializable 
{
    
    public ArrayList<Player> getPlayers()throws RemoteException;
    
    public Game startGame()throws RemoteException;

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
    
    @Override
    public String toString();
    
    public int getRoomId() throws RemoteException;
    public int getRoomChatPort() throws RemoteException;
}
