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
        assertEquals("Name is incorrect","TestName", player.name);
        
        Player failPlayer1 = admin.newPlayer("TestName", "anderEmail");
        Player failPlayer2 = admin.newPlayer("Test", "Testemail");
        Player failPlayer3 = admin.newPlayer("", "Test");
        Player failPlayer4 = admin.newPlayer("Test", "");
        
        assertNull("Name not unique",failPlayer1);
        assertNull("Email not unique",failPlayer2);
        assertNull("Name not empty",failPlayer3);
        assertNull("Email not empty",failPlayer4);
    }
    
    @Test
    public void newRoomTest(){
        Room room = admin.newRoom("TestName");
        assertEquals("Name is incorrect","TestName",room.name);
        
        Room failRoom = admin.newRoom("TestName");
        assertNull("Name not unique",failRoom);
    }
    
    @Test
    public void getRoomsTest(){   
        assertTrue("Rooms exist",admin.getRooms().isEmpty());
        
        Room room1 = admin.newRoom("TestName");
        Room room2 = admin.newRoom("Test");
        
        List<Room> rooms = admin.getRooms();
        
        assertTrue("Room not in administration",rooms.contains(room1));
        assertTrue("Room not in administration",rooms.contains(room2));
        assertEquals("Too many rooms in administration",rooms.size(), 2);
    }
    
    @Test
    public void joinRoomTest(){
        Room room1 = admin.newRoom("TestName");
        Player player = admin.newPlayer("TestName", "Testemail");
        
        assertTrue("Player already in room",admin.joinRoom(player, room1));
        assertFalse("Player not yet in room",admin.joinRoom(player, room1));
    }
    
    @Test 
    public void updateScoreTest(){
        Player player = admin.newPlayer("TestName", "Testemail");
        
        admin.updateScore(player, 10);
        assertEquals("Score not 10",10,player.score);
        
        admin.updateScore(player, 10);
        assertEquals("Score not 20",20,player.score);
        
        admin.updateScore(player, -10);
        assertEquals("Score not 10",10,player.score);        
    }
    
    @Test
    public void loginTest(){
        Player player = admin.newPlayer("TestName", "Testemail");
        
        assertTrue("Player not in administration",admin.login("TestName", "password"));
        assertFalse("Illegal player logged in",admin.login("asdf", "password"));
    }
}
