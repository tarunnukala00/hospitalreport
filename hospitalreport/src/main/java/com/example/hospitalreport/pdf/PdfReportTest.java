package com.example.hospitalreport.pdf;

public class PdfReportTest {
    public static void main(String[] args) {
        PdfReportStep1 report = new PdfReportStep1();
        try {
            // Update this path as needed - ensure you have write permission
            String outputPath = "D:/myreports/hospital_report.pdf";
            report.createDocument(outputPath);
            System.out.println("PDF generated successfully at: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
