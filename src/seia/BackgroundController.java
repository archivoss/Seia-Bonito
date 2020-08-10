/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seia;



import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.AWTException; 
import java.awt.Rectangle; 
import java.awt.Robot; 
import java.awt.image.BufferedImage; 
import java.io.IOException; 
import java.io.File; 
import javafx.scene.control.TabPane;
import javax.imageio.ImageIO; 

/**
 * FXML Controller class
 *
 * @author Gama
 */
public class BackgroundController implements Initializable {
    int aux;
    int num = 0;
    double x;
    double y;
    double auxW;
    double auxH;
    double auxX;
    double auxY;
    BufferedImage bim = null;
    String pdfToText;
    String url;
    String texto;
    Rectangle rec;  
    List<Rectangle> modificarRec;
    ArrayList<Rectangle> listRec;
    List<BufferedImage> pagina;
    ArrayList<String> nombres;
    GraphicsContext gc;
    File archivoSeleccionado;
    JFileChooser seleccionarArchivo;
    Stack stackundo = new Stack<>();
    Stack stackredo = new Stack<>();
    ToString string;
    JsonRec jsonFile;

    int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
    
    
    @FXML
    private Label plantillaLabel;
    
    @FXML
    private Label nameRec;
    
    @FXML
    private AnchorPane tamañoPDF;
    
    @FXML
    private AnchorPane eliminarPane;
    
    @FXML
    private AnchorPane panelTexto;
    
    @FXML
    private TextArea extraerTexto;
    
    @FXML
    private AnchorPane savePane;
    
    @FXML
    private StackPane panelPDF;
          
    @FXML
    private Button button;
    
    @FXML
    private Button RecTextButton;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button extraer;
    
    @FXML
    private Button undobtn;
    
    @FXML
    private Button redobtn;
    
    @FXML
    private Button selectButton;
    
    @FXML
    private Button drawButton;
    
    @FXML
    private Button acceptButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Button disableButton;
    
    @FXML
    private Canvas drawPane;
    
    @FXML
    private Canvas contenidoPDF;
    
    @FXML
    private TextField fileName;
    
    @FXML
    private TextField nombreRec;
    
    @FXML
    private Button cargaButton;
    
    @FXML
    private Button sobreEscritura;
    
    @FXML
    private Button borrarButton;
    
    @FXML
    private AnchorPane savePane1;
    
    @FXML
    private Button acptButonnP;
    
    @FXML
    private Button cancelButtonP;
    
    @FXML
    private TabPane tabPane;
    
