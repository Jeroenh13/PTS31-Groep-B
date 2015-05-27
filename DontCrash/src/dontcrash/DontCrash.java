/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dontcrash;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Saya
 */
public class DontCrash extends Application
{

     public static void main(String[] args) {
         launch(args);
    }

     @Override
    public void start(Stage primaryStage) {
         try {
            Pane page = (Pane) FXMLLoader.load(DontCrash.class.getResource("/fxml/Login.fxml"));
            //SplitPane gamescreen = FXMLLoader.load(getClass().getResource("/fxml/GameScreen.fxml"));
             
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(DontCrash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
