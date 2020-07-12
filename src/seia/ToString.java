/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seia;

import java.io.File;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract; 
import net.sourceforge.tess4j.TesseractException; 

/**
 *
 * @author nestor
 */
public class ToString {
    
    String str;
    
    public ToString(String ruta){
        ITesseract t = new Tesseract(); 

        try{
            str = t.doOCR(new File(ruta));

        } catch (TesseractException e){
            System.out.println("Exception" + e.getMessage());
        }
      
    }
    
    public String Lectura(){
        
        
        return str;
    }


}
