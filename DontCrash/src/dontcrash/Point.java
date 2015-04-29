/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dontcrash;

import java.io.Serializable;
import javafx.scene.paint.Color;

/**
 *
 * @author Sebastiaan
 */
public class Point implements Serializable {

    public int X;
    public int Y;
    public Color color;

    public Point(int X, int Y, Color color) {
        this.X = X;
        this.Y = Y;
        this.color = color;
    }
}
