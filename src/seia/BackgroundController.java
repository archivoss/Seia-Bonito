/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seia;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Gama
 */
public class BackgroundController implements Initializable {
    int aux;
    double x;
    double y;
    double auxW;
    double auxH;
    double auxX;
    double auxY;
    String pdfToText;
    Rectangle rec;  
    List<Rectangle> modificarRec;
    List<Rectangle> listRec;
    GraphicsContext gc;
    LeerPdf pdfTextParserObj;
    File archivoSeleccionado;
    JFileChooser seleccionarArchivo;
       
    @FXML
    private Button button;
    
    @FXML
    private Button selectButton;
    
    @FXML
    private Button drawButton;
    
    @FXML
    private Canvas drawPane;
    
    @FXML
    private Canvas contenidoPDF;
    
    @FXML
    private void addFileButtonAction(ActionEvent event) throws IOException {
        seleccionarArchivo = new JFileChooser();
        seleccionarArchivo.showOpenDialog(null);
        archivoSeleccionado = seleccionarArchivo.getSelectedFile(); 
        pdfTextParserObj = new LeerPdf();
        pdfToText = pdfTextParserObj.pdftoText(archivoSeleccionado);
        gc = contenidoPDF.getGraphicsContext2D();
        gc.setFont(Font.font("Monospaced", 24.0));
        gc.setLineWidth(1);
        gc.strokeText(pdfToText, 20, 30); 
        listRec = new ArrayList<>();
        drawButton.setDisable(false);
        selectButton.setDisable(false);
    }
     
    @FXML
    private void selectRectangle(MouseEvent event){
        gc = contenidoPDF.getGraphicsContext2D();
        gc.clearRect(0, 0, 1100, 750);
        gc.setLineWidth(1);    
        gc.setFont(Font.font("Monospaced", 24.0));       
        gc.strokeText(pdfToText, 20, 30); 
        rec = new Rectangle();
        for (int i = 0; i < listRec.size(); i++) {
            gc.strokeRect(listRec.get(i).getX(), listRec.get(i).getY(),
                   listRec.get(i).getWidth(), listRec.get(i).getHeight());
        }
        x = event.getX();
        y = event.getY();
        for (int i = 0; i < listRec.size(); i++) {
            if (listRec.get(i).getY() < y && listRec.get(i).getX() < x &&
                    listRec.get(i).getY() + listRec.get(i).getHeight() > y &&
                    listRec.get(i).getX() + listRec.get(i).getWidth() > x) {
                rec = listRec.get(i);               
            }
        }
        modificarRec = new ArrayList<>();
        modificarRec.add(new Rectangle(rec.getX() - 5, rec.getY() - 5, 10, 10));  
        modificarRec.add(new Rectangle(rec.getX() + rec.getWidth() - 5, rec.getY() - 5, 10, 10));
        modificarRec.add(new Rectangle(rec.getX() - 5, rec.getY() + rec.getHeight() - 5, 10, 10));                   
        modificarRec.add(new Rectangle(rec.getX() + rec.getWidth() - 5, rec.getY() + rec.getHeight() - 5, 10, 10));
        gc.setFill(Color.WHITE);
        for (int i = 0; i < modificarRec.size(); i++) {
            gc.fillRect(modificarRec.get(i).getX(), modificarRec.get(i).getY(),
                   modificarRec.get(i).getWidth(), modificarRec.get(i).getHeight());
            gc.strokeRect(modificarRec.get(i).getX(), modificarRec.get(i).getY(),
                   modificarRec.get(i).getWidth(), modificarRec.get(i).getHeight());        
        }
    }
    
    @FXML
    private void modifyPressed(MouseEvent event){
        gc = drawPane.getGraphicsContext2D();
        if (modificarRec != null) {
            x = event.getX();
            y = event.getY();
            for (int i = 0; i < modificarRec.size(); i++) {
                if (modificarRec.get(i).getY() < y && modificarRec.get(i).getX() < x &&
                        modificarRec.get(i).getY() + modificarRec.get(i).getHeight() > y &&
                        modificarRec.get(i).getX() + modificarRec.get(i).getWidth() > x) {
                    auxW = rec.getWidth();
                    auxH = rec.getHeight();
                    auxX = rec.getX();
                    auxY = rec.getY();
                    aux = i + 1;
                }
            }    
        }    
    }
    
