/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seia;
import com.google.gson.Gson;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author queve
 */
public class JsonRec {
    Gson gson;
    ArrayList<Rectangle> r;
    String ruta;
    String archivo;
    
    public JsonRec(ArrayList<Rectangle> n){
        this.archivo = "";
        this.ruta = "";
        this.r = n;
        gson = new Gson();
    }
    
    public void escritura(String nombre) throws IOException{
        this.ruta = nombre;
        archivo = gson.toJson(r);
         try {
            File fichero = new File(ruta);
            try (FileWriter escribir = new FileWriter(fichero, true)) {
                escribir.write(archivo);
                escribir.close();
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
            
            
            System.out.println(fichero);
            Rectangle[] na = gson.fromJson(fichero, Rectangle[].class);  
            ArrayList<Rectangle> n = new ArrayList<>();
                        
            n.addAll(Arrays.asList(na));
            
            
            return n;
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public ArrayList<Rectangle> getRectangulos() {
        return r;
    }

    public void setRectangulos(ArrayList<Rectangle> r) {
        this.r = r;
    }
    
}
