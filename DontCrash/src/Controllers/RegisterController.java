/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Database.DatabaseManager;
import dontcrash.Administration;
import java.io.IOException;
import java.rmi.RemoteException;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Bas
 */
public class RegisterController {
    @FXML Parent root;
    @FXML Button btnRegister;
    @FXML Button btnBack;
    
    @FXML TextField txtUsername;
    @FXML TextField txtEmail;
    @FXML TextField txtPassword;
    @FXML TextField txtPasswordRepeat;
    
    
    //Database testlabel
    @FXML Label lbTest;
    @FXML Button btnTest;
    DatabaseManager dbm;
    
    Administration admin;
    
    String username;
    String email;
    String password;
    String rPassword;
    
    /**
     * Constructor
     * @throws RemoteException 
     */
    public RegisterController() throws RemoteException
    {
        this.dbm = new DatabaseManager();
        this.admin = new Administration();
    }
    
    /**
     * Handles the register button click
     * Gets values from field and checks if passwords are equal
     * and username already in use
     * If equal and not in use, create new user
     * @param evnt
     * @throws IOException 
     */
    public void btnRegisterClick (Event evnt) throws IOException
    {        
        username = txtUsername.getText();
        email = txtEmail.getText();
        password = txtPassword.getText();
        rPassword = txtPasswordRepeat.getText();
        
        if (password.equals(rPassword))
        {
            if (admin.newPlayer(username, password, email) == null)
                JOptionPane.showMessageDialog(null, "Username already in use");
            else
            {
                JOptionPane.showMessageDialog(null, "Account created! \n You can now login.");
                txtUsername.setText("");
                txtEmail.setText("");
                txtPassword.setText("");
                txtPasswordRepeat.setText("");
            }
        }
        else
            JOptionPane.showMessageDialog(null, "Repeated password is not the same");
    }
    
    /**
     * Handles back button click
     * Returns user to login.fxml
     * @param e
     * @throws IOException 
     */
    public void btnBackClick (Event e) throws IOException 
    {
        Stage stage=(Stage) btnRegister.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}