    @FXML
    private void modifyDragged(MouseEvent event){
        switch (aux) {
            case 1:
                System.out.println(1);
                // Abajo derecha
                if (event.getX() > auxX && event.getY() > auxY) {
                    rec.setX(auxX + auxW);
                    rec.setY(auxY + auxH);
                    rec.setWidth(event.getX() - (auxX + auxW));
                    rec.setHeight(event.getY() - (auxY + auxH));             
                    gc.clearRect(0, 0, 1100, 750);
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }   

                // Abajo izquierda
                if (event.getX() <  auxX && event.getY() > auxY) {
                    rec.setX(event.getX());
                    rec.setY(auxY + auxH);
                    rec.setWidth((auxX + auxW) - event.getX());
                    rec.setHeight(event.getY() - (auxY + auxH));
                    gc.clearRect(0, 0, 1100, 750);
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }

                //Arriba izquierda
                if (event.getX() <  auxX && event.getY() < auxY) {
                    rec.setX(event.getX());
                    rec.setY(event.getY());
                    rec.setWidth(auxW + (auxX - event.getX()));
                    rec.setHeight(auxH + (auxY - event.getY()));
                    gc.clearRect(0, 0, 1100, 750);
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }

                //Arriba derecha
                if (event.getX() > auxX && event.getY() < auxY) {
                    rec.setX(auxX + auxW);
                    rec.setY(event.getY());
                    rec.setWidth(event.getX() - (auxX + auxW));
                    rec.setHeight((auxY + auxH) - event.getY());
                    gc.clearRect(0, 0, 1100, 750);
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }

                if (rec.getWidth() < 0 || rec.getHeight() < 0) {   
                    rec.setX(event.getX());
                    rec.setY(event.getY());
                    rec.setWidth((auxW + auxX) - event.getX());
                    rec.setHeight((auxH + auxY) - event.getY()); 
                    if (rec.getWidth() < 0) {
                        rec.setX(auxX + auxW);
                        rec.setWidth(event.getX() - (auxX + auxW));                                             
                        rec.setHeight((auxY + auxH) - event.getY());
                    }
                    else if (rec.getHeight() < 0) {
                        rec.setY(auxH + auxY);
                        rec.setWidth((auxX + auxW) - event.getX());
                        rec.setHeight(event.getY() - (auxY + auxH));
                    }
                    gc.clearRect(0, 0, 1100, 750);
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }
                break;
            case 2:
                System.out.println(2);
                // Abajo derecha
                if (event.getX() > auxX && event.getY() > auxY) {
                    rec.setX(auxX + auxW);
                    rec.setY(auxY + auxH);
                    rec.setWidth(event.getX() - (auxX + auxW));
                    rec.setHeight(event.getY() - (auxY + auxH));             
                    gc.clearRect(0, 0, 1100, 750);
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }   

                // Abajo izquierda
                if (event.getX() <  auxX && event.getY() > auxY) {
                    rec.setX(event.getX());
                    rec.setY(auxY + auxH);
                    rec.setWidth((auxX + auxW) - event.getX());
                    rec.setHeight(event.getY() - (auxY + auxH));
                    gc.clearRect(0, 0, 1100, 750);
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }

                //Arriba izquierda
                if (event.getX() <  auxX && event.getY() < auxY) {
                    rec.setX(event.getX());
                    rec.setY(event.getY());
                    rec.setWidth(auxW + (auxX - event.getX()));
                    rec.setHeight(auxH + (auxY - event.getY()));
                    gc.clearRect(0, 0, 1100, 750);
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }

                //Arriba derecha
                if (event.getX() > auxX && event.getY() < auxY) {
                    rec.setX(auxX + auxW);
                    rec.setY(event.getY());
                    rec.setWidth(event.getX() - (auxX + auxW));
                    rec.setHeight((auxY + auxH) - event.getY());
                    gc.clearRect(0, 0, 1100, 750);
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }

                if (rec.getWidth() < 0 || rec.getHeight() < 0) {   
                    rec.setX(event.getX());
                    rec.setY(event.getY());
                    rec.setWidth((auxW + auxX) - event.getX());
                    rec.setHeight((auxH + auxY) - event.getY()); 
                    if (rec.getWidth() < 0) {
                        rec.setX(auxX + auxW);
                        rec.setWidth(event.getX() - (auxX + auxW));                                             
                        rec.setHeight((auxY + auxH) - event.getY());
                    }
                    else if (rec.getHeight() < 0) {
                        rec.setY(auxH + auxY);
                        rec.setWidth((auxX + auxW) - event.getX());
                        rec.setHeight(event.getY() - (auxY + auxH));
                    }
                    gc.clearRect(0, 0, 1100, 750);
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }
                break;
            case 3:
                System.out.println(3);
                break;
            case 4:
                System.out.println(4);
                break;
            default:
                break;
        }
    }
    
   
    
    @FXML
    private void drawButtonAction(ActionEvent event){
        drawPane.toFront();
        gc = contenidoPDF.getGraphicsContext2D();
        gc.clearRect(0, 0, 1100, 750);
        gc.setLineWidth(1);    
        gc.setFont(Font.font("Monospaced", 24.0));       
        gc.strokeText(pdfToText, 20, 30); 
        for (int i = 0; i < listRec.size(); i++) {
            gc.strokeRect(listRec.get(i).getX(), listRec.get(i).getY(),
                   listRec.get(i).getWidth(), listRec.get(i).getHeight());
        }
        contenidoPDF.setDisable(true);
        if (drawPane.disableProperty().getValue()) {
            drawPane.setDisable(false);
        }
        else{
            drawPane.setDisable(true);
        }
        if (!selectButton.getStyle().equals("-fx-background-color: #FFFFFF")) {
            selectButton.setStyle("-fx-background-color: #FFFFFF");
        }  
    }     
    
