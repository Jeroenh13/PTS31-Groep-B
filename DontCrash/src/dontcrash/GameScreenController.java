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

    @FXML Button PLAY;
    @FXML Circle cCircle;
    @FXML TextArea gameArea;
    @FXML Canvas game;
    @FXML Label lblRound;
    @FXML Circle cCircle2;

    int direction;
    int directionp2;

    double speed = 2;
    ArrayList<Point> positions = new ArrayList<>();
    ArrayList<Point> positionsp2 = new ArrayList<>();
    ArrayList<DrawablePowerup> powerups = new ArrayList<>();
    double CurX;
    double CurY;
    double CurXP2;
    double CurYP2;
    boolean Player1 = true;
    boolean Player2 = true;

    //Powerup stuff
    private int spawnChancePowerUp = 40; // Between 0 and 10000 chance every tick to spawn powerup
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
                if (directionp2 == 0) {
                    CurYP2 = CurYP2 - speed;
                } else if (directionp2 == 1) {
                    CurXP2 = CurXP2 + speed;
                } else if (directionp2 == 2) {
                    CurYP2 = CurYP2 + speed;
                } else if (directionp2 == 3) {
                    CurXP2 = CurXP2 - speed;
                }
                draw();
                Point p = new Point((int) CurX, (int) CurY, Color.ORANGE);
                Point p2 = new Point((int) CurXP2, (int) CurYP2, Color.GREEN);
                if (Player1) {
                    if (!checkPoint(p)) {
                        positions.add(p);
                        cCircle.relocate(CurX, CurY);
                        //lblRound.setText(Double.toString(CurX));
                    } else {
                        Player1 = false;
                        endGame(this);
                    }
                }
                if (Player2) {
                    if (!checkPoint(p2)) {
                        positions.add(p2);
                        cCircle2.relocate(CurXP2, CurYP2);
                        //lblRound.setText(Double.toString(CurX));
                    } else {
                        Player2 = false;
                        endGame(this);
                    }
                }
                DrawablePowerup hitdpu = checkPointPowerup(p);
                DrawablePowerup hitdpu2 = checkPointPowerup(p2);
                if (hitdpu != null) {
                    redraw();
                    applyPowerup(hitdpu.powerup);
                }
                if (hitdpu2 != null) {
                    redraw();
                    applyPowerup(hitdpu2.powerup);
                }
                DrawablePowerup dpu = spawnPowerUp();
                if (dpu != null) {
                    drawPowerup(dpu);
                }
            }

            @Override
            public void start() {
                cCircle.relocate(50, 20);
                CurX = cCircle.getLayoutX();
                CurY = cCircle.getLayoutY();
                cCircle2.relocate(250, 20);
                CurXP2 = cCircle2.getLayoutX();
                CurYP2 = cCircle2.getLayoutY();
                super.start();
                direction = 2;
                directionp2 = 2;
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
        if (!Player1 && !Player2) {
            //Stop timer
            t.stop();
            //Clear board for new round
            lblRound.setText("AF");
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            positions.clear();
            powerups.clear();
            PLAY.setDisable(false);
            Player1 = true;
            Player2 = true;
        }
    }

    public void draw() {
        GraphicsContext gc = game.getGraphicsContext2D();        
        gc.setLineWidth(2);
        if(Player1)
        {
            gc.setStroke(Color.ORANGE);
            gc.strokeOval(CurX, CurY, 1, 1);
        }
        if(Player2)
         {
            gc.setStroke(Color.GREEN);
            gc.strokeOval(CurXP2, CurYP2, 1, 1);
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
                } else if (ke.getCode() == KeyCode.A) {
                    if (directionp2 == 0) {
                        directionp2 = 3;
                    } else {
                        directionp2--;
                    }
                } else if (ke.getCode() == KeyCode.D) {
                    if (directionp2 == 3) {
                        directionp2 = 0;
                    } else {
                        directionp2++;
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
                    speed = speed * powerup.modifier;
                    try {
                        Thread.sleep(powerup.tijdsduur * 1000);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    speed = speed / powerup.modifier;
                }
            }).start();
        } else if (powerup.type == PowerupType.DECREASESPEED) {
            (new Thread() {
                public void run() {
                    speed = speed * powerup.modifier;
                    try {
                        Thread.sleep(powerup.tijdsduur * 1000);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    speed = speed / powerup.modifier;
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

    private class Point {

        public int X;
        public int Y;
        public Color color;

        public Point(int X, int Y, Color color) {
            this.X = X;
            this.Y = Y;
            this.color = color;
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
