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
    /** // Codigo de screenshot de rectangulo v.1
     * Robot robot = new Robot();
        Rectangle rec = new Rectangle((int) x + 410, (int) y + 180, (int) currentX, (int) currentY);
        BufferedImage image = robot.createScreenCapture(rec);
        Image myImage = SwingFXUtils.toFXImage(image, null);
        display.setImage(myImage);
        ObjectOutputStream oos;
        File imageFile = new File("screen.jpg");
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
     */
}
