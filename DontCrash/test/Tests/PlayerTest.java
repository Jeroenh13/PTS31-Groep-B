package Tests;

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
        
        assertTrue("Can't add friend to list",player.addFriend(friend));
        assertFalse("Impossibe to  add friend twice", player.addFriend(friend));
    }
    
    @Test
    public void newCharacterTest(){
        dontcrash.Character character = player.newCharacter(player, 1);
        
        assertNotNull("newCharacter doesnt return character properly", character);
    }
}
