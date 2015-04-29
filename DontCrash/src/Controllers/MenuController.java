/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import dontcrash.ActualChat;
import dontcrash.Player;
import dontcrash.portsAndIps;
import java.io.IOException;
import java.rmi.RemoteException;
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
import javax.swing.JOptionPane;

/**
 *
 * @author Kitty
 */
public class MenuController  implements Observer
{
    @FXML Button btnplay;
    @FXML Parent root;
    
    @FXML TextArea taMessages;
    @FXML TextField txtMessage;
    
    ActualChat ac;

    public MenuController() {
        try {
            this.ac = new ActualChat(portsAndIps.IP,portsAndIps.defaultServerPortChat ,portsAndIps.getNewPort() , "deze man");
        }
        catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ac.addObserver(this);
    }
    
    
    public void Play(Event evnt) throws IOException
    {
        Stage stage=(Stage) btnplay.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/fxml/CharacterSelect.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void sendMessage(Event e) throws RemoteException
    {
        if(txtMessage.getText().isEmpty())
            return;
        ac.sendMessage(txtMessage.getText());
        txtMessage.setText("");
    }

    @Override
    public void update(Observable o, Object o1) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                taMessages.appendText(o1.toString() + "\n");
            }
        });
    }
    
}
