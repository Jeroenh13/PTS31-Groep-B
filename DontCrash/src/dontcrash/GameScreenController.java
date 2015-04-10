/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dontcrash;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    int direction;
    @FXML
    Label lblRound;

    double speed = 2;
    ArrayList<Point> positions = new ArrayList<>();
    ArrayList<DrawablePowerup> powerups = new ArrayList<>();
    double CurX;
    double CurY;

    //Powerup stuff
    private int spawnChancePowerUp = 30; // Between 0 and 10000 chance every tick to spawn powerup
    private boolean invincible = false;

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
                    CurY = CurY - speed;
                } else if (direction == 1) {
                    CurX = CurX + speed;
                } else if (direction == 2) {
                    CurY = CurY + speed;
                } else if (direction == 3) {
                    CurX = CurX - speed;
                }
                draw();
                Point p = new Point((int) CurX, (int) CurY);
                if (!checkPoint(p)) {
                    positions.add(p);
                    cCircle.relocate(CurX, CurY);
                    //lblRound.setText(Double.toString(CurX));
                } else {
                    endGame(this);
                }
                DrawablePowerup hitdpu = checkPointPowerup(p);
                if (hitdpu != null) {
                    redraw();
                    applyPowerup(hitdpu.powerup);
                }
                DrawablePowerup dpu = spawnPowerUp();
                if (dpu != null) {
                    drawPowerup(dpu);
                }
            }

            @Override
            public void start() {
                cCircle.relocate(20, 20);
                CurX = cCircle.getLayoutX();
                CurY = cCircle.getLayoutY();
                super.start();
                direction = 2;
                speed = 2;
            }
        };
        t.start();
        PLAY.setDisable(true);
    }

    private boolean checkPoint(Point loc) {
        if (loc.Y >= game.getHeight() || loc.Y <= 0 || loc.X >= game.getWidth() || loc.X <= 0) {
            return true;
        }
        if (!invincible) {
            lblRound.setText("NORMAL");
            for (Point p : positions) {
                if (p.X == loc.X && p.Y == loc.Y) {
                    return true;
                }
            }
        } else{
            lblRound.setText("INVINCIBLE");
        }
        return false;
    }

    private DrawablePowerup checkPointPowerup(Point loc) {
        for (DrawablePowerup p : powerups) {
            if (p.MinX <= loc.X && p.MaxX >= loc.X && p.MinY <= loc.Y && p.MaxY >= loc.Y) {
                powerups.remove(p);
                return p;
            }
        }

        return null;
    }

    private void endGame(AnimationTimer t) {
        GraphicsContext gc = game.getGraphicsContext2D();
        //Stop timer
        t.stop();
        //Clear board for new round
        lblRound.setText("AF");
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        positions.clear();
        powerups.clear();
        PLAY.setDisable(false);
    }

    public void draw() {
        GraphicsContext gc = game.getGraphicsContext2D();
        gc.setStroke(Color.ORANGE);
        gc.setLineWidth(1);
        //gc.fillOval(CurX, CurY, 1, 1);
        gc.strokeOval(CurX, CurY, 1, 1);
    }

    public void drawPowerup(DrawablePowerup dpu) {
        GraphicsContext gc = game.getGraphicsContext2D();
        this.powerups.add(dpu);
        gc.setStroke(Color.CRIMSON);
        gc.setFill(Color.CRIMSON);
        gc.fillOval(dpu.MinX, dpu.MinY, dpu.MaxX - dpu.MinX, dpu.MaxY - dpu.MinY);
        gc.strokeOval(dpu.MinX, dpu.MinY, dpu.MaxX - dpu.MinX, dpu.MaxY - dpu.MinY);
    }
    
    public void redraw(){
        GraphicsContext gc = game.getGraphicsContext2D();
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setStroke(Color.ORANGE);
        gc.setLineWidth(1);
        for (Point p : positions) {
             gc.strokeOval(p.X, p.Y, 1, 1);
        }
        gc.setStroke(Color.CRIMSON);
        gc.setFill(Color.CRIMSON);
        for (DrawablePowerup p : powerups) {
            gc.fillOval(p.MinX, p.MinY, p.MaxX - p.MinX, p.MaxY - p.MinY);
            gc.strokeOval(p.MinX, p.MinY, p.MaxX - p.MinX, p.MaxY - p.MinY);
        }
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

    public DrawablePowerup spawnPowerUp() {
        Random random = new Random();

        if (random.nextInt(10000) < spawnChancePowerUp) {
            Powerup powerup;
            int tijdsduur = 2 + random.nextInt(5);;
            //Get a random powerup
            switch (random.nextInt(6) + 1) {
                case 1: //Speed up
                    powerup = new Powerup(PowerupType.INCREASESPEED, tijdsduur, (float) 1.5, 1);
                    break;
                case 2: //Speed down
                    powerup = new Powerup(PowerupType.DECREASESPEED, tijdsduur, (float) 0.5, 1);
                    break;
                /*case 3: //Size up
                    powerup = new Powerup(PowerupType.INCREASESIZE, tijdsduur, (float) 2, 1);
                    break;
                case 4: //Size down
                    powerup = new Powerup(PowerupType.DECREASESIZE, tijdsduur, (float) 0.5, 1);
                    break;*/
                case 5: //ClearBoard
                    powerup = new Powerup(PowerupType.CLEARBOARD, 0, 0, 1);
                    break;
                case 6: //Invincible
                    powerup = new Powerup(PowerupType.INVINCIBLE, tijdsduur, 0, 1);
                    break;
                default:
                    return null;
            }
            GraphicsContext gc = game.getGraphicsContext2D();
            DrawablePowerup dpu = new DrawablePowerup(
                    powerup,
                    random.nextInt((int) gc.getCanvas().getWidth()),
                    random.nextInt((int) gc.getCanvas().getHeight()));
            return dpu;
        }
        return null;
    }

    public void applyPowerup(Powerup powerup) {
        if (powerup.type == PowerupType.INCREASESPEED) {
            (new Thread() {
                public void run() {
                    speed = speed*powerup.modifier;
                    try {
                        Thread.sleep(powerup.tijdsduur*1000);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    speed = speed/powerup.modifier;
                }
            }).start();
        } else if (powerup.type == PowerupType.DECREASESPEED) {
            (new Thread() {
                public void run() {
                    speed = speed*powerup.modifier;
                    try {
                        Thread.sleep(powerup.tijdsduur*1000);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    speed = speed/powerup.modifier;
                }
            }).start();
        } else if (powerup.type == PowerupType.INCREASESIZE) {
            
        } else if (powerup.type == PowerupType.DECREASESIZE) {
            
        } else if (powerup.type == PowerupType.INVINCIBLE) {
            (new Thread() {
                public void run() {
                    invincible = true;
                    try {
                        Thread.sleep(powerup.tijdsduur*1000);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    invincible = false;
                }
            }).start();
        } else if (powerup.type == PowerupType.CLEARBOARD) {
            GraphicsContext gc = game.getGraphicsContext2D();
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            positions.clear();
            powerups.clear();
        }
    }

    private class Point {

        public int X;
        public int Y;

        public Point(int X, int Y) {
            this.X = X;
            this.Y = Y;
        }
    }

    private class DrawablePowerup {

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
}