    @FXML
    private void selectButtonAction(ActionEvent event){
        contenidoPDF.toFront();
        drawPane.setDisable(true);
        if (contenidoPDF.disableProperty().getValue()) {
            contenidoPDF.setDisable(false);
        }
        else{
            contenidoPDF.setDisable(true);
        }
        if (!drawButton.getStyle().equals("-fx-background-color: #FFFFFF")) {
            drawButton.setStyle("-fx-background-color: #FFFFFF");
        }
    }    
    
    @FXML
    private void drawPressed(MouseEvent event) {
        x = event.getX();
        y = event.getY();       
    }
    
    @FXML
    private void drawDragged(MouseEvent event) {  
        gc = drawPane.getGraphicsContext2D();
        // Abajo derecha
        if (event.getX() > x && event.getY() > y) {
            gc.clearRect(0, 0, 1100, 750);
            gc.strokeRect(x, y, event.getX() - x, event.getY() - y);
        }   
        
        // Abajo izquierda
        if (event.getX() <  x && event.getY() > y) {
            gc.clearRect(0, 0, 1100, 750);        
            gc.strokeRect(x - (x - event.getX()), y, x - event.getX(), event.getY() - y);
        }
        
        //Arriba izquierda
        if (event.getX() <  x && event.getY() < y) {
            gc.clearRect(0, 0, 1100, 750);
            gc.strokeRect(event.getX(), event.getY(), x - event.getX(), y - event.getY());
        }
        
        //Arriba derecha
        if (event.getX() > x && event.getY() < y) {
            gc.clearRect(0, 0, 1100, 750);
            gc.strokeRect(x, y - (y - event.getY()), event.getX() - x, y - event.getY());
        }
    }
    
    @FXML
    private void drawReleased(MouseEvent event) {
        gc = contenidoPDF.getGraphicsContext2D();   
        // Abajo derecha
        rec = null;
        if (event.getX() > x && event.getY() > y) {
            gc.strokeRect(x, y, event.getX() - x, event.getY() - y);
            rec = new Rectangle(x, y, event.getX() - x, event.getY() - y);
        }   
        
        // Abajo izquierda
        else if (event.getX() <  x && event.getY() > y) {      
            gc.strokeRect(x - (x - event.getX()), y, x - event.getX(), event.getY() - y);
            rec = new Rectangle(x - (x - event.getX()), y, x - event.getX(), event.getY() - y);
        }
        
        //Arriba izquierda
        else if (event.getX() <  x && event.getY() < y) {
            gc.strokeRect(event.getX(), event.getY(), x - event.getX(), y - event.getY());
            rec = new Rectangle(event.getX(), event.getY(), x - event.getX(), y - event.getY());
        }
        
        //Arriba derecha
        else if (event.getX() > x && event.getY() < y) {
            gc.strokeRect(x, y - (y - event.getY()), event.getX() - x, y - event.getY());
            rec = new Rectangle(x, y - (y - event.getY()), event.getX() - x, y - event.getY());
        }
        listRec.add(rec);
    }
    
    @FXML
    private void enter(MouseEvent event){
        button = (Button) event.getPickResult().getIntersectedNode();
        if (!button.getId().equals("drawButton") && !button.getId().equals("selectButton")) {
            button.setStyle("-fx-background-color: #CCCCCC;");
        }
        else{
            if (!button.getStyle().equals("-fx-background-color: #8b008b")) {
                button.setStyle("-fx-background-color: #CCCCCC");
            }
        }
    }
    
    @FXML
    private void release(MouseEvent event){
        if (!button.getId().equals("drawButton") && !button.getId().equals("selectButton")) {
            button.setStyle("-fx-background-color: #CCCCCC;");
        }
        else{
            if (!button.getStyle().equals("-fx-background-color: #8b008b")) {
                button.setStyle("-fx-background-color: #CCCCCC");
            }
        }
    }
    
    @FXML
    private void exit(MouseEvent event){
        if (!button.getId().equals("drawButton") && !button.getId().equals("selectButton")) {
            button.setStyle("-fx-background-color: #FFFFFF;");
        }
        else{
            if (!button.getStyle().equals("-fx-background-color: #8b008b")) {
                button.setStyle("-fx-background-color: #FFFFFF");
            }
        }
    }
    
    @FXML
    private void press(MouseEvent event){
        if (button.getId().equals("drawButton") || button.getId().equals("selectButton")) {
            if (button.getStyle().equals("-fx-background-color: #8b008b")) {
                button.setStyle("-fx-background-color: #FFFFFF");
            }
            else{
                button.setStyle("-fx-background-color: #8b008b");
            }  
        }
        else{
            button.setStyle("-fx-background-color: #AAAAAA;");
        }       
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
