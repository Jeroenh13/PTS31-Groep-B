/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dontcrash;

import java.util.List;

/**
 *
 * @author Saya
 */
public class Game
{
    public int gameID;
    public int rounds;
    public int currentRound;
    public List<Player> players;
    
    /**
     * Initializes a new game with the given players
     * @param players of the game
     */
    public Game(List<Player> players, int rounds)
    {
        this.players = players;
        this.rounds = rounds;
    }
    
    
    public void Update()
    {
        
    }
    
    
    public Object getHitBy(Object object)
    {
        return null;
    }
}
