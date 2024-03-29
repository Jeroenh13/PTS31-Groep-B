
/*
 * Class that holds all data and connects different classes.
 */
package dontcrash;

import Database.DatabaseManager;
import RMI.Server;
import RemoteObserver.BasicPublisher;
import RemoteObserver.RemotePropertyListener;
import RemoteObserver.RemotePublisher;
import SharedInterfaces.IAdministator;
import SharedInterfaces.IRoom;
import Sockets.SocketClient;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bas
 */
public class Administration extends UnicastRemoteObject implements RemotePublisher, IAdministator {

    private final transient BasicPublisher bp;

    private final List<Player> players;

    private final List<IRoom> rooms;

    private int nextRoomID;

    private int nextPlayerID;

    private int nextCharacterID;

    private final transient DatabaseManager dbm;

    /**
     * create a new administration
     *
     * @throws RemoteException Connection error
     */
    public Administration() throws RemoteException {
        bp = new BasicPublisher(new String[]{"Room", "CharSelect"});
        this.nextRoomID = 1;
        this.nextPlayerID = 1;
        this.nextCharacterID = 1;
        this.rooms = new ArrayList<>();
        this.dbm = new DatabaseManager();

        this.players = new ArrayList<>();
        /*
         dbm.openConn();
         players = dbm.getPlayers();
         dbm.closeConn();
         */
    }

    /**
     * Updates the score of the given player
     *
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
        dbm.openConn();
        boolean succes = dbm.checkPassword(name, password);
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
            room.chatPort = setUpNewChat();
            this.nextRoomID++;
            rooms.add(room);
            bp.inform(this, "Room", null, (IRoom) room);
        } catch (IOException | InterruptedException | ExecutionException ex) {
            Logger.getLogger(Administration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (IRoom) room;
    }

    /**
     * Initializes a new player using the given name
     *
     * @param name of the new player
     * @param password of the new player
     * @param email of the new player
     * @return null if the name is already taken, otherwise returns a new player
     * with the given name
     */
    public Player newPlayer(String name, String password, String email) {
        Player p = null;

        for (Player player : players) {
            if (player.name.equals(name)) {
                return null;
            }
        }
        if (name.equals("admin")) {
            p = new Player(nextPlayerID, name, 0, email);
            players.add(p);
            nextPlayerID++;
        } else {
            dbm.openConn();
            if (dbm.addPlayer(name, password, email)) {
                p = new Player(nextPlayerID, name, 0, email);
                players.add(p);
                nextPlayerID++;
            }
            dbm.closeConn();
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
        for (IRoom r : rooms) {
            if (roomID == r.getRoomId()) {
                addToRoom = r;
                break;
            }
        }
        if (addToRoom == null) {
            return false;
        }

        return addToRoom.addPlayer(player);
    }

    /**
     * Starts a new server
     *
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
        bp.addProperty(property);
        bp.addListener(listener, property);
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.removeListener(listener, property);
    }

    /**
     * gets the next character id
     *
     * @return the next characterID
     */
    @Override
    public int getNextCharacterID() {
        int temp = nextCharacterID;
        nextCharacterID++;
        return temp;
    }

    /**
     * Returns the room with the given ID
     *
     * @param roomID requested room
     * @return room with matching id or null
     * @throws RemoteException RMI connection fails.
     */
    @Override
    public IRoom getRoom(int roomID) throws RemoteException {
        for (IRoom r : rooms) {
            if (r.getRoomId() == roomID) {
                return r;
            }
        }
        return null;
    }

    /**
     *
     * @param roomID
     * @return returns a list of players
     * @throws java.rmi.RemoteException
     */
    @Override
    public ArrayList<Player> getPlayers(int roomID) throws RemoteException {
        for (IRoom r : rooms) {
            if (r.getRoomId() == roomID) {
                return r.getPlayers();
            }
        }
        return null;

    }

    @Override
    public void startNewGame(int roomID, double x, double y, double w, double h) throws RemoteException {
        IRoom startRoom = getRoom(roomID);
        startRoom.startGame(x, y, w, h);
    }

    @Override
    public Character newCharacter(int roomID, Player player, int nextCharacterID) throws RemoteException {
        IRoom tempRoom = getRoom(roomID);
        dontcrash.Character c = null;
        for (Player p : tempRoom.getPlayers()) {
            if (p.name.equals(player.name)) {
                c = p.newCharacter(p, getNextCharacterID());
            }
        }
        return c;
    }

    @Override
    public void UpdateCharacter(int roomID, dontcrash.Character c, Player player) throws RemoteException {
        IRoom tempRoom = getRoom(roomID);
        for (Player p : tempRoom.getPlayers()) {
            if (p.name.equals(player.name)) {
                p.character.blue = c.blue;
                p.character.green = c.green;
                p.character.red = c.red;
                p.character.direction = c.direction;
                p.character.getinput = c.getinput;
                p.character.curX = c.curX;
                p.character.curY = c.curY;
                p.character.gameOver = c.gameOver;
                p.character.invincible = c.invincible;
                p.character.size = c.size;
                p.character.speed = c.speed;
                if (c.oldPoint != null) {
                    p.character.oldPoint = c.oldPoint;
                }
                p.score = player.score;
            }
        }
    }

    /**
     * sets up a new chat server
     *
     * @return what port the new server runs on.
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public int setUpNewChat() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future f = pool.submit(new SocketClient());
        return (int) f.get();
    }

    @Override
    public boolean LeaveRoom(Player player, int roomID) throws RemoteException {
        IRoom room = getRoom(roomID);
        boolean succes = room.removePlayer(player);
        if (room.getHost() == null) {
            bp.inform(this, "Room" + room.toString(), null, "Admin left, room closed.");
        }
        if (room.getHost() == null || room.getPlayers().isEmpty()) {
            rooms.remove(room);
        }
        return succes;
    }
    
        @Override
    public boolean LeaveGame(Player player, int roomID) throws RemoteException {
        IRoom room = getRoom(roomID);
        boolean succes = room.removePlayer(player);
        if (room.getHost() == null) {
            room.getCurrentGame().Close();
        }
        if (room.getHost() == null || room.getPlayers().isEmpty()) {
            rooms.remove(room);
        }
        return succes;
    }
    
    

    public Player getPlayer(String username) {
        if (username.equals("admin")) {
            for (Player p : players) {
                if (p.name == "admin") {
                    return p;
                }
            }
        }
        dbm.openConn();
        try {
            return dbm.getPlayer(username);
        } catch (Exception e) {
        } finally {
            dbm.closeConn();
        }
        return null;
    }

    @Override
    public void AdminInform(String property, Object oldVal, Object newVal) throws RemoteException {
        bp.inform(this, property, oldVal, newVal);
    }
}
