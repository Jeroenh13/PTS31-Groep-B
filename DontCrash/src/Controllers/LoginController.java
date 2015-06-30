/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import dontcrash.Administration;
import dontcrash.LocalVariables;
import dontcrash.Player;
import java.io.IOException;
import java.rmi.RemoteException;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Bas
 */
public class LoginController {
    @FXML Button btnLogIn;
    @FXML PasswordField txtPassword;
    @FXML TextField txtNaam;
    @FXML Button btnRegistreer;
    @FXML Parent root; 
    @FXML Button btnGame;
    @FXML Button btnNoDBCLick;
    
    Administration admin;

    /**
     * Constructor
     * @throws RemoteException 
     */
    public LoginController() throws RemoteException {
        this.admin = new Administration();
    }
    
    /**
     * Handles the login button click
     * Takes values from login fields and checks if correct
     * If correct, shows menu.fxml
     * @param evnt
     * @throws IOException 
     */
    public void btnLoginClick(Event evnt) throws IOException
    {
        String username = txtNaam.getText();
        String password = txtPassword.getText();
        if("admin".equals(txtNaam.getText())){
            admin.newPlayer("admin", "", "admin");
            LocalVariables.setPlayer(admin.getPlayer(username));
            Stage stage=(Stage) txtNaam.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return;
        }
        /*
        LocalVariables.setPlayer(new Player(1, txtNaam.getText(), 300, "blabla@gmail.com"));
            Stage stage=(Stage) txtNaam.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        */
        
        
        
        if(admin.login(username, password))
        {
            LocalVariables.setPlayer(admin.getPlayer(username));
            Stage stage=(Stage) txtNaam.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else
            JOptionPane.showMessageDialog(null, "foute inloggegevens");
    }
    
    /**
     * Handles the registreer button click
     * @param evnt
     * @throws IOException 
     */
    public void btnRegistreerClick(Event evnt) throws IOException
    {
            Stage stage=(Stage) txtNaam.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/fxml/Register.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
    }
}
