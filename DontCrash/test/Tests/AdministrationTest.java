package Tests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import SharedInterfaces.IRoom;
import dontcrash.*;
import java.rmi.RemoteException;
import java.util.List;
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
        try {
            admin = new Administration();
        } catch (RemoteException ex) {
            Logger.getLogger(AdministrationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
    }    
    
    @Test
    public void newPlayerTest(){
        Player player = admin.newPlayer("TestPlayer", "1", "1");
        assertEquals("TestName", player.name);
        
        Player failPlayer = admin.newPlayer("TestPlayer","","");
        assertNull(failPlayer);
    }
    
    
    @Test
    public void getRoomsTest(){   
        Player player = admin.newPlayer("admin", "1", "1");
        assertTrue(admin.getRooms().isEmpty());
        IRoom room1 = admin.newRoom(player);
        IRoom room2 = admin.newRoom(player);
        
        List<IRoom> rooms = admin.getRooms();
        assertTrue(rooms.contains(room1));
        assertTrue(rooms.contains(room2));
    }
    
    @Test
    public void joinRoomTest(){
        try {
            Player player = admin.newPlayer("admin", "1", "1");
            IRoom room1 = admin.newRoom(player);
            
            Player player2 = admin.newPlayer("admin", "1", "1");
            assertTrue(admin.joinRoom(player2, room1.getRoomId()));
            assertFalse(admin.joinRoom(player2, room1.getRoomId()));
        } catch (RemoteException ex) {
            Logger.getLogger(AdministrationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test 
    public void updateScoreTest(){
        Player player = admin.newPlayer("admin", "1", "1");
        
        admin.updateScore(player, 10);
        assertEquals(10,player.score);
    }

}
