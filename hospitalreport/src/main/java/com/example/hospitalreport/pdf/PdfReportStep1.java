package com.example.hospitalreport.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileOutputStream;
import java.net.URL;

public class PdfReportStep1 {
    public enum Status {
        HIGH, NORMAL, LOW
    }

    public Document createDocument(String outputPath) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        ClassLoader classLoader = getClass().getClassLoader();
        document.setMargins(30, 30, 20, 50);

        document.open();

        //Logo Image
        URL imageUrl = classLoader.getResource("static/image-removebg-preview.png");
        if (imageUrl == null) {
            throw new IllegalArgumentException("Logo image not found");
        }
        Image logo = Image.getInstance(imageUrl);
        logo.scaleToFit(80, 100);

        //QR
        URL qrImageUrl = classLoader.getResource("static/qr.png");
        if (qrImageUrl == null) {
            throw new IllegalArgumentException("QR image not found");
        }
        Image qrCodeImage = Image.getInstance(qrImageUrl);
        qrCodeImage.scaleToFit(60, 60);

        URL signatureUrl = getClass().getClassLoader().getResource("static/doctor jane sign.jpg");
        if (signatureUrl == null) {
            throw new IllegalArgumentException("Signature image not found");
        }


        // Define fonts
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY);
        Font reportTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.DARK_GRAY);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(64, 64, 64));
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.GRAY); // Normal text 10pt gray
        // Step 2: Header section
        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1, 4, 1});
        PdfPCell logoCell = new PdfPCell(logo, false);
//        logoCell.setBackgroundColor(BaseColor.GREEN);
        logoCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell infoCell = new PdfPCell();
//       infoCell.setBackgroundColor(BaseColor.GRAY);
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.addElement(new Paragraph("GENERAL HOSPITAL", titleFont));
        infoCell.addElement(new Paragraph("123 Main, Hyderabad\nPhone:123-456-789\ninfo@generalhospital.com", bodyFont));
        infoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell qrCell = new PdfPCell(qrCodeImage, false);
