/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seia;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import static java.lang.System.out;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

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
    
    @FXML
    private Region region;
    
    @FXML
    private ImageView display;
    
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
    }
    
    @FXML
    private void drawButtonAction(ActionEvent event) throws AWTException, IOException {
        Robot robot = new Robot();
        Rectangle rec = new Rectangle((int) x + 410, (int) y + 180, (int) currentX, (int) currentY);
        BufferedImage image = robot.createScreenCapture(rec);
        Image myImage = SwingFXUtils.toFXImage(image, null);
        display.setImage(myImage);
        ObjectOutputStream oos = null;
        File imageFile = new File("screen.jpg");
        oos.defaultWriteObject();
        ImageIO.write(image, "png", out); // png is lossless
        String fileName = "pdfWithImage.pdf";
        String imageName = "screen.jpg";
        try {

            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();

            doc.addPage(page);

            PDXObjectImage imagex = new PDJpeg(doc, new FileInputStream(imageName));

            PDPageContentStream content = new PDPageContentStream(doc, page);

            content.drawImage(imagex, 180, 700);

            content.close();

            doc.save(fileName);

            doc.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println("funciono!!!");
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
        currentX = event.getX() - x;
        currentY = event.getY() - y;
    }
    
    @FXML
    private void enter(MouseEvent event){
        addFileButton.setStyle("-fx-background-color: #CCCCCC;");
    }
    
    @FXML
    private void release(MouseEvent event){
        addFileButton.setStyle("-fx-background-color: #FFFFFF;");
    }
    
    @FXML
    private void exit(MouseEvent event){
        addFileButton.setStyle("-fx-background-color: #FFFFFF;");
    }
    
    @FXML
    private void press(MouseEvent event){
        addFileButton.setStyle("-fx-background-color: #BBBBBB;");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
}
