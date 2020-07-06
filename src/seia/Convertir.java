/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seia;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect.Type;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author queve
 */
public class Convertir {
    Gson gson;
    ArrayList<Rectangle> r;
    String ruta;
    String archivo;
    
    public Convertir(ArrayList<Rectangle> n){
        this.archivo = "";
        this.ruta = "Rectangulos.json";
        this.r = n;
        gson = new Gson();
    }
    
    public void escritura() throws IOException{
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
    public ArrayList<Rectangle> lectura(){
        String fichero = "";
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                fichero += linea;
            }
            ArrayList<Rectangle> n = gson.fromJson(fichero, ArrayList.class);
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
