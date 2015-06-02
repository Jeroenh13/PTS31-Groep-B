package dontcrash;

import java.io.Serializable;
import javafx.scene.paint.Color;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Saya
 */
public class Character implements Serializable
{
    int characterID;
    String name;
    int cost;
    double speed;
    float direction;
    double curX;
    double curY;
    Color color;
    
    /**
     * Initializes a new character using a player and a characterid
     * @param player to use
     * @param characterID id of the character
     */
    public Character(Player player, int characterID)
    {
        this.name = player.name;
        this.characterID = characterID;
        direction = 2;
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
    
    /**
     * Gives back the direction
     * @return direction
     */
    public float getDirection()
    {
        return direction;
    }
    
    /**
     * returns the current X
     * @return Current X
     */
    public double X()
    {
        return curX;
    }
    
    /**
     * returns the current Y
     * @return Current Y
     */
    public double Y()
    {
        return curY;
    }
    
    /**
     * returns the current speed
     * @return The speed
     */
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
        this.direction = direction;
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
    
    public void setColor(Color color){
        this.color = color;
    }
    
    public Color getColor(){
        return this.color;
    }
    
    public Point getPoint(){
        return new Point((int)curX, (int)curY, color);
    }
}
