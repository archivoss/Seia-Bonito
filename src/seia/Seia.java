/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seia;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;
import java.awt.event.KeyListener;
import static java.lang.Character.CONTROL;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Gama
 */
public class Seia extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        BackgroundController bc = new BackgroundController();
        Parent root = FXMLLoader.load(getClass().getResource("Background.fxml"));   
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        stage.setMaximized(true);
        stage.show();
      
    }

    
    public static void main(String[] args) {
        launch(args);
    }
    
}
