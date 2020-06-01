/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seia;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class LeerPdf {  
    
    // PDFTextParser Constructor
    public LeerPdf() {
    }

    // Extract text from PDF Document
    /**
     * 
     * @param f
     * @return 
     */
    public String pdftoText(File f) throws IOException {
        PDDocument doc = PDDocument.load(f);
        return new PDFTextStripper().getText(doc);
    }
}
