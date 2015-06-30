/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dontcrash;

import RMI.RMIClient;
import RemoteObserver.BasicPublisher;
import RemoteObserver.RemotePropertyListener;
import RemoteObserver.RemotePublisher;
import SharedInterfaces.IAdministator;
import SharedInterfaces.IGame;
import SharedInterfaces.IRoom;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

/**
 *
 * @author Saya
 */
public class Game extends UnicastRemoteObject implements RemotePublisher, IGame, Serializable {

    private int gameID;
    private List<Player> players;
    private Timer timer;
    BasicPublisher bp;
    int i = 0;
    private int roomID;
    private ArrayList<Point> newPoints;
    private ArrayList<Point> oldPoints;
    private ArrayList<Point> allPoints;
    private double x;
    private double y;
    private double w;
    private double h;

    private ArrayList<DrawablePowerup> powerups;

    private final int spawnChancePowerUp = 40; // Between 0 and 10000 chance every tick to spawn powerup

    private IAdministator admin;

    /**
     * Initializes a new game with the given players
     *
     * @param players of the game
     * @param roomID
     * @param x
     * @param y
     * @param w
     * @param h
     * @throws java.rmi.RemoteException
     */
    public Game(List<Player> players, int roomID, double x, double y, double w, double h) throws RemoteException {
        RMIClient rmi = new RMIClient(portsAndIps.IP, portsAndIps.ServerPort, "Admin");
        admin = rmi.setUpNewAdministrator();
        newPoints = new ArrayList<>();
        oldPoints = new ArrayList<>();
        allPoints = new ArrayList<>();
        powerups = new ArrayList();

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.roomID = roomID;
        this.players = players;
        bp = new BasicPublisher(new String[]{"Game"});
    }

    public Game() throws RemoteException {

    }

    /**
     * Updates the current game
     */
    public void Update() {

    }