//       qrCell.setBackgroundColor(BaseColor.BLUE);
        qrCell.setBorder(Rectangle.NO_BORDER);
        qrCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        qrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        headerTable.addCell(logoCell);
        headerTable.addCell(infoCell);
        headerTable.addCell(qrCell);

        document.add(headerTable);

        LineSeparator separator = new LineSeparator();
        document.add(new Chunk(separator));

        // Step 3: Report title
        Paragraph title = new Paragraph("Health Checkup\nREPORT", reportTitleFont);
        title.setAlignment(Element.ALIGN_LEFT);
        title.setSpacingBefore(15f);
        title.setSpacingAfter(15f);
        document.add(title);

        // Step 4: Patient info
        PdfPTable patientTable = new PdfPTable(5);
        patientTable.setWidthPercentage(100);
        patientTable.setWidths(new float[]{2, 1, 1, 2, 2});
        patientTable.setSpacingAfter(10f);

        patientTable.addCell(getCell("Patient Name", labelFont, Element.ALIGN_LEFT));
        patientTable.addCell(getCell("Age", labelFont, Element.ALIGN_CENTER));
        patientTable.addCell(getCell("Gender", labelFont, Element.ALIGN_CENTER));
        patientTable.addCell(getCell("Date", labelFont, Element.ALIGN_CENTER));
        patientTable.addCell(getCell("Contact", labelFont, Element.ALIGN_RIGHT));

        patientTable.addCell(getCell("John Doe", valueFont, Element.ALIGN_LEFT));
        patientTable.addCell(getCell("29", valueFont, Element.ALIGN_CENTER));
        patientTable.addCell(getCell("Male", valueFont, Element.ALIGN_CENTER));
        patientTable.addCell(getCell("2025-08-26", valueFont, Element.ALIGN_CENTER));
        patientTable.addCell(getCell("555-123-4567", valueFont, Element.ALIGN_RIGHT));

        document.add(patientTable);

        // Step 5: Grouped test results table
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 1, 1.5f, 2, 2});
        table.setSpacingBefore(15f);

        BaseColor headerColor = new BaseColor(200, 200, 200);
        addHeaderCell(table, "Test Name", headerFont, headerColor);
        addHeaderCell(table, "Value", headerFont, headerColor);
        addHeaderCell(table, "Unit", headerFont, headerColor);
        addHeaderCell(table, "Result Status", headerFont, headerColor);
        addHeaderCell(table, "Comment", headerFont, headerColor);

        addGroupHeader(table, "BLOOD PROFILE", headerFont);

        addTestRow(table, "Panstigation", "1.5", "gidk", "High", "Normal", Status.HIGH, bodyFont);
        addTestRow(table, "WIRTS", "8.3", "M%/14", "Normal", "1114-6.9", Status.NORMAL, bodyFont);
        addTestRow(table, "Pinwrlty", "180", "M%/44", "Low", "12-22.00", Status.LOW, bodyFont);
        addTestRow(table, "Klsenate", "110", "regilel", "Normal", "1-0%4", Status.NORMAL, bodyFont);
        addTestRow(table, "Divreaored", "215", "ingial", "Low", "0-4-x 4", Status.LOW, bodyFont);

        addGroupHeader(table, "URINE PROFILE", headerFont);

        addTestRow(table, "Amesscatorre", "6.3", "mg", "Normal", "Normal", Status.NORMAL, bodyFont);
        addTestRow(table, "PH", "6.0", "nol", "Normal", "Normal", Status.NORMAL, bodyFont);
        addTestRow(table, "Movelw", "8.2", "mg", "Normal", "Normal", Status.NORMAL, bodyFont);

        addGroupHeader(table, "URINE PROFILE", headerFont);

        addTestRow(table, "Amesscatorre", "8.2", "mg", "Normal", "Normal", Status.NORMAL, bodyFont);
        addTestRow(table, "PH", "6.0", "nol", "Normal", "Normal", Status.NORMAL, bodyFont);
        addTestRow(table, "Movelw", "8,3", "mg", "Normal", "Normal", Status.NORMAL, bodyFont);

        document.add(table);

        // Step 6: Add summary and doctor's note section here
        Font legendFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);

        PdfPTable legendTable = new PdfPTable(1);
        legendTable.setWidthPercentage(30);
        legendTable.setSpacingBefore(10f);

        legendTable.addCell(createColoredLegendCell("High", new BaseColor(255, 102, 102), legendFont));     // Red for High
        legendTable.addCell(createColoredLegendCell("Normal", new BaseColor(153, 255, 153), legendFont));   // Green for Normal
        legendTable.addCell(createColoredLegendCell("Low", new BaseColor(255, 204, 102), legendFont));      // Yellow/Orange for Low

        PdfPCell legendCell = new PdfPCell(legendTable);
        legendCell.setBorder(Rectangle.BOX);
        legendCell.setPadding(5f);


        String doctorsNoteText = "\nPatient advised to maintain a balanced diet and regular exercise.\n" +
                "Follow-up appointment scheduled after 1 month.\n" +
                "Prescribed medication: Vitamin D supplements.";

        Font subheadingFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
        Phrase notesPhrase = new Phrase();
        notesPhrase.add(new Chunk("Doctor's Note:\n", subheadingFont));  // Subheading, can also be "Remarks:\n"

        notesPhrase.add(new Chunk(doctorsNoteText, normalFont));         // Actual note content

        PdfPCell notesCell = new PdfPCell(notesPhrase);
        notesCell.setBorder(Rectangle.BOX);
        notesCell.setPadding(10f);


        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(100);
        summaryTable.setSpacingBefore(20f);
        summaryTable.setWidths(new float[]{1, 3});

        summaryTable.addCell(legendCell);
        summaryTable.addCell(notesCell);

        document.add(summaryTable);
        Paragraph linkParagraph = new Paragraph();
        Chunk linkChunk = new Chunk("View Diagnostic Report", subheadingFont);
        linkChunk.setAnchor("https://www.medplusmart.com/diagnostics?srsltid=AfmBOopIr680Hj2wDcSjtHzMnQ3RWpoy0VoOn4rFM8tNpGsur2z2J5gh");
        linkParagraph.add(linkChunk);
        linkParagraph.setSpacingBefore(10f);

        document.add(linkParagraph);


        // Doctor signature below legend cell
        Image signatureImage = Image.getInstance(signatureUrl);
        signatureImage.scaleToFit(120, 60);  // adjust size as needed
        signatureImage.setAlignment(Element.ALIGN_LEFT);
        document.add(signatureImage);
        Paragraph signature = new Paragraph("Signature", normalFont);
        signature.setIndentationLeft(15f);
        signature.setSpacingBefore(10f);
        document.add(signature);


        // Close document only after all content is added
        document.close();

        return document;
    }

    //patient info table
    private PdfPCell getCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(alignment);
        cell.setPaddingBottom(5f);
        return cell;
    }

    //Legend section (High/Normal/Low).
    private PdfPCell createColoredLegendCell(String label, BaseColor color, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(label, font));
        cell.setBackgroundColor(color);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(7f);
        cell.setBorder(Rectangle.BOX);
        return cell;
    }

    //Header
    private void addHeaderCell(PdfPTable table, String text, Font font, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private void addGroupHeader(PdfPTable table, String groupTitle, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(groupTitle, font));
        cell.setColspan(6);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(7f);
        table.addCell(cell);
    }

    private void addTestRow(PdfPTable table, String testName, String value, String unit, String resultStatusText,
                            String comment, Status status, Font font) {
        // table.addCell(new PdfPCell(new Phrase(testName, font)));
        PdfPCell testNameCell = new PdfPCell(new Phrase(testName, font));
        testNameCell.setPadding(6f);
        table.addCell(testNameCell);


        // table.addCell(new PdfPCell(new Phrase(value, font)));
        // table.addCell(new PdfPCell(new Phrase(unit, font)));

        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        valueCell.setPadding(6f);
        table.addCell(valueCell);

        PdfPCell unitCell = new PdfPCell(new Phrase(unit, font));
        unitCell.setPadding(6f);
        table.addCell(unitCell);

        PdfPCell statusCell = new PdfPCell(new Phrase(resultStatusText, font));
        statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        statusCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        switch (status) {
            case HIGH:
                statusCell.setBackgroundColor(new BaseColor(255, 102, 102)); // light red
                break;
            case NORMAL:
                statusCell.setBackgroundColor(new BaseColor(153, 255, 153)); // light green
                break;
            case LOW:
                statusCell.setBackgroundColor(new BaseColor(255, 204, 102)); // light orange
                break;
        }
        table.addCell(statusCell);
        //table.addCell(new PdfPCell(new Phrase(comment, font)));
        PdfPCell commentCell = new PdfPCell(new Phrase(comment, font));
        commentCell.setPadding(6f);
        commentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(commentCell);


    }


}
