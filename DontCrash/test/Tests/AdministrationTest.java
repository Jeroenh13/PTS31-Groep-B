package Tests;

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
    
    Administration admin;
    
    public AdministrationTest() {
    }
    
    
    @Before
    public void setUp() {
        admin = new Administration();
    }

    @Test
    public void newPlayerTest(){
        Player player = admin.newPlayer("TestName", "Testemail");
        assertEquals("TestName", player.name);
        
        Player failPlayer = admin.newPlayer("TestName", "anderEmail");
        Player failPlayer2 = admin.newPlayer("Test", "Testemail");
        assertNull(failPlayer);
        assertNull(failPlayer2);
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
        assertEquals(rooms.size(), 2);
    }
    
    @Test
    public void joinRoomTest(){
        Room room1 = admin.newRoom("TestName");
        Player player = admin.newPlayer("TestName", "Testemail");
        
        assertTrue(admin.joinRoom(player, room1));
        assertFalse(admin.joinRoom(player, room1));
    }
    
    @Test 
    public void updateScoreTest(){
        Player player = admin.newPlayer("TestName", "Testemail");
        
        admin.updateScore(player, 10);
        assertEquals(10,player.score);
        
        admin.updateScore(player, 10);
        assertEquals(20,player.score);
        
        admin.updateScore(player, -10);
        assertEquals(10,player.score);        
    }
    
    @Test
    public void loginTest(){
        Player player = admin.newPlayer("TestName", "Testemail");
        
        assertTrue(admin.login("TestName", "password"));
        assertFalse(admin.login("asdf", "password"));
    }
}
