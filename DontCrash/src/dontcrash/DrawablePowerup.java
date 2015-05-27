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

    public int minX;
    public int minY;
    public int maxX;
    public int maxY;
    public Powerup powerup;

    public DrawablePowerup(Powerup powerup, int X, int Y) {
        this.powerup = powerup;
        this.minX = X;
        this.minY = Y;
        this.maxX = X + 20;
        this.maxY = Y + 20;
    }
}
