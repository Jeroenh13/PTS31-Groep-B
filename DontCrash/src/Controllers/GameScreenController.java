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
    Label lblScore;

    @FXML
    Label lblPlayer1;
    @FXML
    Label lblPlayer2;
    @FXML
    Label lblPlayer3;
    @FXML
    Label lblPlayer4;

    @FXML
    TextField txtChat;
    @FXML
    TextArea taChat;

    private IGame game = null;
    private IRoom room = null;
    private Player player = null;
    private ActualChat ac;

    ArrayList<Point> positions = new ArrayList<>();
    ArrayList<DrawablePowerup> powerups = new ArrayList<>();
    boolean player1 = true;

    private IAdministator admin;
    dontcrash.Character character = null;

    //Powerup stuff
    private final int spawnChancePowerUp = 40; // Between 0 and 10000 chance every tick to spawn powerup
    private boolean invincible = false;
    private boolean isHost;

    public GameScreenController() throws IOException {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        RMIClient rmi = new RMIClient(portsAndIps.IP, portsAndIps.ServerPort, "Admin");
        admin = rmi.setUpNewAdministrator();
        try {
            UnicastRemoteObject.exportObject(this, portsAndIps.getNewPort());
            room = admin.getRoom(LocalVariables.getRoomID());

            this.ac = new ActualChat(portsAndIps.IP, room.getRoomChatPort(), portsAndIps.getNewPort(), LocalVariables.getPlayer().name, "Chat");

            ac.addObserver(this);
            admin.addListener(this, "Room" + room.toString());

            isHost = room.getHost().playerID == LocalVariables.getPlayer().playerID;
            if (isHost) {
                PLAY.setVisible(true);
                admin.startNewGame(room.getRoomId(), gameCanvas.getLayoutX(), gameCanvas.getLayoutY(), gameCanvas.getWidth(), gameCanvas.getHeight());
                room = admin.getRoom(LocalVariables.getRoomID());
                game = room.getCurrentGame();
            } else {
                room = admin.getRoom(LocalVariables.getRoomID());
                PLAY.setVisible(false);
                game = room.getCurrentGame();
            }

            lblScore.setText(String.valueOf(LocalVariables.getScore()));
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
            if (game != null) {
                game.addListener(this, "Game");
            }
            for (Player p : room.getPlayers()) {
                if (p.playerID == LocalVariables.curPlayerID) {
                    player = p;
                }
            }
            player.character = admin.newCharacter(LocalVariables.getRoomID(), player, admin.getNextCharacterID());
            lblScore.setText(String.valueOf(room.getNeededScore()));
            character = admin.newCharacter(LocalVariables.getRoomID(), LocalVariables.getPlayer(), admin.getNextCharacterID());

        } catch (RemoteException ex) {
            Logger.getLogger(CharacterScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
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
                    room = admin.getRoom(LocalVariables.getRoomID());
                    if (player.playerID == (LocalVariables.getPlayer().playerID)) {
                        character = player.character;
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
                            admin.UpdateCharacter(LocalVariables.getRoomID(), character, player);
                        }
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(GameScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @Override
    public void update(Observable o, Object o1) {
        Platform.runLater(() -> {
            taChat.appendText(o1.toString() + "\n");
        });
    }

    /**
     * Sends chat message
     *
     * @param e
     * @throws RemoteException
     */
    public void txtChatSend(Event e) throws RemoteException {
        if (txtChat.getText().isEmpty()) {
            return;
        }
        ac.sendMessage(txtChat.getText());
        txtChat.setText("");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if ("GameOver".equals(evt.getNewValue())) {
                    GraphicsContext gc = gameCanvas.getGraphicsContext2D();
                    gc.clearRect(gameCanvas.getLayoutX(), gameCanvas.getLayoutY(), gameCanvas.getWidth(), gameCanvas.getHeight());
                } else if ("Points".equals((String) evt.getOldValue())) // If it is a arraylist of points
                {
                    //ArrayList<Point> oldPoints = (ArrayList<Point>) evt.getOldValue();
                    ArrayList<Point> newPoints = (ArrayList<Point>) evt.getNewValue();
                    //for (Point op : oldPoints) {
                    for (Point np : newPoints) {
                        //if (op.red == np.red && op.green == np.green && op.blue == np.blue) {
                        draw(np);
                        //}
                    }

                    // }
                } else if ("Powerup".equals((String) evt.getOldValue())) {
                    if ("ClearBoard".equals((String) evt.getNewValue())) {
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                GraphicsContext gc = gameCanvas.getGraphicsContext2D();
                                gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                                powerups.clear();
                            }
                        });

                    }
                    if (evt.getNewValue() instanceof DrawablePowerup) {
                        drawPowerup((DrawablePowerup) evt.getNewValue());
                    } else {
                        ArrayList<DrawablePowerup> powerups = (ArrayList<DrawablePowerup>) evt.getNewValue();
                        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
                        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                        redrawPoints();
                        drawPowerups(powerups);
                    }
                }
            }

        });

    }

    public void draw(Point p) {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.setLineWidth(p.size);
        gc.setStroke(Color.color(p.red, p.green, p.blue));
        gc.strokeOval(p.X, p.Y, 1, 1);
    }

    public void drawPowerup(DrawablePowerup powerup) {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.setStroke(Color.CRIMSON);
        gc.setFill(Color.CRIMSON);
        gc.fillOval(powerup.minX, powerup.minY, powerup.maxX - powerup.minX, powerup.maxY - powerup.minY);
        gc.strokeOval(powerup.minX, powerup.minY, powerup.maxX - powerup.minX, powerup.maxY - powerup.minY);

    }

    public void drawPowerups(ArrayList<DrawablePowerup> powerups) {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        for (DrawablePowerup p : powerups) {
            gc.setStroke(Color.CRIMSON);
            gc.setFill(Color.CRIMSON);
            gc.fillOval(p.minX, p.minY, p.maxX - p.minX, p.maxY - p.minY);
            gc.strokeOval(p.minX, p.minY, p.maxX - p.minX, p.maxY - p.minY);
        }
    }

    public void redrawPoints() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        for (Point p : positions) {
            gc.setLineWidth(p.size);
            gc.setStroke(Color.color(p.red, p.green, p.blue));
            gc.strokeOval(p.X, p.Y, 1, 1);
        }
    }
}

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
