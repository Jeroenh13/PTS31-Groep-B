package Tests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dontcrash.*;
import dontcrash.Character;
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
    
    Player player;
    
    public PlayerTest() {
    }
    
    @Before
    public void setUp() {
        player = new Player(1, "Test", 0, "Test@email.com");
    }

    @Test
    public void addFriendTest(){
        Player friend = new Player(2,"Friend", 0, "Friend@email.com");
        
        assertTrue("De vriend kan niet worden toegevoegd", player.addFriend(friend));
        assertFalse("de vriend kan worden toegevoegd terwijl dit al is gebeurd", player.addFriend(friend));
    }
    
    @Test
    public void setCharacterTest(){
        Character character = new Character("Naam", 1);
        
        player.setCharacter(character);
        
        assertEquals("de characters komen niet overeen", character, player.currentCharacter);
    }
}
