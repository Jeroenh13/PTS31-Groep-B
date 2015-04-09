package dontcrash;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Saya
 */
public class Character
{
    int characterID;
    String name;
    int cost;
    
    /**
     * Initializes a new character using a player and a characterid
     * @param player to use
     * @param characterID id of the character
     */
    public Character(Player player, int characterID)
    {
        this.name = player.name;
        this.characterID = characterID;
    }
    
    public void getHit()
    {
        //rekt
    }
    
    public void gameOver()
    {
        //gg
    }
}
