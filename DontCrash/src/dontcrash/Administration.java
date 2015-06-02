package dontcrash;

import Database.DatabaseManager;
import RMI.Server;
import RemoteObserver.BasicPublisher;
import RemoteObserver.RemotePropertyListener;
import RemoteObserver.RemotePublisher;
import SharedInterfaces.IAdministator;
import SharedInterfaces.IRoom;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class Administration extends UnicastRemoteObject implements RemotePublisher, IAdministator {

    BasicPublisher bp;

    private final List<Player> players;

    private final List<IRoom> rooms;

    private int nextRoomID;

    private int nextPlayerID;
    
    private int nextCharacterID;
    
    DatabaseManager dbm;
    
    /**
     * create a new administration
     * @throws RemoteException Connection error
     */
    public Administration() throws RemoteException {
        bp = new BasicPublisher(new String[]{"Room"});
        this.nextRoomID = 1;
        this.nextPlayerID = 1;
        this.nextCharacterID = 1;
        this.rooms = new ArrayList<IRoom>();
        this.dbm = new DatabaseManager();
        
        
        this.players = new ArrayList<Player>();
        /*
        dbm.openConn();
        players = dbm.getPlayers();
        dbm.closeConn();
        */
    }

    /**
     * Updates the score of the given player
     * @param player to update the score of
     * @param score new score
     */
    public void updateScore(Player player, int score) {
        player.score = score;
    }

    /**
     * Tries to login the player that corresponds with the given name, using the
     * given password
     *
     * @param name of the player
     * @param password of the player
     * @return true if login succes or false if not
     */
    public boolean login(String name, String password) {
        boolean succes = false;
        dbm.openConn();
        succes = dbm.checkPassword(name, password);
        dbm.closeConn();
        return succes;
    }

    /**
     * Initializes a new room using the given name
     *
     * @param host owner of the room to initialize
     * @return null if the name is already taken, otherwise returns a new room.
     */
    @Override
    public IRoom newRoom(Player host) {
        Room room = null;
        try {
            room = new Room(nextRoomID, host);
            
            room.chatPort = Server.createNewServer("Chat","Chat"+room.roomID);
            this.nextRoomID++;
            rooms.add(room);
            bp.inform(this, "Room", null, room);
        } catch (IOException ex) {
            Logger.getLogger(Administration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (IRoom)room;
    }

    /**
     * Initializes a new player using the given name
     *
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
                
        dbm.openConn();
        if (dbm.addPlayer(name, password, email))
        {
            p = new Player(nextPlayerID, name, 0, email);
            players.add(p);
            nextPlayerID++;
        }
        dbm.closeConn();
            
        for (Player pl : players)
        {
            System.out.println(pl.name);
        }
        
        return p;
    }

    /**
     * Gets a list of all available rooms
     *
     * @return List of all rooms
     */
    @Override
    public List<IRoom> getRooms() {
        return rooms;
    }

    /**
     * Adds the given player to the given room
     *
     * @param player to add to room
     * @param roomID to join
     * @return true if joined, false if already in that room
     */
    @Override
    public boolean joinRoom(Player player, int roomID) throws RemoteException {
        IRoom addToRoom = null;
        for(IRoom r : rooms)
        {
            if(roomID == r.getRoomId())
            {
                addToRoom = r;
                break;
            }
        }
        if(addToRoom == null)
            return false;
                
        return addToRoom.addPlayer(player);
    }

    /**
     * Starts a new server
     * @param type of the server
     * @return 
     */
    @Override
    public int startNewServer(String type) {
        try {
            return Server.createNewServer(type);
        } catch (IOException ex) {
            Logger.getLogger(Administration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.addListener(listener, property);
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.removeListener(listener, property);
    }

    public int getNextCharacterID(){
        int temp = nextCharacterID;
        nextCharacterID++;
        return temp;
    }
    /**
     * Returns the room with the given ID
     * @param roomID requested room
     * @return room with matching id or null
     * @throws RemoteException RMI connection fails.
     */
    @Override
    public IRoom getRoom(int roomID) throws RemoteException {
       for(IRoom r : rooms)
            if(r.getRoomId() == roomID){
                return r;
            }
       return null;
    }

    /**
     * 
     */
    @Override
    public ArrayList<Player> getPlayers(int roomID) throws RemoteException {
        for(IRoom r : rooms)
            if(r.getRoomId() == roomID)
                return r.getPlayers();
       return null;
        
    }

    @Override
    public void startNewGame(int roomID) throws RemoteException {
        IRoom startRoom = getRoom(roomID);
        startRoom.startGame();
    }

    @Override
    public Character newCharacter(int roomID, Player player, int nextCharacterID) throws RemoteException {
        IRoom tempRoom = getRoom(roomID);
        dontcrash.Character c = null;
        for(Player p : tempRoom.getPlayers())
        {
            if(p.name.equals(player.name))
            {
                c = p.newCharacter(p,getNextCharacterID());
            }
        }
        return c;
    }
}
