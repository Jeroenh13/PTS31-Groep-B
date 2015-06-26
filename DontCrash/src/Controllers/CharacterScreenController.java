/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import RMI.RMIClient;
import RemoteObserver.RemotePropertyListener;
import SharedInterfaces.IAdministator;
import SharedInterfaces.IRoom;
import dontcrash.ActualChat;
import dontcrash.OmdatFXMLControllersMoeilijkDoen;
import dontcrash.Player;
import dontcrash.portsAndIps;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Kitty
 */
public class CharacterScreenController implements Observer, RemotePropertyListener {

    @FXML
    Button btnstart;
    @FXML
    Button btnLeaveGame;
    @FXML
    Parent root;

    @FXML
    TextArea taChat;
    @FXML
    TextField txtChat;
    @FXML
    TextField txtScore;

    private ActualChat ac;
    private IRoom room;
    private IAdministator admin;

    private int roomID;
    //private int roomNeededScore;

    /**
     * Creates a new CharacterScreenController to select a character etc. also
     * new chat will be initialized
     *
     * @throws java.io.IOException IOException on ports
     */
    public CharacterScreenController() throws IOException {
        RMIClient rmi = new RMIClient(portsAndIps.IP, 1096, "Admin");
        admin = rmi.setUpNewAdministrator();
        try {
            UnicastRemoteObject.exportObject(this, portsAndIps.getNewPort());
        } catch (RemoteException ex) {
            Logger.getLogger(CharacterScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        room = admin.getRoom(OmdatFXMLControllersMoeilijkDoen.getRoomID());
        try {
            this.ac = new ActualChat(portsAndIps.IP, room.getRoomChatPort(), portsAndIps.getNewPort(), OmdatFXMLControllersMoeilijkDoen.getPlayer().name, "Chat");
        } catch (IOException ex) {
            Logger.getLogger(CharacterScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ac.addObserver(this);
        admin.addListener(this, "Room" + room.toString());
    }

    /**
     * Starts the game and displays the GameScreen.fxml
     *
     * @param evnt
     * @throws IOException
     */
    public void Start(Event evnt) throws IOException {
        OmdatFXMLControllersMoeilijkDoen.setScoreNeeded(Integer.parseInt(txtScore.getText()));
        Stage stage = (Stage) btnstart.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/fxml/GameScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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

    /**
     * Update method. Appends text to chat
     *
     * @param o
     * @param o1
     */
    @Override
    public void update(Observable o, Object o1) {
        Platform.runLater(() -> {
            taChat.appendText(o1.toString() + "\n");
        });
    }

    /**
     * sets the roomID
     *
     * @param roomId roomID to be set.
     */
    public void setRoomId(int roomId) {
        this.roomID = roomId;
    }

    /**
     * Kicks player from the room.
     * @param evt
     * @throws RemoteException 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        try {
            leaveRoom();
        } catch (IOException ex) {
            Logger.getLogger(CharacterScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Removes a player from the room.
     * @param evt button event
     * @throws RemoteException 
     * @throws IOException 
     */
    public void btnLeaveGameClick(Event evt) throws RemoteException, IOException {
        boolean succes = admin.leaveGame(OmdatFXMLControllersMoeilijkDoen.getPlayer(), roomID);
        if (succes) {
            leaveRoom();
        }
    }

    /**
     * Returns player to the menu
     * @throws IOException 
     */
    private void leaveRoom() throws IOException {

        admin.removeListener(this, "Room" + room.toString());
        ac.removeObserver();
        ac.deleteObservers();
        Platform.runLater(() -> {
            Stage stage = (Stage) btnstart.getScene().getWindow();
            try {
                root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(CharacterScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });

    }
   
}
