/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seia;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import javafx.scene.shape.Rectangle;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.PrintWriter;

/**
 *
 * @author queve
 */
public class JsonRec {
    Gson gson;
    ArrayList<Rectangle> r;
    String ruta;
    String archivo;
    File fichero;

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public String getRuta() {
        return fichero.getPath();
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public File getFichero() {
        return fichero;
    }

    public void setFichero(File fichero) {
        this.fichero = fichero;
    }
    
    public JsonRec(ArrayList<Rectangle> n){
        this.archivo = "";
        this.ruta = "";
        this.r = n;
        gson = new Gson();
    }
    public JsonRec(){
        this.archivo = "";
        this.ruta = "";
        gson = new Gson();
    }
    
    public void escritura(String nombre) throws IOException{
        this.ruta = nombre;
        archivo = gson.toJson(r);
        try {
            fichero = new File(ruta);
            try (FileWriter escribir = new FileWriter(fichero, true)) {
                escribir.write(archivo);
                escribir.close();   
            }
        }
        catch (IOException e) {
            System.out.println("Error al escribir");
        }
        
    }
    
    public void sobreEscritura(ArrayList<Rectangle> n){
        File archivo1 = new File(this.ruta);
        archivo1.delete();

        archivo = gson.toJson(n);
        PrintWriter writer;
        try {
            
            File ficheroa = new File(ruta);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroa))) {
                bw.write("");
                bw.write(archivo);
                bw.close();
            }

        }
        catch (IOException e) {
            System.out.println("Error al escribir");
        }
    }
    
    public ArrayList<Rectangle> lectura(String nombre){
        String fichero = "";
        try (BufferedReader br = new BufferedReader(new FileReader(nombre))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                fichero += linea;
            }
            
        
            Rectangle[] na = gson.fromJson(fichero, Rectangle[].class);  
            ArrayList<Rectangle> n = new ArrayList<>();
                        
            n.addAll(Arrays.asList(na));
            br.close();
            
            return n;
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void setRectangulos(ArrayList<Rectangle> r) {
        this.r = r;
    }
    
}
