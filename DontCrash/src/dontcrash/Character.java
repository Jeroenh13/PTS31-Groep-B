package dontcrash;

import java.io.Serializable;
import java.util.Random;
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
    private int characterID;
    private String name;
    private int cost;
    public double speed;
    public float direction;
    public double curX;
    public double curY;
    public double red;
    public double green;
    public double blue;
    public boolean gameOver;
    public boolean getinput = true;
    public boolean invincible;
    
    /**
     * Initializes a new character using a player and a characterid
     * @param player to use
     * @param characterID id of the character
     */
    public Character(Player player, int characterID)
    {
        gameOver = false;
        this.name = player.name;
        this.characterID = characterID;
        Random rnd = new Random();
        direction = rnd.nextInt(3);
        speed = 2;
        invincible = false;
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
        red = color.getRed();
        blue = color.getBlue();
        green = color.getGreen();
    }
    
    public Point getPoint(){
        return new Point((int)curX, (int)curY, red,green,blue);
    }
}
