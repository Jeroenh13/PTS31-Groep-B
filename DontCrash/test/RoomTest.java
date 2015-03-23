/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dontcrash.*;
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
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        room = new Room("TestRoom", 1);
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
    public void verlaatRoomTest(){
        Player player = new Player(1, "Test", 0, "Test@email.com");
        assertTrue(room.addPlayer(player));
        
        room.verlaatRoom(player);
        assertTrue(room.addPlayer(player));
    }
    
    @Test
    public void startGameTest(){
        assertNull(room.startGame());
        Player player = new Player(1, "Test", 0, "Test@email.com");
        room.addPlayer(player);
        Game game = room.startGame();
        assertEquals(game, room.currentGame);
        assertNull(room.startGame());
    }
}
