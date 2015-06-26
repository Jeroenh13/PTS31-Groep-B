 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import RMI.RMIClient;
import RemoteObserver.RemotePropertyListener;
import SharedInterfaces.IAdministator;
import SharedInterfaces.IGame;
import SharedInterfaces.IRoom;
import dontcrash.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
public class GameScreenController implements Observer, RemotePropertyListener, Initializable {

    @FXML
    Button PLAY;
    @FXML
    ImageView imgview;
    @FXML
    TextArea gameArea;
    @FXML
    Canvas gameCanvas;
    @FXML
    Label lblRound;

    @FXML
    Label lblPlayer1;
    @FXML
    Label lblPlayer2;
    @FXML
    Label lblPlayer3;
    @FXML
    Label lblPlayer4;

    private IGame game = null;
    private IRoom room = null;

    ArrayList<Point> positions = new ArrayList<>();
    ArrayList<DrawablePowerup> powerups = new ArrayList<>();
    boolean player1 = true;

    private IAdministator admin;
    dontcrash.Character character = null;

    //Powerup stuff
    private final int spawnChancePowerUp = 40; // Between 0 and 10000 chance every tick to spawn powerup
    private boolean invincible = false;

    public GameScreenController() throws IOException {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        RMIClient rmi = new RMIClient(portsAndIps.IP, 1096, "Admin");
        admin = rmi.setUpNewAdministrator();
        try {
            UnicastRemoteObject.exportObject(this, portsAndIps.getNewPort());
        } catch (RemoteException ex) {
            Logger.getLogger(CharacterScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            room = admin.getRoom(OmdatFXMLControllersMoeilijkDoen.getRoomID());
            String name = OmdatFXMLControllersMoeilijkDoen.getPlayer().name;
            String hostname = room.getHost().name;
            if (hostname.equals(name)) {
                admin.startNewGame(room.getRoomId());
                room = admin.getRoom(OmdatFXMLControllersMoeilijkDoen.getRoomID());
                game = room.getCurrentGame();
                PLAY.setVisible(true);
            } else {
                while (room.getCurrentGame() == null) {
                    Thread.sleep(500);
                    room = admin.getRoom(OmdatFXMLControllersMoeilijkDoen.getRoomID());
                    PLAY.setVisible(false);
                }
                game = room.getCurrentGame();
            }

            lblPlayer1.setText("");
            lblPlayer2.setText("");
            lblPlayer3.setText("");
            lblPlayer4.setText("");
            switch (room.getPlayers().size()) {
                case 4:
                    lblPlayer4.setText(room.getPlayers().get(3).name);
                case 3:
                    lblPlayer3.setText(room.getPlayers().get(2).name);
                case 2:
                    lblPlayer2.setText(room.getPlayers().get(1).name);
                    lblPlayer1.setText(room.getPlayers().get(0).name);
                    break;
            }
            game.addListener(this, "Game");

            character = admin.newCharacter(OmdatFXMLControllersMoeilijkDoen.getRoomID(), OmdatFXMLControllersMoeilijkDoen.getPlayer(), admin.getNextCharacterID());

        } catch (RemoteException ex) {
            Logger.getLogger(GameScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void btnToggleSoundPress(Event envt) {
        try {
            game.startRun();
            PLAY.setVisible(false);
        } catch (RemoteException ex) {
            Logger.getLogger(GameScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void HandleKeyPress(Event evt) {
        gameArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                try {
                    room = admin.getRoom(OmdatFXMLControllersMoeilijkDoen.getRoomID());
                    for (Player p : room.getPlayers()) {
                        if (p.name.equals(OmdatFXMLControllersMoeilijkDoen.getPlayer().name)) {
                            character = p.character;
                            if (character.getinput) {
                                if (ke.getCode() == KeyCode.LEFT) {
                                    if (character.getDirection() == 0) {
                                        character.setDirection(3);
                                    } else {
                                        character.setDirection(character.getDirection() - 1);
                                    }
                                } else if (ke.getCode() == KeyCode.RIGHT) {
                                    if (character.getDirection() == 3) {
                                        character.setDirection(0);
                                    } else {
                                        character.setDirection(character.getDirection() + 1);
                                    }
                                }
                            }
                            admin.UpdateCharacter(OmdatFXMLControllersMoeilijkDoen.getRoomID(), character, p);
                        }
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(GameScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if ("GameOver".equals(evt.getNewValue())) {
                    lblRound.setText("Game Over");
                } else {
                    try {
                        // If it is a arraylist of points
                        ArrayList<Point> oldPoints = (ArrayList<Point>) evt.getOldValue();
                        ArrayList<Point> newPoints = (ArrayList<Point>) evt.getNewValue();
                        for (Point op : oldPoints) {
                            for (Point np : newPoints) {
                                if (op.red == np.red && op.green == np.green && op.blue == np.blue) {
                                    draw(newPoints);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        //If not, it is a arraylist of powerups
                        ArrayList<DrawablePowerup> powerups = (ArrayList<DrawablePowerup>) evt.getNewValue();
                        drawPowerups(powerups);
                    }
                }
            }
        });

    }

    public void draw(ArrayList<Point> points) {
        for (Point p : points) {
            GraphicsContext gc = gameCanvas.getGraphicsContext2D();
            gc.setLineWidth(2);
            gc.setStroke(Color.color(p.red, p.green, p.blue));
            gc.strokeOval(p.X, p.Y, 1, 1);
        }
    }

    public void drawPowerups(ArrayList<DrawablePowerup> powerups) {
        for (DrawablePowerup p : powerups) {
            GraphicsContext gc = gameCanvas.getGraphicsContext2D();
            gc.setStroke(Color.CRIMSON);
            gc.setFill(Color.CRIMSON);
            gc.fillOval(p.minX, p.minY, p.maxX - p.minX, p.maxY - p.minY);
            gc.strokeOval(p.minX, p.minY, p.maxX - p.minX, p.maxY - p.minY);
        }
    }
}
//     /**
//     * Eventhandeler for button toggle sound. Currently start button of the game
//     * Consider changing name?
//     *
//     * @param envt
//     */
//    public void btnToggleSoundPress(Event envt) {
//        setup();
//        gameArea.selectAll();
//        AnimationTimer t;
//        t = new AnimationTimer() {
//
//            @Override
//            public void handle(long now) {
//                Point previousPoint = c.getPoint();
//                //The normal rotation angle of 0 is right
//                //0 is Up
//                if (c.getDirection() == 0) {
//                    c.setY(c.Y() - c.speed());
//                    imgview.setRotate(270);
//                    //1 is Right
//                } else if (c.getDirection() == 1) {
//                    c.setX(c.X() + c.speed());
//                    imgview.setRotate(0);
//                    //2 is Bottom
//                } else if (c.getDirection() == 2) {
//                    c.setY(c.Y() + c.speed());
//                    imgview.setRotate(90);
//                    //3 is Left
//                } else if (c.getDirection() == 3) {
//                    c.setX(c.X() - c.speed());
//                    imgview.setRotate(180);
//                }
//                draw();
//                Point currentPoint = c.getPoint();
//                if (player1) {
//                    if (!moveToPoint(previousPoint, currentPoint)) {
//                        imgview.relocate(c.X(), c.Y());
//                    } else {
//                        endGame(this);
//                    }
//                }
//                DrawablePowerup hitdpu = checkPointPowerup(currentPoint);
//                if (hitdpu != null) {
//                    redraw();
//                    applyPowerup(hitdpu.powerup);
//                }
//                DrawablePowerup dpu = spawnPowerUp();
//                if (dpu != null) {
//                    drawPowerup(dpu);
//                }
//            }
//
//            @Override
//            public void start() {
//
//                imgview.relocate(50, 20);
//                c.setX(imgview.getLayoutX());
//                c.setY(imgview.getLayoutY());
//                super.start();
//                c.setDirection(2);
//                c.setSpeed(2);
//                HandleKeyPress(null);
//            }
//        };
//        t.start();
//        PLAY.setDisable(true);
//    }
//
//private ArrayList<Point> moveToPoint(Point previousPosition, Point currentPosition) {
//        ArrayList<Point> points = new ArrayList<Point>();
//        Point point;
//        if (previousPosition.X <= currentPosition.X) {
//            if (previousPosition.Y <= currentPosition.Y) {
//                for (double y = previousPosition.Y; y < currentPosition.Y; y++) {
//                    for (double x = previousPosition.X; x < currentPosition.X; x++) {
//                        point = new Point(x, y, previousPosition.red, previousPosition.green, previousPosition.blue);
//                        if (checkPoint(point)) {
//                            return null;
//                        }
//                        points.add(point);
//                    }
//                }
//            } else {
//                for (double y = currentPosition.Y; y < previousPosition.Y; y++) {
//                    for (double x = previousPosition.X; x < currentPosition.X; x++) {
//                        point = new Point(x, y, previousPosition.red, previousPosition.green, previousPosition.blue);
//                        if (checkPoint(point)) {
//                            return null;
//                        }
//                        points.add(point);
//                    }
//                }
//            }
//        } else {
//            if (previousPosition.Y <= currentPosition.Y) {
//                for (double y = previousPosition.Y; y < currentPosition.Y; y++) {
//                    for (double x = currentPosition.X; x < previousPosition.X; x++) {
//                        point = new Point(x, y, previousPosition.red, previousPosition.green, previousPosition.blue);
//                        if (checkPoint(point)) {
//                            return null;
//                        }
//                        points.add(point);
//                    }
//                }
//            } else {
//                for (double y = currentPosition.Y; y < previousPosition.Y; y++) {
//                    for (double x = currentPosition.X; x < previousPosition.X; x++) {
//                        point = new Point(x, y, previousPosition.red, previousPosition.green, previousPosition.blue);
//                        if (checkPoint(point)) {
//                            return null;
//                        }
//                        points.add(point);
//                    }
//                }
//            }
//        }
//        return points;
//    }
//
//    private boolean checkPoint(Point loc) {
//        if (loc.Y >= gameCanvas.getHeight() || loc.Y <= 0 || loc.X >= gameCanvas.getWidth() || loc.X <= 0) {
//            return true;
//        }
//        if (!invincible) {
//            lblRound.setText("NORMAL");
//            for (Point p : positions) {
//                if (p.X == loc.X && p.Y == loc.Y) {
//                    return true;
//                }
//            }
//        } else {
//            lblRound.setText("INVINCIBLE");
//        }
//        return false;
//    }
//
//private boolean moveToPoint(Point previousPosition, Point currentPosition) {
//        Point point;
//        if (previousPosition.X <= currentPosition.X) {
//            if (previousPosition.Y <= currentPosition.Y) {
//                for (int y = previousPosition.Y; y < currentPosition.Y; y++) {
//                    for (int x = previousPosition.X; x < currentPosition.X; x++) {
//                        point = new Point(x, y, previousPosition.color);
//                        if (checkPoint(point)) {
//                            return false;
//                        }
//                        positions.add(point);
//                    }
//                }
//            } else {
//                for (int y = currentPosition.Y; y < previousPosition.Y; y++) {
//                    for (int x = previousPosition.X; x < currentPosition.X; x++) {
//                        point = new Point(x, y, previousPosition.color);
//                        if (checkPoint(point)) {
//                            return false;
//                        }
//                        positions.add(point);
//                    }
//                }
//            }
//        } else {
//            if (previousPosition.Y <= currentPosition.Y) {
//                for (int y = previousPosition.Y; y < currentPosition.Y; y++) {
//                    for (int x = currentPosition.X; x < previousPosition.X; x++) {
//                        point = new Point(x, y, previousPosition.color);
//                        if (checkPoint(point)) {
//                            return false;
//                        }
//                        positions.add(point);
//                    }
//                }
//            } else {
//                for (int y = currentPosition.Y; y < previousPosition.Y; y++) {
//                    for (int x = currentPosition.X; x < previousPosition.X; x++) {
//                        point = new Point(x, y, previousPosition.color);
//                        if (checkPoint(point)) {
//                            return false;
//                        }
//                        positions.add(point);
//                    }
//                }
//            }
//        }
//        return true;
//    }
//
//    private DrawablePowerup checkPointPowerup(Point loc) {
//        for (DrawablePowerup p : powerups) {
//            if (p.minX <= loc.X && p.maxX >= loc.X && p.minY <= loc.Y && p.maxY >= loc.Y) {
//                powerups.remove(p);
//                return p;
//            }
//        }
//        return null;
//    }
//
//    private void endGame(AnimationTimer t) {
//        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
//        if (!player1) {
//            //Stop timer
//            t.stop();
//            //Clear board for new round
//            lblRound.setText("AF");
//            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
//            positions.clear();
//            powerups.clear();
//            PLAY.setDisable(false);
//            player1 = true;
//        }
//    }
//
//    /**
//     * Draw line player leaves behind
//     */
//    public void draw() {
//        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
//        gc.setLineWidth(2);
//        if (player1) {
//            gc.setStroke(Color.ORANGE);
//            gc.strokeOval(c.X(), c.Y(), 1, 1);
//        }
//    }
//
//    /**
//     * Draw powerup
//     *
//     * @param dpu the powerup to be drawn
//     */
//    public void drawPowerup(DrawablePowerup dpu) {
//        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
//        this.powerups.add(dpu);
//        gc.setStroke(Color.CRIMSON);
//        gc.setFill(Color.CRIMSON);
//        gc.fillOval(dpu.minX, dpu.minY, dpu.maxX - dpu.minX, dpu.maxY - dpu.minY);
//        gc.strokeOval(dpu.minX, dpu.minY, dpu.maxX - dpu.minX, dpu.maxY - dpu.minY);
//    }
//
//    public void redraw() {
//        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
//        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
//        //gc.setStroke(Color.ORANGE);
//        gc.setLineWidth(2);
//        for (Point p : positions) {
//            gc.setStroke(p.color);
//            gc.strokeOval(p.X, p.Y, 1, 1);
//        }
//        gc.setStroke(Color.CRIMSON);
//        gc.setFill(Color.CRIMSON);
//        for (DrawablePowerup p : powerups) {
//            gc.fillOval(p.minX, p.minY, p.maxX - p.minX, p.maxY - p.minY);
//            gc.strokeOval(p.minX, p.minY, p.maxX - p.minX, p.maxY - p.minY);
//        }
//    }
//
//    /**
//     * Makes the character of the player move to the left or right
//     *
//     * @param evt
//     */
//    
//
//    /**
//     * Spawn a powerup on a random position on playfield of a random poweruptype
//     *
//     * @return null if no powerup spawns, return the powerup if spawned
//     */
//    public DrawablePowerup spawnPowerUp() {
//        Random random = new Random();
//
//        if (random.nextInt(10000) < spawnChancePowerUp) {
//            Powerup powerup;
//            int tijdsduur = 2 + random.nextInt(5);
//            //Get a random powerup
//            switch (random.nextInt(6) + 1) {
//                case 1: //Speed up
//                    powerup = new Powerup(PowerupType.INCREASESPEED, tijdsduur, (float) 1.5, 1);
//                    break;
//                case 2: //Speed down
//                    powerup = new Powerup(PowerupType.DECREASESPEED, tijdsduur, (float) 0.5, 1);
//                    break;
//                /*case 3: //Size up
//                 powerup = new Powerup(PowerupType.INCREASESIZE, tijdsduur, (float) 2, 1);
//                 break;
//                 case 4: //Size down
//                 powerup = new Powerup(PowerupType.DECREASESIZE, tijdsduur, (float) 0.5, 1);
//                 break;*/
//                case 5: //ClearBoard
//                    powerup = new Powerup(PowerupType.CLEARBOARD, 0, 0, 1);
//                    break;
//                case 6: //Invincible
//                    powerup = new Powerup(PowerupType.INVINCIBLE, tijdsduur, 0, 1);
//                    break;
//                default:
//                    return null;
//            }
//            GraphicsContext gc = gameCanvas.getGraphicsContext2D();
//            DrawablePowerup dpu = new DrawablePowerup(
//                    powerup,
//                    random.nextInt((int) gc.getCanvas().getWidth()),
//                    random.nextInt((int) gc.getCanvas().getHeight()));
//            return dpu;
//        }
//        return null;
//    }
//
//    /**
//     * Apply the effect of the picked up powerup to the player
//     *
//     * @param powerup
//     */
//    public void applyPowerup(Powerup powerup) {
//        if (powerup.type == PowerupType.INCREASESPEED) {
//            (new Thread() {
//                @Override
//                public void run() {
//                    c.setSpeed(c.speed() * powerup.modifier);
//                    try {
//                        Thread.sleep(powerup.tijdsduur * 1000);
//                    } catch (Exception ex) {
//                        System.out.println(ex.getMessage());
//                    }
//                    c.setSpeed(c.speed() / powerup.modifier);
//                }
//            }).start();
//        } else if (powerup.type == PowerupType.DECREASESPEED) {
//            (new Thread() {
//                @Override
//                public void run() {
//                    c.setSpeed(c.speed() * powerup.modifier);
//                    try {
//                        Thread.sleep(powerup.tijdsduur * 1000);
//                    } catch (Exception ex) {
//                        System.out.println(ex.getMessage());
//                    }
//                    c.setSpeed(c.speed() / powerup.modifier);
//                }
//            }).start();
//        } else if (powerup.type == PowerupType.INCREASESIZE) {
//
//        } else if (powerup.type == PowerupType.DECREASESIZE) {
//
//        } else if (powerup.type == PowerupType.INVINCIBLE) {
//            (new Thread() {
//                @Override
//                public void run() {
//                    invincible = true;
//                    try {
//                        Thread.sleep(powerup.tijdsduur * 1000);
//                    } catch (Exception ex) {
//                        System.out.println(ex.getMessage());
//                    }
//                    invincible = false;
//                }
//            }).start();
//        } else if (powerup.type == PowerupType.CLEARBOARD) {
//            GraphicsContext gc = gameCanvas.getGraphicsContext2D();
//            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
//            positions.clear();
//            powerups.clear();
//        }
//    }
//
//    private void setup() {
//        Player p = new Player(1, "jeroen", 2, "jeroenh13@live.nl");
//        c = new dontcrash.Character(p, 1);
//    }
//

//
//    @Override
//    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void update(Observable o, Object arg) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//}
//
// 
