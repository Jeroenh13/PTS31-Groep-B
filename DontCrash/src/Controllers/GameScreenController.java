/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import dontcrash.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author Bas
 */
public class GameScreenController implements Initializable {

    @FXML Button PLAY;
    @FXML Circle cCircle;
    @FXML ImageView imgview;
    @FXML TextArea gameArea;
    @FXML Canvas game;
    @FXML Label lblRound;

    ArrayList<Point> positions = new ArrayList<>();
    ArrayList<DrawablePowerup> powerups = new ArrayList<>();
    boolean Player1 = true;
    dontcrash.Character c;

    //Powerup stuff
    private int spawnChancePowerUp = 40; // Between 0 and 10000 chance every tick to spawn powerup
    private boolean invincible = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void btnToggleSoundPress(Event envt) {
        setup();
        gameArea.selectAll();
        AnimationTimer t;
        t = new AnimationTimer() {
            
            @Override
            public void handle(long now) {
                double X = 0;
                double Y = 0;
                //The normal rotation angle of 0 is right
                //0 is Up
                if (c.getDirection() == 0) {
                    c.setY( c.Y() - c.speed());
                    imgview.setRotate(270);
                //1 is Right
                } else if (c.getDirection() == 1) {
                    c.setX(c.X() + c.speed());
                    imgview.setRotate(0);
                //2 is Bottom
                } else if (c.getDirection() == 2) {
                    c.setY(c.Y() + c.speed());
                    imgview.setRotate(90);
                //3 is Left
                } else if (c.getDirection() == 3) {
                    c.setX(c.X() - c.speed());
                    imgview.setRotate(180);
                }
                draw();
                Point p = new Point((int) c.X(), (int) c.Y(), Color.ORANGE);
                if (Player1) {
                    if (!checkPoint(p)) {
                        positions.add(p);
                        imgview.relocate(c.X(), c.Y());
                    } else {
                        endGame(this);
                    }
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
                
                imgview.relocate(50, 20);
                c.setX(imgview.getLayoutX());
                c.setY(imgview.getLayoutY());
                super.start();
                c.setDirection(2);
                c.setSpeed(2);
                HandleKeyPress(null);
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
        } else {
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
        if (!Player1) {
            //Stop timer
            t.stop();
            //Clear board for new round
            lblRound.setText("AF");
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            positions.clear();
            powerups.clear();
            PLAY.setDisable(false);
            Player1 = true;
        }
    }

    public void draw() {
        GraphicsContext gc = game.getGraphicsContext2D();        
        gc.setLineWidth(2);
        if(Player1)
        {
            gc.setStroke(Color.ORANGE);
            gc.strokeOval(c.X(), c.Y(), 1, 1);
        }
    }

    public void drawPowerup(DrawablePowerup dpu) {
        GraphicsContext gc = game.getGraphicsContext2D();
        this.powerups.add(dpu);
        gc.setStroke(Color.CRIMSON);
        gc.setFill(Color.CRIMSON);
        gc.fillOval(dpu.MinX, dpu.MinY, dpu.MaxX - dpu.MinX, dpu.MaxY - dpu.MinY);
        gc.strokeOval(dpu.MinX, dpu.MinY, dpu.MaxX - dpu.MinX, dpu.MaxY - dpu.MinY);
    }

    public void redraw() {
        GraphicsContext gc = game.getGraphicsContext2D();
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        //gc.setStroke(Color.ORANGE);
        gc.setLineWidth(2);
        for (Point p : positions) {
            gc.setStroke(p.color);
            gc.strokeOval(p.X, p.Y, 1, 1);
        }
        gc.setStroke(Color.CRIMSON);
        gc.setFill(Color.CRIMSON);
        for (DrawablePowerup p : powerups) {
            gc.fillOval(p.MinX, p.MinY, p.MaxX - p.MinX, p.MaxY - p.MinY);
            gc.strokeOval(p.MinX, p.MinY, p.MaxX - p.MinX, p.MaxY - p.MinY);
        }
    }

    public void HandleKeyPress(Event evt) {
        gameArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.LEFT) {
                    if (c.getDirection() == 0) {
                        c.setDirection(3);
                    } else {
                        c.setDirection(c.getDirection() - 1);
                    }
                } else if (ke.getCode() == KeyCode.RIGHT) {
                    if (c.getDirection()  == 3) {
                        c.setDirection(0);
                    } else {
                        c.setDirection(c.getDirection() + 1);
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
                    c.setSpeed(c.speed() * powerup.modifier);
                    try {
                        Thread.sleep(powerup.tijdsduur * 1000);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    c.setSpeed(c.speed()/powerup.modifier);
                }
            }).start();
        } else if (powerup.type == PowerupType.DECREASESPEED) {
            (new Thread() {
                public void run() {
                    c.setSpeed(c.speed() * powerup.modifier);
                    try {
                        Thread.sleep(powerup.tijdsduur * 1000);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    c.setSpeed(c.speed()/powerup.modifier);
                }
            }).start();
        } else if (powerup.type == PowerupType.INCREASESIZE) {

        } else if (powerup.type == PowerupType.DECREASESIZE) {

        } else if (powerup.type == PowerupType.INVINCIBLE) {
            (new Thread() {
                public void run() {
                    invincible = true;
                    try {
                        Thread.sleep(powerup.tijdsduur * 1000);
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

    private void setup() {
        Player p = new Player(1, "jeroen", 2,"jeroenh13@live.nl");
        c = new dontcrash.Character(p,1);
    }
}
