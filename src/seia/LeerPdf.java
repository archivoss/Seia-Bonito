/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seia;
import java.io.File;
import java.io.FileInputStream;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.
PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.PDFTextStripper;

public class LeerPdf {  

    PDFParser parser;
    String parsedText;
    PDFTextStripper pdfStripper;
    PDDocument pdDoc;
    COSDocument cosDoc;
    PDDocumentInformation pdDocInfo;

    // PDFTextParser Constructor
    public LeerPdf() {
    }

    // Extract text from PDF Document
    public String pdftoText(File f) {

        if (!f.isFile()) {
            System.out.println("File " + f.getName() + " does not exist.");
            return null;
        }

        try {
            parser = new PDFParser(new FileInputStream(f));
        } catch (Exception e) {
            System.out.println("Unable to open PDF Parser.");
            return null;
        }

        try {
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            parsedText = pdfStripper.getText(pdDoc);
        } catch (Exception e) {
            System.out.println("An exception occured in parsing the PDF Document.");
            e.printStackTrace();
        try {
            if (cosDoc != null) cosDoc.close();
            if (pdDoc != null) pdDoc.close();
            } catch (Exception e1) {
                e.printStackTrace();
        }
        return null;
        }
        System.out.println("Done.");
        return parsedText;
    }
}
