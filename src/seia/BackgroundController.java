/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seia;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

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
    VisualizarPDF p;
    String pdfToText;
    Rectangle rec;  
    PagePanel panelPDF;
    List<Rectangle> modificarRec;
    List<Rectangle> listRec;
    GraphicsContext gc;
    PDFPage PDF;
    PDFFile pdfFile;
    File archivoSeleccionado;
    JFileChooser seleccionarArchivo;
    Stack<List<Rectangle>> stackundo = new Stack<>();
    Stack<List> stackredo = new Stack<>();
    
    int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
          
    @FXML
    private Button button;
    
    @FXML
    private Button selectButton;
    
    @FXML
    private Button drawButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Canvas drawPane;
    
    @FXML
    private Canvas contenidoPDF;
    
    
    
    @FXML
    private void undoButtonAction(ActionEvent event){
        if (!stackundo.isEmpty()){
            gc = contenidoPDF.getGraphicsContext2D();
            gc.clearRect(0, 0, contenidoPDF.getWidth(), contenidoPDF.getHeight());
            gc = drawPane.getGraphicsContext2D();
            gc.clearRect(0, 0, contenidoPDF.getWidth(), contenidoPDF.getHeight());
            System.out.println(listRec);
            stackundo.pop();
            System.out.println(stackundo.pop());
            for (int k = 0; k < stackundo.size(); k++) {
                gc.strokeRect(stackundo.get(k).get(k).getX(), stackundo.get(k).get(k).getY(),
                stackundo.get(k).get(k).getWidth(), stackundo.get(k).get(k).getHeight());
                
            }  
            
              
                 
        }
        else{
            System.out.println("vacio");
        }
    }
    @FXML
    private void redoButtonAction(ActionEvent event){
       
    }
    
    @FXML
    private void addFileButtonAction(ActionEvent event) throws IOException {
        seleccionarArchivo = new JFileChooser();
        seleccionarArchivo.showOpenDialog(null);
        archivoSeleccionado = seleccionarArchivo.getSelectedFile(); 
        p = new VisualizarPDF(archivoSeleccionado);
        p.setDefaultCloseOperation(EXIT_ON_CLOSE);
        p.setVisible(true);
        p.setBounds(0, 0, 500, 500);
        p.setLocationRelativeTo(null);
        listRec = new ArrayList<>();
        drawButton.setDisable(false);
        selectButton.setDisable(false);
        deleteButton.setDisable(false);
    }
    
    @FXML
    private void deleteButtonAction(ActionEvent event){
        if (rec != null) {
            for (int i = 0; i < listRec.size(); i++) {
                if (rec.equals(listRec.get(i))) {           
                    listRec.remove(rec);
                }
            }
            gc = drawPane.getGraphicsContext2D();
            gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);
            gc = contenidoPDF.getGraphicsContext2D();
            gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);
            gc.setLineWidth(1);    
            for (int i = 0; i < listRec.size(); i++) {
                gc.strokeRect(listRec.get(i).getX(), listRec.get(i).getY(),
                listRec.get(i).getWidth(), listRec.get(i).getHeight());
            }
        }   
    }
     
    @FXML
    private void selectRectangle(MouseEvent event){
        
        modificarRec = new ArrayList<>();
        gc = contenidoPDF.getGraphicsContext2D();
        gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);
        gc.setLineWidth(1);    
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
                modificarRec.add(new Rectangle(rec.getX() - 5, rec.getY() - 5, 10, 10));  
                modificarRec.add(new Rectangle(rec.getX() + rec.getWidth() - 5, rec.getY() - 5, 10, 10));
                modificarRec.add(new Rectangle(rec.getX() - 5, rec.getY() + rec.getHeight() - 5, 10, 10));                   
                modificarRec.add(new Rectangle(rec.getX() + rec.getWidth() - 5, rec.getY() + rec.getHeight() - 5, 10, 10));
                gc.setFill(Color.WHITE);
                for (int j = 0; j < modificarRec.size(); j++) {
                    gc.fillRect(modificarRec.get(j).getX(), modificarRec.get(j).getY(),
                    modificarRec.get(j).getWidth(), modificarRec.get(j).getHeight());
                    gc.strokeRect(modificarRec.get(j).getX(), modificarRec.get(j).getY(),
                    modificarRec.get(j).getWidth(), modificarRec.get(j).getHeight());        
                }
            }
        }
        
        
    }
    
    @FXML
    private void modifyPressed(MouseEvent event){
        gc = drawPane.getGraphicsContext2D();
        aux = 0;
        if (modificarRec != null) {
            x = event.getX();
            y = event.getY();
            for (int i = 0; i < modificarRec.size(); i++) {
                auxW = rec.getWidth();
                auxH = rec.getHeight();
                auxX = rec.getX();
                auxY = rec.getY();
                if (modificarRec.get(i).getY() < y && modificarRec.get(i).getX() < x &&
                        modificarRec.get(i).getY() + modificarRec.get(i).getHeight() > y &&
                        modificarRec.get(i).getX() + modificarRec.get(i).getWidth() > x) {
                    aux = i + 1;
                }
                else if (auxY < y && auxX < x && auxY + auxH > y && auxX + auxW > x){
                    aux = 5;
                }
            }
        }    
    }
    
    @FXML
    private void modifyReleased(MouseEvent event){
        System.out.println("pene");
    }
    
    @FXML
    private void modifyDragged(MouseEvent event){
        switch (aux) {
            case 1:
                rec.setX(event.getX());
                rec.setY(event.getY());
                rec.setWidth((auxX + auxW) - event.getX());
                rec.setHeight((auxY + auxH) - event.getY());             
                gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);
                gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                if (rec.getWidth() < 0 || rec.getHeight() < 0) {   
                    rec.setX(auxX + auxW);
                    rec.setY(event.getY());
                    rec.setWidth(event.getX() - (auxX + auxW));
                    rec.setHeight((auxY + auxH) - event.getY()); 
                    if (rec.getWidth() < 0) {
                        rec.setX(event.getX());
                        rec.setY(auxY + auxH);
                        rec.setWidth((auxX + auxW) - event.getX());                                             
                        rec.setHeight(event.getY() - (auxY + auxH));
                    }
                    else if (rec.getHeight() < 0) {
                        rec.setX(auxX + auxW);
                        rec.setY(auxY + auxH);
                        rec.setWidth(event.getX() - (auxX + auxW));                                             
                        rec.setHeight(event.getY() - (auxY + auxH));
                    }
                    gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }
                
                break;
            case 2:
                rec.setX(auxX);
                rec.setY(event.getY());
                rec.setWidth(event.getX() - auxX);
                rec.setHeight((auxY + auxH) - event.getY());             
                gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);
                gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());                
                if (rec.getWidth() < 0 || rec.getHeight() < 0) {   
                    rec.setX(event.getX());
                    rec.setY(event.getY());
                    rec.setWidth(auxX - event.getX());
                    rec.setHeight((auxH + auxY) - event.getY()); 
                    if (rec.getWidth() < 0) {
                        rec.setX(auxX);
                        rec.setY(auxY + auxH);
                        rec.setWidth(event.getX() - auxX);                                             
                        rec.setHeight(event.getY() - (auxY + auxH));
                    }
                    else if (rec.getHeight() < 0) {
                        rec.setY(auxH + auxY);
                        rec.setHeight(event.getY() - (auxY + auxH));
                    }
                    gc.clearRect(0, 0,screenWidth - 275, screenHeight - 135);
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }
                break;
            case 3:
                rec.setX(event.getX());
                rec.setY(auxY);
                rec.setWidth((auxX + auxW) - event.getX());
                rec.setHeight(event.getY() - auxY);             
                gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);
                gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                if (rec.getWidth() < 0 || rec.getHeight() < 0) {   
                    rec.setX(auxX + auxW);
                    rec.setY(auxY);
                    rec.setWidth(event.getX() - (auxX + auxW));
                    rec.setHeight(event.getY() - auxY);
                    if (rec.getWidth() < 0) {
                        rec.setX(event.getX());
                        rec.setY(event.getY());
                        rec.setWidth((auxX + auxW) - event.getX());                                             
                        rec.setHeight(auxY - event.getY());
                    }
                    else if (rec.getHeight() < 0) {
                        rec.setX(auxX + auxW);
                        rec.setY(event.getY());
                        rec.setWidth(event.getX() - (auxX + auxW));
                        rec.setHeight(auxY - event.getY());                     
                    }
                    gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }
                break;
            case 4:
                rec.setX(auxX);
                rec.setY(auxY);
                rec.setWidth(event.getX() - auxX);
                rec.setHeight(event.getY() - auxY);             
                gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);
                gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                if (rec.getWidth() < 0 || rec.getHeight() < 0) {   
                    rec.setX(auxX);
                    rec.setY(event.getY());
                    rec.setWidth(event.getX() - auxX);
                    rec.setHeight(auxY - event.getY());
                    if (rec.getWidth() < 0) {
                        rec.setX(event.getX());
                        rec.setY(event.getY());
                        rec.setWidth(auxX - event.getX());                                             
                        rec.setHeight(auxY - event.getY());
                    }
                    if (rec.getHeight() < 0) {
                        rec.setX(event.getX());
                        rec.setY(auxY);
                        rec.setWidth(auxX - event.getX());
                        rec.setHeight(event.getY() - auxY);                     
                    }
                    gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }
                break;
            case 5:
                for (int i = 0; i < listRec.size(); i++) {
                    if (auxY < y && auxX < x && auxY + auxH > y && auxX + auxW > x) {
                        //arriba izquierda
                        if (event.getX() <= x && event.getY() <= y) {
                            rec.setX(auxX - (x - event.getX()));
                            rec.setY(auxY - (y - event.getY()));
                        }
                        
                        //arriba derecha
                        if (event.getX() >= x && event.getY() <= y) {
                            rec.setX(auxX + (event.getX() - x));
                            rec.setY(auxY - (y - event.getY()));
                        }
                        
                        //abajo izquierda
                        if (event.getX() <= x && event.getY() >= y) {
                            rec.setX(auxX - (x - event.getX()));
                            rec.setY(auxY + (event.getY() - y));
                        }
                                                                      
                        //abajo derecha
                        if (event.getX() >= x && event.getY() >= y) {
                            rec.setX(auxX + (event.getX() - x));
                            rec.setY(auxY + (event.getY() - y));
                        }
                        gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);
                        gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());           
                    }
                }
            default:
                break;
        }
    }
    
   
    
    @FXML
    private void drawButtonAction(ActionEvent event){
        drawPane.toFront();
        gc = contenidoPDF.getGraphicsContext2D();
        gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);
        gc.setLineWidth(1);
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
            gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);
            gc.strokeRect(x, y, event.getX() - x, event.getY() - y);
        }   
        
        // Abajo izquierda
        if (event.getX() <  x && event.getY() > y) {
            gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);        
            gc.strokeRect(x - (x - event.getX()), y, x - event.getX(), event.getY() - y);
        }
        
        //Arriba izquierda
        if (event.getX() <  x && event.getY() < y) {
            gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);
            gc.strokeRect(event.getX(), event.getY(), x - event.getX(), y - event.getY());
        }
        
        //Arriba derecha
        if (event.getX() > x && event.getY() < y) {
            gc.clearRect(0, 0, screenWidth - 275, screenHeight - 135);
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
        stackundo.push(listRec);
        rec = null;
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
        
        // Paneles ajustables al tamaño de la pantalla.
        drawPane.setWidth(screenWidth - 275);
        drawPane.setHeight(screenHeight - 135);
        contenidoPDF.setWidth(screenWidth - 275);
        contenidoPDF.setHeight(screenHeight - 135);
    }    
    
}
