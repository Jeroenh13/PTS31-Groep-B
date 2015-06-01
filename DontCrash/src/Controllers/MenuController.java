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
import dontcrash.DontCrash;
import dontcrash.OmdatFXMLControllersMoeilijkDoen;
import dontcrash.Player;
import dontcrash.Room;
import dontcrash.portsAndIps;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 *
 * @author Kitty
 */
public class MenuController  implements Observer, RemotePropertyListener, Initializable
{
    @FXML Button btnCreate;
    @FXML Button btnJoin;
    @FXML Parent root;
    @FXML ScrollPane spRooms;
    
    @FXML TextArea taMessages;
    @FXML TextField txtMessage;
    
    @FXML Label lblWelcomeUser;
    
    ActualChat ac;
    IAdministator admin;
    
    /**
     * Create the RMI connections.
     * @throws IOException if there is a problem withe the ports
     */
    public MenuController() throws IOException {
          
        //makes connection to the admin.
        RMIClient  rmi = new RMIClient(portsAndIps.IP, 1098,"Admin");
        admin = rmi.setUpNewAdministrator();
        try {
            UnicastRemoteObject.exportObject(this, portsAndIps.getNewPort());
            admin.addListener( this, "Room");
        } catch (RemoteException ex) {
            Logger.getLogger(DontCrash.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
        //Makes connection to the chat
        try {
            this.ac = new ActualChat(portsAndIps.IP,portsAndIps.defaultServerPortChat ,portsAndIps.getNewPort() , "deze man");
        }
        catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ac.addObserver(this);
    }
    
    /**
     * creates a new room
     * @param evnt button event
     * @throws RemoteException if the RMI connection fails
     * @throws IOException if there is a problem with the ports.
     */
    public void createNewRoom(Event evnt) throws RemoteException, IOException
    {          
        IRoom newRoom = admin.newRoom(new Player(1,"sg",300,"adsgd"));
        goToCharacterSelect(newRoom.getRoomId());
    }
    
    /**
     * joins an existing room.
     * @param evnt button event
     * @throws IOException if there is a problem with the ports.
     */
    public void joinRoom(Event evnt) throws IOException
    {
        int roomID = Integer.parseInt(txtMessage.getText());
        if(admin.joinRoom(new Player(2,"sgads",330,"adsgdaadfa"), roomID))
            goToCharacterSelect(roomID);
        
    }
    
    /**
     * switches the screen to character select.
     * @throws IOException if the FXML file cannot be found.
     */
    public void goToCharacterSelect(int roomID) throws IOException
    {      
        OmdatFXMLControllersMoeilijkDoen.setRoomID(roomID);
        Stage stage=(Stage) btnCreate.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CharacterSelect.fxml"));
        loader.load();
        root = loader.getRoot();
        Scene scene = new Scene(root);
        ac.removeObserver();
        ac.deleteObserver(this);
        stage.setScene(scene);
        //Shit die toch niet werkt dus leuke class voor gemaakt 
        CharacterScreenController csc = loader.getController();
        csc.setRoomId(roomID);
        stage.show();    
    }
    
    /**
     * Sends a message to the server for the chat
     * @param e onEnterPressEvent
     * @throws RemoteException if RMI Connection fails
     */
    public void sendMessage(Event e) throws RemoteException
    {
        if(txtMessage.getText().isEmpty())
            return;
        ac.sendMessage(txtMessage.getText());
        txtMessage.setText("");
    }

    /**
     * writes text to the TextArea
     * 
     * @param o AC
     * @param o1 string text
     */
    @Override
    public void update(Observable o, Object o1) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                taMessages.appendText(o1.toString() + "\n");
            }
        });
    }

    /**
     * Triggers when there is a new room made.
     * @param evt change event if there is a new room made.
     * @throws RemoteException if the connection to the server fails.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        ArrayList<IRoom> gamerooms = (ArrayList<IRoom>) admin.getRooms();
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
            for (IRoom r:gamerooms)
            {
                Label label = new Label(r.toString());
                
                taMessages.appendText(r.toString()+ "Room created");
            }
        }
    });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
