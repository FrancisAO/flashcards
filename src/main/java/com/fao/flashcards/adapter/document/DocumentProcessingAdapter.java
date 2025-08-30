package com.fao.flashcards.adapter.document;

import com.fao.flashcards.domain.port.DocumentProcessingPort;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * Adapter für die Extraktion von Text aus verschiedenen Dokumentformaten.
 * Implementiert den DocumentProcessingPort und unterstützt PDF, DOCX und
 * TXT-Dateien.
 */
@Component
public class DocumentProcessingAdapter implements DocumentProcessingPort {

    @Override
    public String extractTextFromDocument(InputStream documentStream, String fileName)
            throws DocumentProcessingPort.DocumentProcessingException {
        try {
            if (fileName.toLowerCase().endsWith(".pdf")) {
                return extractTextFromPdf(documentStream);
            } else if (fileName.toLowerCase().endsWith(".docx")) {
                return extractTextFromDocx(documentStream);
            } else if (fileName.toLowerCase().endsWith(".txt")) {
                return extractTextFromTxt(documentStream);
            } else {
                throw new DocumentProcessingException("Nicht unterstütztes Dateiformat: " + fileName);
            }
        } catch (Exception e) {
            throw new DocumentProcessingException("Fehler bei der Textextraktion: " + e.getMessage(), e);
        }
    }

    /**
     * Extrahiert Text aus einer PDF-Datei.
     * 
     * @param pdfStream Der InputStream der PDF-Datei
     * @return Der extrahierte Text
     * @throws Exception wenn bei der Extraktion ein Fehler auftritt
     */
    private String extractTextFromPdf(InputStream pdfStream) throws Exception {

        try (RandomAccessReadBuffer buffer = new RandomAccessReadBuffer(pdfStream);
                PDDocument document = Loader.loadPDF(buffer)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    /**
     * Extrahiert Text aus einer DOCX-Datei.
     * 
     * @param docxStream Der InputStream der DOCX-Datei
     * @return Der extrahierte Text
     * @throws Exception wenn bei der Extraktion ein Fehler auftritt
     */
    private String extractTextFromDocx(InputStream docxStream) throws Exception {
        XWPFDocument document = new XWPFDocument(docxStream);
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);

        try (document; extractor) {
            return extractor.getText();
        }
    }

    /**
     * Extrahiert Text aus einer TXT-Datei.
     * 
     * @param txtStream Der InputStream der TXT-Datei
     * @return Der extrahierte Text
     * @throws Exception wenn bei der Extraktion ein Fehler auftritt
     */
    private String extractTextFromTxt(InputStream txtStream) throws Exception {
        return new String(txtStream.readAllBytes());
    }
}