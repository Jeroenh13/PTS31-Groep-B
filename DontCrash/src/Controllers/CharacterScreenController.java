/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import dontcrash.ActualChat;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;
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
public class CharacterScreenController implements Observer
{
    @FXML Button btnstart;
    @FXML Parent root;
    
    @FXML TextArea taChat;
    @FXML TextField txtChat;
    
    private ActualChat ac;
    
    /**
     * Constructor
     */
    public CharacterScreenController()
    {
        //Dit er misschien nog bij?
        //ac = new ActualChat("zooi", "zooi", "zooi", "zooi");
   /*     try {
            this.ac = new ActualChat(portsAndIps.IP,portsAndIps.defaultServerPortChat ,portsAndIps.getNewPort() , "deze man");
        }
        catch (IOException ex) {
            Logger.getLogger(CharacterScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ac.addObserver(this);*/
    
    }
    
    
    /**
     * Starts the game and displays the GameScreen.fxml
     * @param evnt
     * @throws IOException 
     */
    public void Start(Event evnt) throws IOException
    {
        Stage stage=(Stage) btnstart.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/fxml/GameScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }    

    /**
     * Sends chat message
     * @param e
     * @throws RemoteException 
     */
    public void txtChatSend(Event e) throws RemoteException
    {
        if(txtChat.getText().isEmpty())
            return;
        ac.sendMessage(txtChat.getText());
        txtChat.setText("");
    }
    
    /**
     * Update method. Appends text to chatbox
     * @param o
     * @param o1 
     */
    @Override
    public void update(Observable o, Object o1) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                taChat.appendText(o1.toString() + "\n");
            }
        });
    }
}
