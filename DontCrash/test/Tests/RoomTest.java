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
public class RoomTest {
    
    Room room;
    
    public RoomTest() {
    }
            
    @Before
    public void setUp() {
        room = new Room("TestRoom", 1);
    }
    
    @Test
    public void addPlayerTest(){
        Player player = new Player(1, "Test", 0, "Test@email.com");
        
        assertTrue("Player kan niet worden toegevoegd", room.addPlayer(player));
        assertFalse("Player kan worden toegevoegd, maar bestaat al", room.addPlayer(player));
    }
    
    @Test
    public void verlaatRoomTest(){
        Player player = new Player(1, "Test", 0, "Test@email.com");
        assertTrue("Player kan niet worden toegevoegd", room.addPlayer(player));
        
        room.removePlayer(player);
        assertTrue("Player kan niet worden verwijderd", room.addPlayer(player));
    }
    
    @Test
    public void startGameTest(){
        assertNull(room.startGame(1));
        
        Player player = new Player(1, "Test", 0, "Test@email.com");
        room.addPlayer(player);
        
        Game game = room.startGame(1);
        assertEquals("het aantal rondes komt niet overeen", game.rounds, 1);
        assertEquals("de gemaakte game komt niet overeen", game, room.currentGame);
        assertNull("room moet null zijn want de game is al gestart", room.startGame(1));
        
        Player player2 = new Player(2,"test", 0, "test");
        
        assertTrue("de player kan niet worden gevonden in de gemaakte game", game.players.contains(player));
        assertFalse("de player kan niet worden gevonden in de gemaakte game", game.players.contains(player2));
        
        
    }
}
