package Controller;

import Model.Person;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.collections.ObservableList;

import java.io.FileOutputStream;

public class PdfGeneration {

    private void insertImage(Document document, String imageURL) throws Exception{
        Image image1= Image.getInstance(getClass().getResource(imageURL));
        image1.scaleToFit(400,172);
        image1.setAbsolutePosition(170f,650f);
        document.add(image1);
        document.add(new Paragraph("\n\n\n\n\n\n\n\n\n"));
    }

    private void generateTable(Document document, ObservableList<Person> data) throws Exception{
        float[] columnWidths = {2, 4, 4, 6};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell(new Phrase("Kontaktlista"));
        cell.setBackgroundColor(GrayColor.GRAYWHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(4);
        table.addCell(cell);

        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("Sorszám");
        table.addCell("Vezetéknév");
        table.addCell("Keresztnév");
        table.addCell("E-mail cím");
        table.setHeaderRows(1);

        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        for (int i=1; i<=data.size(); i++){
            Person actualPerson = data.get(i-1);

            table.addCell("" + i);
            table.addCell(actualPerson.getLastName());
            table.addCell(actualPerson.getFirstName());
            table.addCell(actualPerson.getEmail());
        }

        document.add(table);
    }

    private void addSignature(Document document, String content) throws Exception{
        Chunk signature = new Chunk("\n\n" + content);
        Paragraph base = new Paragraph(signature);
        document.add(base);
    }

    public void pdfGeneration(String filename, ObservableList<Person> data){
        Document document = new Document();
        try{

            PdfWriter.getInstance(document, new FileOutputStream(filename + ".pdf"));
            document.open();

            insertImage(document,"/imageSources/pdflogo.jpg");
            generateTable(document,data);
            addSignature(document,"Generálva a Telefonkönyv alkalmazás segítségével.");

        }catch (Exception e){
            e.printStackTrace();
        }
        document.close();
    }
}
