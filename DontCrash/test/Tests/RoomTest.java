package Tests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dontcrash.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sebastiaan
 */
public class RoomTest {
    
    public RoomTest() {
    }
    Room room;
    Player player;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        player = new Player(1, "Test", 0, "Test@email.com");
        try {
            room = new Room(1, player);
        } catch (IOException ex) {
            Logger.getLogger(RoomTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void addPlayerTest(){
        Player player = new Player(1, "Test", 0, "Test@email.com");
        
        assertTrue(room.addPlayer(player));
        assertFalse(room.addPlayer(player));
    }
    
    @Test
    public void exitRoomRoomTest(){
        Player player = new Player(1, "Test", 0, "Test@email.com");
        assertTrue(room.addPlayer(player));
        
        room.exitRoom(player);
        assertFalse("Player not removed from players", room.players.contains(player));
    }
    
    @Test
    public void removePlayerTest(){
        Player player = new Player(1, "Test", 0, "Test@email.com");
        assertTrue(room.addPlayer(player));
        
        assertTrue("Unable to remove player",room.removePlayer(player));
        assertFalse("Player not removed from players", room.players.contains(player));
    }
    
    @Test
    public void getRoomIDTest(){
        try {
            assertEquals("Returned value differs from actual value",room.getRoomId(), 1);
        } catch (RemoteException ex) {
            Logger.getLogger(RoomTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void getHostTest(){
        assertEquals("Returned value differs from actual value", room.getHost(), this.player);
    }
    
    @Test
    public void ScoreTest(){
        room.setNeededScore(5);
        assertEquals("Returned value differs from actual value", room.getNeededScore(), 5);
    }
}
