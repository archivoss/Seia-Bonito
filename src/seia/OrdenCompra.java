/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seia;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author queve
 */
public class OrdenCompra {
    String datos;
    
    public OrdenCompra(String n){
        this.datos = n;
        
        
    }
    public void escribir(){
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("prueba.txt");
            pw = new PrintWriter(fichero);
            pw.println(datos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
    }
}
