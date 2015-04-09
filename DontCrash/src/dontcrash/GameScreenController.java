/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dontcrash;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.EllipseBuilder;
import javafx.stage.*;

/**
 *
 * @author Bas
 */
public class GameScreenController implements Initializable {

    @FXML
    Button PLAY;
    @FXML
    Circle cCircle;
    @FXML
    TextArea gameArea;
    @FXML
    Canvas game;
    int direction = 0;
    @FXML
    Label lblRound;

    int speed = 1;
    ArrayList<Point> positions = new ArrayList<>();
    double CurX;
    double CurY;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnToggleSoundPress(Event envt) {
        gameArea.selectAll();
        AnimationTimer t = new AnimationTimer() {

            @Override
            public void handle(long now) {
                double X = 0;
                double Y = 0;
                if (direction == 0) {
                    CurY = CurY + speed;
                } else if (direction == 1) {
                    CurX = CurX - speed;
                } else if (direction == 2) {
                    CurY = CurY - speed;
                } else if (direction == 3) {
                    CurX = CurX + speed;
                }
                draw();
                Point p = new Point((int) CurX, (int) CurY);
                if (!checkPoint(p)) {
                    positions.add(p);
                    cCircle.relocate(CurX, CurY);
                    lblRound.setText(Integer.toString(direction));
                } else {
                    lblRound.setText("AF");
                }
            }

            @Override
            public void start() {
                cCircle.relocate(20, 20);
                CurX = cCircle.getLayoutX();
                CurY = cCircle.getLayoutY();
                super.start();
            }
        };
        t.start();
    }

    private boolean checkPoint(Point loc) {
        for (Point p : positions) {
            if (p.X == loc.X && p.Y == loc.Y) {
                return true;
            }
        }
        return false;
    }

    public void draw() {
        GraphicsContext gc = game.getGraphicsContext2D();
        gc.setStroke(Color.ORANGE);
        gc.setLineWidth(5);
        gc.fillOval(CurX, CurY, 1, 1);
        gc.strokeOval(CurX, CurY, 1, 1);
    }

    public void handleStuff(Event evt) {
        gameArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.LEFT) {
                    if (direction == 0) {
                        direction = 3;
                    } else {
                        direction--;
                    }
                } else if (ke.getCode() == KeyCode.RIGHT) {
                    if (direction == 3) {
                        direction = 0;
                    } else {
                        direction++;
                    }
                }
            }
        });
    }

    private static class Point {

        public int X;
        public int Y;

        public Point(int X, int Y) {
            this.X = X;
            this.Y = Y;
        }
    }
}
