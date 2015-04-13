/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dontcrash;

import java.io.IOException;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author Kitty
 */
public class CharacterScreenController 
{
    @FXML Button btnstart;
    @FXML Parent root;
    
    public void Start(Event evnt) throws IOException
    {
        Stage stage=(Stage) btnstart.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/fxml/GameScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }    
}
