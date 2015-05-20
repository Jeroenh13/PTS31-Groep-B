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
import javafx.application.Platform;
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
public class registerController {
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
    
    String Username;
    String Email;
    String Password;
    String RPassword;
    
    public registerController() throws RemoteException
    {
        this.dbm = new DatabaseManager();
        this.admin = new Administration();
    }
    
    public void btnRegisterClick (Event evnt) throws IOException
    {        
        Username = txtUsername.getText();
        Email = txtEmail.getText();
        Password = txtPassword.getText();
        RPassword = txtPasswordRepeat.getText();
        
        if (Password.equals(RPassword))
        {
            if (admin.newPlayer(Username, Password, Email) == null)
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
    
    public void btnBackClick (Event e) throws IOException 
    {
        Stage stage=(Stage) btnRegister.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void TestDatabase(Event e)
    {
        String result;
        dbm.OpenConn();
        result = dbm.TestCon();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lbTest.setText(result);
            }
        });
        dbm.CloseConn();
    }
}
