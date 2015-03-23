/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dontcrash.*;
import java.util.List;
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
public class AdministrationTest {
    
    public AdministrationTest() {
    }
    Administration admin;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        admin = new Administration();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void newPlayerTest(){
        //TODO emailadres checken als dat bij creatie nodig is.
        Player player = admin.newPlayer("TestName");
        assertEquals("TestName", player.name);
        
        Player failPlayer = admin.newPlayer("TestName");
        assertNull(failPlayer);
    }
    
    @Test
    public void newRoomTest(){
        Room room = admin.newRoom("TestName");
        assertEquals("TestName",room.name);
        
        Room failRoom = admin.newRoom("TestName");
        assertNull(failRoom);
    }
    
    @Test
    public void getRoomsTest(){   
        assertTrue(admin.getRooms().isEmpty());
        Room room1 = admin.newRoom("TestName");
        Room room2 = admin.newRoom("Test");
        
        List<Room> rooms = admin.getRooms();
        assertTrue(rooms.contains(room1));
        assertTrue(rooms.contains(room2));
    }
    
    @Test
    public void joinRoomTest(){
        Room room1 = admin.newRoom("TestName");
        Player player = admin.newPlayer("TestName");
        
        assertTrue(admin.joinRoom(player, room1));
        assertFalse(admin.joinRoom(player, room1));
    }
    
    @Test 
    public void updateScoreTest(){
        Player player = admin.newPlayer("TestName");
        
        admin.updateScore(player, 10);
        assertEquals(10,player.score);
    }
    
    @Test
    public void loginTest(){
        //TODO
    }
}
