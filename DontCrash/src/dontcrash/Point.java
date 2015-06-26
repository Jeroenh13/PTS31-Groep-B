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

    public double X;
    public double Y;
    public double red;
    public double green;
    public double blue;
    public double size;

    public Point(double X, double Y, double r, double g, double b, double size) {
        this.X = X;
        this.Y = Y;
        red = r;
        green = g;
        blue = b;
        this.size = size;
    }
}
