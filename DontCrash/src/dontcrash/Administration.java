package dontcrash;

import Database.DatabaseManager;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Saya
 */
public class Administration
{
    private List<Player> players;
    
    private final List<Room> rooms;
    
    private int nextRoomID;
    
    private int nextPlayerID;
    
    DatabaseManager dbm;
    
    /**
     * Initiates a new  instance of administration
     */
    public Administration()
    {
        this.nextRoomID = 1;
        this.nextPlayerID = 1;
        this.players = new ArrayList<Player>();
        this.rooms = new ArrayList<Room>();
        this.dbm = new DatabaseManager();
        
        First();
    }
    
    public void First()
    {
        dbm.OpenConn();
        players = dbm.GetPlayers();
        dbm.CloseConn();
    }
    
    /**
     * Updates the score of the given player
     * @param Player to update the score of
     * @param Score new score
     */
    public void updateScore(Player player, int score)
    {
        player.score = score;
    }
    
    /**
     * Tries to login the player that corresponds with the given name, using the given password
     * @param name of the player
     * @param password of the player
     */
    public void login(String name, String password)
    {
        //TODO returnvalue voor inloggen?
        for(Player player: players){
            if(player.name.equals(name)){
                //TODO check password
            }
        }
    }
    
    /**
     * Initializes a new room using the given name
     * @param name of the room to initialize
     * @return null if the name is already taken, otherwise returns a new room with the given name
     */
    public Room newRoom(String name)
    {
        for (Room room : rooms)
            if(room.name.equals(name))
                return null;
        Room room = new Room(name, nextRoomID);
        this.nextRoomID++;
        rooms.add(room);
        return room;
    }
    
    /**
     * Initializes a new player using the given name
     * @param name of the new player
     * @param password of the new player
     * @param email of the new player
     * @return null if the name is already taken, otherwise returns a new player with the given name
     */
    public Player newPlayer(String name, String password, String email)
    {
        Player p = null;
        
        for (Player player : players)
        {
            if(player.name.equals(name))
                return null;
        }
                
        dbm.OpenConn();
        if (dbm.AddPlayer(name, password, email))
        {
            p = new Player(nextPlayerID, name, 0, email);
            players.add(p);
            nextPlayerID++;
        }
        dbm.CloseConn();
            
        for (Player pl : players)
        {
            System.out.println(pl.name);
        }
        
        return p;
    }
    
    /**
     * Gets a list of all available rooms
     * @return List of all rooms
     */
    public List<Room> getRooms()
    {
        return rooms;
    }
    
    /**
     * Adds the given player to the given room
     * @param player to add to room
     * @param room to join
     * @return true if joined, false if already in that room
     */
    public boolean joinRoom(Player player, Room room)
    {
        return room.addPlayer(player);
    }
}
