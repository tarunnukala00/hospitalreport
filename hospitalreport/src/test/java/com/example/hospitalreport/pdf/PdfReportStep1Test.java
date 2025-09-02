import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.hospitalreport.pdf.PdfReportStep1;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

class PdfReportStep1Test {
    private PdfReportStep1 pdfReport;
    private String outputPath;
    private File outputFile;

    @BeforeEach
    void setup() {
        pdfReport = new PdfReportStep1();
        outputPath = "target/test-output/health-report.pdf";
        outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
    }

    @Test
    void testCreateDocument_vaildpath_ShouldGeneratePdf() throws Exception {
        pdfReport.createDocument(outputPath);
        assertTrue(outputFile.exists(), "PDF file should be created");
        assertTrue(outputFile.length() > 0, "PDF file should not be empty");
    }

    @Test
    void testCreateDocument_NullPath_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> pdfReport.createDocument(null));
    }

    @Test
    void testCreateDocument_invalidpath_shouldThrowException() {
        String invalidPath = "/invalid/path/health-report.pdf";
        assertThrows(IOException.class, () -> pdfReport.createDocument(invalidPath));
    }
   
}