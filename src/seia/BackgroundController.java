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
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * FXML Controller class
 *
 * @author Gama
 */
public class BackgroundController implements Initializable {
    String pdfToText;
    File archivoSeleccionado;
    JFileChooser seleccionarArchivo;
    GraphicsContext gc;
    double x;
    double y;
    double currentX;
    double currentY;
    ArrayList <Rectangulo> rectangulos;

    @FXML
    private Button addFileButton;
    
    @FXML
    private Canvas drawPane;
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private Canvas contenidoPDF;
    
    @FXML
    private Button drawButton;
    
     //Necesario para borrar un rectangulo
    @FXML 
    private Button borrarbtn;
    
    @FXML 
    private TextField borrarText;
    
    
    @FXML
    private void borrarR(ActionEvent event){
        
        String rec = borrarText.getText();
        if(!rec.equals(null)){
            int borrar = parseInt(rec);
            Rectangulo n = rectangulos.get(borrar-1);
            if(rectangulos.contains(n)){
                rectangulos.remove(n);
            }
            //IMPRESION
            
            //gc.clearRect(n.getX(), n.getY(), n.getAncho(), n.getAlto());
        }
        
    }
    
    @FXML
    private void addFileButtonAction(ActionEvent event) throws IOException {
        seleccionarArchivo = new JFileChooser();
        seleccionarArchivo.showOpenDialog(null);
        archivoSeleccionado = seleccionarArchivo.getSelectedFile(); 
        LeerPdf pdfTextParserObj = new LeerPdf();
        pdfToText = pdfTextParserObj.pdftoText(archivoSeleccionado);
        gc = contenidoPDF.getGraphicsContext2D();
        gc.setFont(Font.font("Monospaced", 24.0));
        gc.setLineWidth(1);
        gc.strokeText(pdfToText, 20, 30);
        gc = drawPane.getGraphicsContext2D();
        rectangulos = new ArrayList();
    }
    
    @FXML
    private void drawButtonAction(ActionEvent event) {
        
    }
    
    @FXML
    private void drawPressed(MouseEvent event) {
        gc.setLineWidth(1);
        x = event.getX();
        y = event.getY();  
        
    }
    
    @FXML
    private void drawDragged(MouseEvent event) {  
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
    private void enter(MouseEvent event){
        //addFileButton.setStyle("-fx-background-color: #CCCCCC;");
        addFileButton.setTextFill(WHITE);
    }
    
    @FXML
    private void release(MouseEvent event){
        //addFileButton.setStyle("-fx-background-color: #FFFFFF;");
        addFileButton.setStyle("-fx-background-color: #43B581;");
        Color c = Color.rgb(54,57,63);
        addFileButton.setTextFill(c);
        
        
    }
    
    @FXML
    private void exit(MouseEvent event){
        //addFileButton.setStyle("-fx-background-color: #FFFFFF;");
        Color c = Color.rgb(54,57,63);
        addFileButton.setTextFill(c);
    }
    
    @FXML
    private void press(MouseEvent event){
        //addFileButton.setStyle("-fx-background-color: #BBBBBB;");
        addFileButton.setStyle("-fx-background-color: #B22222;");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }   
    class Rectangulo{
        double x, y, ancho, alto;
        
        public Rectangulo(){
            
        }
        public Rectangulo(double x, double y, double ancho , double alto){
            this.x = x;
            this.y = y;
            this.ancho = ancho; 
            this.alto = alto;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getAncho() {
            return ancho;
        }

        public void setAncho(double ancho) {
            this.ancho = ancho;
        }

        public double getAlto() {
            return alto;
        }

        public void setAlto(double alto) {
            this.alto = alto;
        }
        
    }
    
}
