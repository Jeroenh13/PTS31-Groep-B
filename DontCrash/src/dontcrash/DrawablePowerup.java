/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dontcrash;

import java.io.Serializable;

/**
 *
 * @author Sebastiaan
 */
public class DrawablePowerup implements Serializable{

    public int MinX;
    public int MinY;
    public int MaxX;
    public int MaxY;
    public Powerup powerup;

    public DrawablePowerup(Powerup powerup, int X, int Y) {
        this.powerup = powerup;
        this.MinX = X;
        this.MinY = Y;
        this.MaxX = X + 20;
        this.MaxY = Y + 20;
    }
}
