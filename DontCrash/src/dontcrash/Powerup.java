/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dontcrash;

/**
 *
 * @author Saya
 */
public class Powerup
{
    public int tijdsduur;
    public PowerupType type;
    public float modifier;
    public int effected;
    
    /**
     * Initializes a new instance of powerup
     */
    public Powerup(PowerupType type, int tijdsduur, float modifier, int effected)
    {
        this.type = type;
        this.tijdsduur = tijdsduur;
        this.modifier = modifier;
        this.effected = effected;
    }
    
    public void Effect()
    {
        
    }
    
}