    /**
     *
     * @param object
     * @return
     */
    public Object getHitBy(Object object) {
        return null;
    }

    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.addListener(listener, property);
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.removeListener(listener, property);
    }

    @Override
    public void startRun() {
        try {
            IRoom r = admin.getRoom(roomID);
            Color[] colors = new Color[]{Color.ORANGE, Color.RED, Color.BLUE, Color.BROWN};
            int colorcnt = 0;
            for (Player p : r.getPlayers()) {
                dontcrash.Character c = p.character;
                c.gameOver = false;
                c.setColor(colors[colorcnt]);
                Random rnd = new Random();
                int x = rnd.nextInt((int) w / 2);
                c.curX = x;
                int y = rnd.nextInt((int) h / 2);
                c.curY = y;
                oldPoints.add(new Point(x, y, colors[colorcnt].getRed(), colors[colorcnt].getGreen(), colors[colorcnt].getBlue(), p.character.size));
                newPoints.add(new Point(x, y, colors[colorcnt].getRed(), colors[colorcnt].getGreen(), colors[colorcnt].getBlue(), p.character.size));
                admin.UpdateCharacter(roomID, c, p);
                allPoints.addAll(oldPoints);
                colorcnt++;
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new GameTask(), 0, 15);
    }

    public void calculatePoints() {
        try {
            IRoom r = admin.getRoom(roomID);
            Point point = null;
            newPoints = new ArrayList<>();
            for (Player p : r.getPlayers()) {
                dontcrash.Character c = p.character;
                System.out.println(c.gameOver);
                if (!c.gameOver) {
                    c.getinput = false;
                    for (Point op : oldPoints) {
                        if (op.red == c.red && op.green == c.green && op.blue == c.blue) {
                            //0 i up
                            if (c.getDirection() == 0) {
                                point = new Point(op.X, c.Y() - c.speed(), op.red, op.green, op.blue, c.size);
                                c.setY(c.Y() - c.speed());
                                //imgview.setRotate(270);

                                //1 is Right
                            } else if (c.getDirection() == 1) {
                                point = new Point(c.X() + c.speed(), op.Y, op.red, op.green, op.blue, c.size);
                                c.setX(c.X() + c.speed());
                                //imgview.setRotate(0);

                                //2 is Bottom
                            } else if (c.getDirection() == 2) {
                                point = new Point(op.X, c.Y() + c.speed(), op.red, op.green, op.blue, c.size);
                                c.setY(c.Y() + c.speed());
                                //imgview.setRotate(90);

                                //3 is Left
                            } else if (c.getDirection() == 3) {
                                point = new Point(c.X() - c.speed(), op.Y, op.red, op.green, op.blue, c.size);
                                c.setX(c.X() - c.speed());
                                //imgview.setRotate(180);
                            }
                            if (!checkPoint(point)) {
                                allPoints.add(point);
                                newPoints.add(point);
                            } else {
                                c.gameOver = true;
                            }
                            //Check collision with powerup
                            DrawablePowerup hitdpu = checkPointPowerup(point);
                            if (hitdpu != null) {
                                applyPowerup(hitdpu.powerup, c);
                            }
                            //Spawn new powerup
                            DrawablePowerup dpu = spawnPowerUp();
                            if (dpu != null) {
                                powerups.add(dpu);
                                bp.inform(this, "Game", "Powerup", powerups);
                            }
                        }
                    }
                    c.getinput = true;
                    admin.UpdateCharacter(roomID, c, p);
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean checkPoint(Point loc) {
        if (loc.Y <= y || loc.Y >= h || loc.X <= x || loc.X >= h) {
            return true;
        }

        for (Point p : allPoints) {
            if (p.X == loc.X && p.Y == loc.Y) {
                return true;
            }
        }
        return false;
    }

    private DrawablePowerup checkPointPowerup(Point loc) {
        for (DrawablePowerup p : powerups) {
            if (p.minX <= loc.X && p.maxX >= loc.X && p.minY <= loc.Y && p.maxY >= loc.Y) {
                powerups.remove(p);
                bp.inform(this, "Game", "Powerup", powerups);
                return p;
            }
        }
        return null;
    }

    public DrawablePowerup spawnPowerUp() {
        Random random = new Random();

        if (random.nextInt(10000) < spawnChancePowerUp) {
            Powerup powerup;
            int tijdsduur = 2 + random.nextInt(5);
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

            DrawablePowerup dpu = new DrawablePowerup(
                    powerup,
                    random.nextInt(600),
                    random.nextInt(500));
            return dpu;
        }
        return null;
    }

    public void applyPowerup(Powerup powerup, dontcrash.Character c) {
        if (powerup.type == PowerupType.INCREASESPEED) {
            (new Thread() {
                @Override
                public void run() {
                    c.setSpeed(c.speed() * powerup.modifier);
                    try {
                        Thread.sleep(powerup.tijdsduur * 1000);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    c.setSpeed(c.speed() / powerup.modifier);
                }
            }).start();
        } else if (powerup.type == PowerupType.DECREASESPEED) {
            (new Thread() {
                @Override
                public void run() {
                    c.setSpeed(c.speed() * powerup.modifier);
                    try {
                        Thread.sleep(powerup.tijdsduur * 1000);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    c.setSpeed(c.speed() / powerup.modifier);
                }
            }).start();
        } else if (powerup.type == PowerupType.INCREASESIZE) {
            (new Thread() {
                @Override
                public void run() {
                    c.size = c.size * 2;
                    try {
                        Thread.sleep(powerup.tijdsduur * 4000);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    c.size = 2;
                }
            }).start();
        } else if (powerup.type == PowerupType.DECREASESIZE) {
            (new Thread() {
                @Override
                public void run() {
                    c.size = c.size * 0.5;
                    try {
                        Thread.sleep(powerup.tijdsduur * 4000);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    c.size = 2;
                }
            }).start();
        } else if (powerup.type == PowerupType.INVINCIBLE) {
            (new Thread() {
                @Override
                public void run() {
                    c.invincible = true;
                    try {
                        Thread.sleep(powerup.tijdsduur * 1000);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    c.invincible = false;
                }
            }).start();
        } else if (powerup.type == PowerupType.CLEARBOARD) {
            allPoints.clear();
            powerups.clear();
            bp.inform(this, "Game", "Powerup", powerups);
        }
    }

    private class GameTask extends TimerTask {

        @Override
        public void run() {
            int cnt = 0;
            calculatePoints();
            for (Player p : players) {
                if (p.character.gameOver) {
                    cnt++;
                }
            }
            if (cnt == players.size()) {
                try {
                    bp.inform(this, "Game", "GameOver", "GameOver");
                    timer.cancel();
                    timer.purge();
                    oldPoints = new ArrayList<>();
                    newPoints = new ArrayList<>();
                    Thread.sleep(2000);
                    startRun();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                bp.inform(this, "Game", "Points", newPoints);
                oldPoints = newPoints;
                newPoints = oldPoints;
            }
        }
    }
    /*
     private ArrayList<Point> moveToPoint(Point previousPosition, Point currentPosition) {
     ArrayList<Point> points = new ArrayList<Point>();
     Point point;
     if (previousPosition.X <= currentPosition.X) {
     if (previousPosition.Y <= currentPosition.Y) {
     for (double y = previousPosition.Y; y < currentPosition.Y; y++) {
     for (double x = previousPosition.X; x < currentPosition.X; x++) {
     point = new Point(x, y, previousPosition.red, previousPosition.green, previousPosition.blue);
     if (checkPoint(point)) {
     return null;
     }
     points.add(point);
     }
     }
     } else {
     for (double y = currentPosition.Y; y < previousPosition.Y; y++) {
     for (double x = previousPosition.X; x < currentPosition.X; x++) {
     point = new Point(x, y, previousPosition.red, previousPosition.green, previousPosition.blue);
     if (checkPoint(point)) {
     return null;
     }
     points.add(point);
     }
     }
     }
     } else {
     if (previousPosition.Y <= currentPosition.Y) {
     for (double y = previousPosition.Y; y < currentPosition.Y; y++) {
     for (double x = currentPosition.X; x < previousPosition.X; x++) {
     point = new Point(x, y, previousPosition.red, previousPosition.green, previousPosition.blue);
     if (checkPoint(point)) {
     return null;
     }
     points.add(point);
     }
     }
     } else {
     for (double y = currentPosition.Y; y < previousPosition.Y; y++) {
     for (double x = currentPosition.X; x < previousPosition.X; x++) {
     point = new Point(x, y, previousPosition.red, previousPosition.green, previousPosition.blue);
     if (checkPoint(point)) {
     return null;
     }
     points.add(point);
     }
     }
     }
     }
     return points;
     }
     */

}
