/*
 * Class for the information of a game Room.
 */
package dontcrash;

import Controllers.CharacterScreenController;
import RMI.Server;
import SharedInterfaces.IGame;
import SharedInterfaces.IRoom;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Room implements Serializable, IRoom
{
    public int roomID;
    public List<Player> players;
    public IGame currentGame;
    public int chatPort;
    public Player host;
    
    /**
     * Initializes a new instance of room;
     * @param roomID id of the room
     * @param host host of the room
     * @throws java.io.IOException
     */
    public Room(int roomID,Player host) throws IOException
    {
        players = new ArrayList<>();
        this.roomID = roomID;
        this.host = host;
        players.add(host);
    }
    
    /**
     * Starts a new game
     * @return null if couldnt start game, game if could start game
     * @throws RemoteException
     */
    public IGame startGame() throws RemoteException
    {
        //if(this.currentGame != null || this.players.isEmpty())
        //    return null;
        IGame game = new Game(this.players,this.roomID);
        this.currentGame = game;
        return game;
    }
 
    
    /**
     * Adds player to this room, if the player isnt already in this room
     * @param player
     * @return 
     */
    public boolean addPlayer(Player player)
    {
        if(players.contains(player))
            return false;
        players.add(player);
        return true;
    }
    
    /**
     * removes the given player from this room
     * @param player to remove from room
     */
    public void exitRoom(Player player)
    {
        players.remove(player);
    }
    
    /**
     * returns roomid
     * @return returns roomID
     */
    @Override
    public String toString()
    {
        return String.valueOf(roomID);
    }

    /**
     * gets the roomId
     * @return 
     * @throws RemoteException 
     */
    @Override
    public int getRoomId() throws RemoteException {
        return roomID;
    }

    /**
     * 
     * @return 
     * @throws RemoteException 
     */
    @Override
    public int getRoomChatPort() throws RemoteException {
        return chatPort;
    }

    @Override
    public ArrayList<Player> getPlayers() throws RemoteException {
        return (ArrayList<Player>) players;
    }

    @Override
    public IGame getCurrentGame() {
        return currentGame;
    }
    
    public Player getHost()
    {
        return host;
    }
}
