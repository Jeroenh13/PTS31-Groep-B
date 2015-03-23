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
public class PlayerTest {
    
    public PlayerTest() {
    }
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
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void addFriendTest(){
        Player friend = new Player(2,"Friend", 0, "Friend@email.com");
        
        assertTrue(player.addFriend(friend));
        assertFalse(player.addFriend(friend));
    }
}
