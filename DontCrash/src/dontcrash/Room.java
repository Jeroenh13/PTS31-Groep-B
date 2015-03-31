/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dontcrash;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Saya
 */
public class Room implements Chatbox
{
    public int roomID;
    public String name;
    public List<Player> players;
    public Game currentGame;
    public List<String> messages;
    
    /**
     * Initializes a new instance of room;
     * @param name of the room
     * @param roomID id of the room
     */
    public Room(String name, int roomID)
    {
        players = new ArrayList<Player>();
        messages = new ArrayList<String>();
        this.name = name;
        this.roomID = roomID;
    }
    
    /**
     * Starts a new game
     * @return null if couldnt start game, game if could start game
     */
    public Game startGame(int rounds)
    {
        if(this.currentGame != null || this.players.isEmpty())
            return null;
        Game game = new Game(this.players, rounds);
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
    public void removePlayer(Player player)
    {
        players.remove(player);
    }

    @Override
    public void Send(String message, Object reciever) {
        //TODO
    }
}
