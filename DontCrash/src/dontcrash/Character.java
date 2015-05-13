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
    double speed;
    float direction;
    double curX;
    double curY;
    
    /**
     * Initializes a new character using a player and a characterid
     * @param player to use
     * @param characterID id of the character
     */
    public Character(Player player, int characterID)
    {
        this.name = player.name;
        this.characterID = characterID;
        direction = 0;
        speed = 2;
    }
    
    public void getHit()
    {
        //rekt
    }
    
    public void gameOver()
    {
        //gg
    }
    
    public float getDirection()
    {
        return direction;
    }
    
    public double X()
    {
        return curX;
    }
    
    public double Y()
    {
        return curY;
    }
    
    public double speed()
    {
        return speed;
    }
    /**
     * Sets the speed for the character
     * @param speed new speed value
     */
    public void setSpeed(double speed)
    {
        this.speed = speed;
    }
    
    /**
     * Sets the direction for the character
     * @param direction new direction value
     */
    public void setDirection(float direction)
    {
        
    }
    
    /**
     * Sets the X position of the character
     * @param X the X position
     */
    public void setX(double X)
    {
        curX = X;
    }
        /**
     * Sets the Y position of the character
     * @param Y the Y position
     */
    public void setY( double Y)
    {
        curY = Y;
    }
}