    @FXML
    private void RecTextButtonAction(ActionEvent event){
        ArrayList<String> contenido = new ArrayList();
        ArrayList<String> nameRec = new ArrayList();   
        try { 
            Robot r = new Robot(); 
            String path = "ScreenShot\\text.png"; 
            Rectangle capture;
            for (int i = 0; i < listRec.size(); i++) {
                capture = new Rectangle(listRec.get(i));
                capture.x = (int) (capture.getX() + 240);
                capture.y = (int) (capture.getY() + 75);
                BufferedImage Image = r.createScreenCapture(capture); 
                ImageIO.write(Image, "png", new File(path)); 
                string = new ToString(path);
                contenido.add(string.Lectura());
                nameRec.add(nombres.get(i));
            }  
        } 
        catch (AWTException | IOException ex) { 
            System.out.println(ex); 
        }      
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Interfaz nueva = new Interfaz();
                nueva.nuevaColumna("Rectangulo", nameRec);
                nueva.nuevaColumna("Contenido", contenido);

                nueva.terminarTabla();
                nueva.setVisible(true);
            }
        });
    }
    
    @FXML
    private void deletePButtonAction(ActionEvent event){
        eliminarPane.toFront();
        eliminarPane.setVisible(true);
    }
    
    @FXML
    private void acptButonnPlantilla(ActionEvent event){
        String ruta = jsonFile.getRuta();
        File na = new File(ruta);
        na.delete();
        eliminarPane.setVisible(false);
        plantillaLabel.setText("Se eliminó -> "+jsonFile.getRuta());
        jsonFile.setRuta("");
    }
    
    @FXML
    private void cancelButtonP(ActionEvent event){
        eliminarPane.setVisible(false);
    }
            
    @FXML
    private void saveButtonAction(ActionEvent event){
        disableButton.setVisible(true);
        savePane.toFront();
        savePane.setVisible(true);
        fileName.clear();
        plantillaLabel.setText("Se guardó -> " + jsonFile.getRuta());
    } 
    
    @FXML
    private void acceptNameButton(ActionEvent event) throws InterruptedException{
        disableButton.setVisible(false);
        savePane1.setVisible(false);
        nombres.add(nombreRec.getText());        
        nombreRec.clear();
    }
    
    @FXML
    private void cancelNameButton(ActionEvent event){
        disableButton.setVisible(false);
        savePane1.setVisible(false);
        
        for (int i = 0; i <= listRec.size(); i++) {
            if (i == listRec.size()-1){
                listRec.remove(i);   
            }
        }
        
        gc = contenidoPDF.getGraphicsContext2D();
        gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
        gc = drawPane.getGraphicsContext2D();
        gc.clearRect(0, 0, contenidoPDF.getWidth(), contenidoPDF.getHeight());
        for (int i = 0; i < listRec.size(); i++) {
            gc.strokeRect(listRec.get(i).getX(), listRec.get(i).getY(),
            listRec.get(i).getWidth(), listRec.get(i).getHeight());
        }
    }
    
    @FXML
    private void acceptSaveButtonAction(ActionEvent event) throws IOException{
        disableButton.setVisible(false);
        fileName.setText(fileName.getText() + ".json");
        ArrayList<Rectangle> n = new ArrayList<>();
        for (int i = 0; i < listRec.size(); i++) {
            n.add(listRec.get(i));
        }
        jsonFile.setRectangulos(n);
        jsonFile.escritura(fileName.getText());
        savePane.setVisible(false);
        plantillaLabel.setText("Se guardó ->" + jsonFile.getRuta());
    }
    
    @FXML
    private void cargaButtonAction(ActionEvent event){
        seleccionarArchivo = new JFileChooser();
        seleccionarArchivo.showOpenDialog(null);
        archivoSeleccionado = seleccionarArchivo.getSelectedFile(); 
        String ruta;
        ruta = archivoSeleccionado.getPath();
        ArrayList<Rectangle>cargaR = jsonFile.lectura(ruta);
        jsonFile.setRuta(ruta);
        File cargado = new File(ruta);
        jsonFile.setFichero(cargado);
        gc = contenidoPDF.getGraphicsContext2D();
        gc.clearRect(0, 0, contenidoPDF.getWidth(), contenidoPDF.getHeight());
        listRec = new ArrayList<>(cargaR);
        for (int i = 0; i < listRec.size(); i++) {
            gc.strokeRect(listRec.get(i).getX(), listRec.get(i).getY(),
            listRec.get(i).getWidth(), listRec.get(i).getHeight());
        }
        jsonFile.setRectangulos(listRec);
        jsonFile.setRuta(ruta);
        plantillaLabel.setText(jsonFile.getRuta());
    }
    @FXML
    private void sobreEscritura(ActionEvent event) throws IOException{
        ArrayList<Rectangle> n = new ArrayList<>();
        for (int i = 0; i < listRec.size(); i++) {
            n.add(listRec.get(i));       
        }
        //jsonFile.setRectangulos(n);
        jsonFile.sobreEscritura(n);
        plantillaLabel.setText(jsonFile.getRuta()+" guardado correctamente!!");
    }
    
    @FXML
    private void undoButtonAction(ActionEvent event){    
        if(!stackundo.isEmpty()){
            gc = contenidoPDF.getGraphicsContext2D();
            gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
            gc = drawPane.getGraphicsContext2D();
            gc.clearRect(0, 0, contenidoPDF.getWidth(), contenidoPDF.getHeight());
            stackredo.push(stackundo.peek());
            stackundo.pop();
            
            for (int i = 0; i <= listRec.size(); i++) {
                if (i == listRec.size()-1){
                    listRec.remove(i);   
                }
            }   
            for (int i = 0; i < listRec.size(); i++) {
                gc.strokeRect(listRec.get(i).getX(), listRec.get(i).getY(),
                listRec.get(i).getWidth(), listRec.get(i).getHeight());
            }
        }
    }
    
    @FXML
    private void redoButtonAction(ActionEvent event){
       if (!stackredo.isEmpty()){
            gc = contenidoPDF.getGraphicsContext2D();
            gc.clearRect(0, 0, contenidoPDF.getWidth(), contenidoPDF.getHeight());
            gc = drawPane.getGraphicsContext2D();
            gc.clearRect(0, 0, contenidoPDF.getWidth(), contenidoPDF.getHeight());
            stackundo.push(stackredo.peek());
            listRec.add((Rectangle) stackredo.peek());
            stackredo.pop();
            
            for (int i = 0; i < listRec.size(); i++) {
                gc.strokeRect(listRec.get(i).getX(), listRec.get(i).getY(),
                listRec.get(i).getWidth(), listRec.get(i).getHeight());
            }     
        }
    }
    
    @FXML
    private void keyPress(KeyEvent event) {
        if (event.isControlDown() == true) {
            if (event.getText().equals("z")) {
                undobtn.fire();
          
            }
        }
        if (event.isControlDown() == true) {
            if (event.getText().equals("y")) {
                redobtn.fire();
            }
        }
    }
    
    @FXML
    private void addFileButtonAction(ActionEvent event) throws IOException{
        pagina = new ArrayList<>();
        panelPDF.getChildren().clear();
        seleccionarArchivo = new JFileChooser();
        seleccionarArchivo.showOpenDialog(null);
        archivoSeleccionado = seleccionarArchivo.getSelectedFile(); 
        try (PDDocument document = PDDocument.load(archivoSeleccionado)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page)
            {
                bim = pdfRenderer.renderImage(page, 2);
                pagina.add(bim);                            
            }           
        }
        tamañoPDF.setPrefWidth(bim.getWidth());
        drawPane.setWidth(bim.getWidth());
        drawPane.setHeight(bim.getHeight());
        contenidoPDF.setWidth(bim.getWidth());
        contenidoPDF.setHeight(bim.getHeight());
        
        tabPane.setPrefHeight(bim.getHeight());
        tabPane.setPrefWidth(bim.getWidth());
        
        Image i = SwingFXUtils.toFXImage(bim, null);
        ImageView v = new ImageView(i);
        panelPDF.getChildren().add(v);
        listRec = new ArrayList<>();
        drawButton.setDisable(false);
        selectButton.setDisable(false);
        deleteButton.setDisable(false);
        saveButton.setDisable(false);
        extraer.setDisable(false);
        cargaButton.setDisable(false);
        sobreEscritura.setDisable(false);
        borrarButton.setDisable(false);
        RecTextButton.setDisable(false);
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
            gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
            gc = contenidoPDF.getGraphicsContext2D();
            gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
            gc.setLineWidth(1);    
            for (int i = 0; i < listRec.size(); i++) {
                gc.strokeRect(listRec.get(i).getX(), listRec.get(i).getY(),
                listRec.get(i).getWidth(), listRec.get(i).getHeight());
            }
        }   
    }
     
    @FXML
    private void selectRectangle(MouseEvent event){ 
        
        //nameRec.setVisibler(true);
        modificarRec = new ArrayList<>();
        gc = contenidoPDF.getGraphicsContext2D();
        gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
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
                //modificarRec.add(new Rectangle());
                Rectangle f = new Rectangle();
                f.setRect(rec.getX() - 5, rec.getY() - 5, 10.0, 10.0);
                modificarRec.add(f);
                Rectangle f2 = new Rectangle();
                f2.setRect(rec.getX() + rec.getWidth() - 5, rec.getY() - 5, 10.0, 10.0);
                modificarRec.add(f2);
                Rectangle f3 = new Rectangle();
                f3.setRect(rec.getX() - 5, rec.getY() + rec.getHeight() - 5, 10.0, 10.0);
                modificarRec.add(f3);
                Rectangle f4 = new Rectangle();
                f4.setRect(rec.getX() + rec.getWidth() - 5, rec.getY() + rec.getHeight() - 5, 10.0, 10.0);
                modificarRec.add(f4);
                gc.setFill(Color.WHITE);
                for (int j = 0; j < modificarRec.size(); j++) {
                    gc.fillRect(modificarRec.get(j).getX(), modificarRec.get(j).getY(),
                    modificarRec.get(j).getWidth(), modificarRec.get(j).getHeight());
                    gc.strokeRect(modificarRec.get(j).getX(), modificarRec.get(j).getY(),
                    modificarRec.get(j).getWidth(), modificarRec.get(j).getHeight());        
                }
                
                System.out.println(i);
                System.out.println(nombres.toString());
                System.out.println(nombres.get(i));
                nameRec.setText(nombres.get(i));
                nameRec.setVisible(true);
                nameRec.toFront();
                
            }
            /*else{
                nameRec.setVisible(false);
              
            }*/
            
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
    }
    
    @FXML
    private void modifyDragged(MouseEvent event){
        switch (aux) {
            case 1:
                rec.setRect(event.getX(), event.getY(), (auxX + auxW) - event.getX(), (auxY + auxH) - event.getY());
                gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
                gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                if (rec.getWidth() < 0 || rec.getHeight() < 0) {   
                    rec.setRect(auxX + auxW, event.getY(), event.getX() - (auxX + auxW),(auxY + auxH) - event.getY() );                    
                    if (rec.getWidth() < 0) {
                        rec.setRect(event.getX(), auxY + auxH, (auxX + auxW) - event.getX(), event.getY() - (auxY + auxH));
                    }
                    else if (rec.getHeight() < 0) {
                        rec.setRect(auxX + auxW,auxY + auxH,event.getX() - (auxX + auxW),event.getY() - (auxY + auxH));
                    }
                    gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }                            
                break;
            case 2:
                rec.setRect(auxX, event.getY(), event.getX() - auxX, (auxY + auxH) - event.getY());            
                gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
                gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());                
                if (rec.getWidth() < 0 || rec.getHeight() < 0) {
                    rec.setRect(event.getX(),event.getY(), auxX - event.getX(),(auxH + auxY) - event.getY()); 
                    if (rec.getWidth() < 0) {
                        rec.setRect(auxX, auxY + auxH, event.getX() - auxX, event.getY() - (auxY + auxH));
                    }
                    else if (rec.getHeight() < 0) {
                        rec.setRect(event.getX(), auxH + auxY, auxX - event.getX(), event.getY() - (auxY + auxH));
                    }
                    gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }
                break;
            case 3:
                rec.setRect(event.getX(),auxY,(auxX + auxW) - event.getX(),event.getY() - auxY);            
                gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
                gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                if (rec.getWidth() < 0 || rec.getHeight() < 0) {
                    rec.setRect(auxX + auxW, auxY, event.getX() - (auxX + auxW), event.getY() - auxY);
                    if (rec.getWidth() < 0) {
                        rec.setRect(event.getX(),event.getY(),(auxX + auxW) - event.getX(),auxY - event.getY());
                    }
                    else if (rec.getHeight() < 0) {
                        rec.setRect(auxX + auxW,event.getY(),event.getX() - (auxX + auxW),auxY - event.getY());                     
                    }
                    gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }
                break;
            case 4:
                rec.setRect(auxX, auxY, event.getX() - auxX, event.getY() - auxY);            
                gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
                gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                if (rec.getWidth() < 0 || rec.getHeight() < 0) {
                    rec.setRect(auxX, event.getY(), event.getX() - auxX, auxY - event.getY());
                    if (rec.getWidth() < 0) {
                        rec.setRect(event.getX(),event.getY(),auxX - event.getX(),auxY - event.getY());
                    }
                    if (rec.getHeight() < 0) {
                        rec.setRect(event.getX(),auxY,auxX - event.getX(),event.getY() - auxY);                     
                    }
                    gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
                    gc.strokeRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                }
                break;
                
            case 5:
                for (int i = 0; i < listRec.size(); i++) { 
                    if (auxY < y && auxX < x && auxY + auxH > y && auxX + auxW > x) { 
                        //arriba izquierda 
                        if (event.getX() <= x && event.getY() <= y) { 
                            rec.x = (int) (auxX - (x - event.getX())); 
                            rec.y = (int) (auxY - (y - event.getY())); 
                        } 
                         
                        //arriba derecha 
                        if (event.getX() >= x && event.getY() <= y) { 
                            rec.x = (int) (auxX + (event.getX() - x)); 
                            rec.y = (int) (auxY - (y - event.getY())); 
                        } 
                         
                        //abajo izquierda 
                        if (event.getX() <= x && event.getY() >= y) { 
                            rec.x = (int) (auxX - (x - event.getX())); 
                            rec.y = (int) (auxY + (event.getY() - y)); 
                        } 
                                                                       
                        //abajo derecha 
                        if (event.getX() >= x && event.getY() >= y) { 
                            rec.x = (int) (auxX + (event.getX() - x)); 
                            rec.y = (int) (auxY + (event.getY() - y)); 
                        } 
                        gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
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
        gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
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
        if (!selectButton.getStyle().equals("-fx-background-color: #ccd0e6")) {
            selectButton.setStyle("-fx-background-color: #ccd0e6");
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
        if (!drawButton.getStyle().equals("-fx-background-color: #ccd0e6")) {
            drawButton.setStyle("-fx-background-color: #ccd0e6");
        }
    }   
    
    @FXML
    private void cancelSaveButtonAction(ActionEvent event){
        disableButton.setVisible(false);
        savePane.setVisible(false);
    }  
    
    @FXML
    private void disableButtonAction(ActionEvent event){
        disableButton.setVisible(false);
        savePane.setVisible(false);
        savePane.toBack();
        savePane1.setVisible(false);
        savePane1.toBack();
       
        
        
        gc = contenidoPDF.getGraphicsContext2D();
        gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
        gc = drawPane.getGraphicsContext2D();
        gc.clearRect(0, 0, contenidoPDF.getWidth(), contenidoPDF.getHeight());
        for (int i = 0; i < listRec.size(); i++) {
            gc.strokeRect(listRec.get(i).getX(), listRec.get(i).getY(),
            listRec.get(i).getWidth(), listRec.get(i).getHeight());
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
            gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
            gc.strokeRect(x, y, event.getX() - x, event.getY() - y);
        }   
        
        // Abajo izquierda
        if (event.getX() <  x && event.getY() > y) {
            gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());        
            gc.strokeRect(x - (x - event.getX()), y, x - event.getX(), event.getY() - y);
        }
        
        //Arriba izquierda
        if (event.getX() <  x && event.getY() < y) {
            gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
            gc.strokeRect(event.getX(), event.getY(), x - event.getX(), y - event.getY());
        }
        
        //Arriba derecha
        if (event.getX() > x && event.getY() < y) {
            gc.clearRect(0, 0, bim.getWidth(), bim.getHeight());
            gc.strokeRect(x, y - (y - event.getY()), event.getX() - x, y - event.getY());
        }
        
    }
    
    @FXML
    private void drawReleased(MouseEvent event) {
        gc = contenidoPDF.getGraphicsContext2D();   
        // Abajo derecha
        rec = new Rectangle();
        if (event.getX() > x && event.getY() > y) {
            gc.strokeRect(x, y, event.getX() - x, event.getY() - y);
            rec.setRect(x, y, event.getX() - x, event.getY() - y);
        }   
        
        // Abajo izquierda
        else if (event.getX() <  x && event.getY() > y) {      
            gc.strokeRect(x - (x - event.getX()), y, x - event.getX(), event.getY() - y);
            rec.setRect(x - (x - event.getX()), y, x - event.getX(), event.getY() - y);

        }
        
        //Arriba izquierda
        else if (event.getX() <  x && event.getY() < y) {
            gc.strokeRect(event.getX(), event.getY(), x - event.getX(), y - event.getY());
            rec.setRect(event.getX(), event.getY(), x - event.getX(), y - event.getY());
        }
        
        //Arriba derecha
        else if (event.getX() > x && event.getY() < y) {
            gc.strokeRect(x, y - (y - event.getY()), event.getX() - x, y - event.getY());
            rec.setRect(x, y - (y - event.getY()), event.getX() - x, y - event.getY());
        }

        listRec.add(rec);
        stackundo.push(rec);
        
        disableButton.setVisible(true);
        savePane1.toFront();
        savePane1.setVisible(true);
        
        rec = new Rectangle();
           
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
            button.setStyle("-fx-background-color: #ccd0e6;");
        }
        else{
            if (!button.getStyle().equals("-fx-background-color: #8b008b")) {
                button.setStyle("-fx-background-color: #ccd0e6");
            }
        }
    }
    
    @FXML
    private void press(MouseEvent event){
        if (button.getId().equals("drawButton") || button.getId().equals("selectButton")) {
            if (button.getStyle().equals("-fx-background-color: #8b008b")) {
                button.setStyle("-fx-background-color: #ccd0e6");
            }
            else{
                button.setStyle("-fx-background-color: #8b008b");
            }  
        }
        else{
            button.setStyle("-fx-background-color: #AAAAAA;");
        }       
    }
    @FXML
    private void extraerTextoButton(){
        url = archivoSeleccionado.getPath();
        panelTexto.setVisible(true);
        panelTexto.toFront();
        string = new ToString(url);
        OrdenCompra n = new OrdenCompra(string.Lectura());
        n.escribir();
        extraerTexto.setText(string.Lectura());
    }
    @FXML
    private void salirButtonaction(){
        panelTexto.setVisible(false);
        panelTexto.toBack();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tamañoPDF.setPrefHeight(screenHeight - 120);
        panelPDF.setPrefHeight(screenHeight - 120);
        disableButton.setPrefWidth(screenWidth);
        disableButton.setPrefHeight(screenHeight);
        jsonFile = new JsonRec();
        nombres = new ArrayList();       
    }       
}


class Interfaz extends javax.swing.JFrame{
    private DefaultTableModel modeltabla;
    private JTable jTable1;
    public Interfaz(){
        initComponents();
        this.modeltabla = new DefaultTableModel();
    }
    public void nuevaColumna(String titulo, ArrayList n){
        String col[]  = new String[n.size()];
        for (int i = 0; i < n.size(); i++) {
            col[i] = (String) n.get(i);
        }
        modeltabla.addColumn(titulo,col);
    }
    public void terminarTabla(){
        jTable1.setModel(modeltabla); 
        
    }
        // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "", "", "", ""
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, (int) Screen.getPrimary().getBounds().getWidth(), javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, (int) Screen.getPrimary().getBounds().getHeight(), javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold> 
}

