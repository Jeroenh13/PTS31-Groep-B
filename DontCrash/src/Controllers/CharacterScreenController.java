/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import RMI.RMIClient;
import RemoteObserver.BasicPublisher;
import RemoteObserver.RemotePropertyListener;
import RemoteObserver.RemotePublisher;
import SharedInterfaces.IAdministator;
import SharedInterfaces.IRoom;
import dontcrash.ActualChat;
import dontcrash.LocalVariables;
import dontcrash.Player;
import dontcrash.portsAndIps;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Kitty
 */
public class CharacterScreenController implements Observer, RemotePropertyListener, Initializable {

    @FXML
    Button btnstart;
    @FXML
    Button btnLeaveGame;
    @FXML
    Parent root;
    @FXML
    Label Rondes;

    @FXML
    TextArea taChat;
    @FXML
    TextField txtChat;
    @FXML
    TextField txtRondes;

    private ActualChat ac;
    private IRoom room;
    private IAdministator admin;
    private boolean isHost;

    private int roomID;
    //private int roomNeededScore;

    /**
     * Creates a new CharacterScreenController to select a character etc. also
     * new chat will be initialized
     *
     * @throws java.io.IOException IOException on ports
     */
    public CharacterScreenController() throws IOException {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnstart.setDisable(true);
        txtRondes.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.matches("^[0-9]+$")){
                    btnstart.setDisable(false);
                }
                else{
                    btnstart.setDisable(true);
                }
            }
        });
        
        try {
            RMIClient rmi = new RMIClient(portsAndIps.IP, portsAndIps.ServerPort, "Admin");
            admin = rmi.setUpNewAdministrator();

            UnicastRemoteObject.exportObject(this, portsAndIps.getNewPort());

            room = admin.getRoom(LocalVariables.getRoomID());
            this.ac = new ActualChat(portsAndIps.IP, room.getRoomChatPort(), portsAndIps.getNewPort(), LocalVariables.getPlayer().name, "Chat");

            ac.addObserver(this);
            admin.addListener(this, "Room" + room.toString());
            setRoomId(room.getRoomId());
            isHost = room.getHost().playerID == LocalVariables.getPlayer().playerID;
            if (!isHost) {
                txtRondes.setVisible(false);
                Rondes.setVisible(false);
                btnstart.setVisible(false);
                admin.addListener(this, "CharSelect");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(CharacterScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CharacterScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Starts the game and displays the GameScreen.fxml
     *
     * @param evnt
     * @throws IOException
     */
    public void Start(Event evnt) throws IOException {
        gotoGame(true);
    }

    public void gotoGame(boolean host) throws IOException {
        if (host) {
            admin.AdminInform("CharSelect", null, "Start");
            LocalVariables.setScoreNeeded(Integer.parseInt(txtRondes.getText()));
        }

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
     *
     * @param evt
     * @throws RemoteException
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        if (evt.getNewValue().equals("Start")) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        gotoGame(false);
                    } catch (IOException ex) {
                        Logger.getLogger(CharacterScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } else {
            try {
                leaveRoom();

            } catch (IOException ex) {
                Logger.getLogger(CharacterScreenController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Removes a player from the room.
     *
     * @param evt button event
     * @throws RemoteException
     * @throws IOException
     */
    public void btnLeaveGameClick(Event evt) throws RemoteException, IOException {
        boolean succes = admin.leaveGame(LocalVariables.getPlayer(), roomID);
        if (succes) {
            leaveRoom();
        }
    }

    /**
     * Returns player to the menu
     *
     * @throws IOException
     */
    private void leaveRoom() throws IOException {

        try {
            admin.removeListener(this, "Room" + room.toString());
            ac.removeObserver();
            ac.deleteObservers();
            Platform.runLater(() -> {
                Stage stage = (Stage) btnstart.getScene().getWindow();
                try {
                    root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));

                } catch (IOException ex) {

                }
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            });
        } catch (Exception e) {
        }
    }
}
