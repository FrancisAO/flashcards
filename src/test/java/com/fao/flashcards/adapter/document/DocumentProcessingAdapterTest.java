package com.fao.flashcards.adapter.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fao.flashcards.cards.adapter.document.DocumentProcessingAdapter;
import com.fao.flashcards.cards.application.port.out.DocumentProcessingPort;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für den DocumentProcessingAdapter.
 */
public class DocumentProcessingAdapterTest {
    
    private DocumentProcessingAdapter documentProcessingAdapter;
    
    @BeforeEach
    public void setUp() {
        documentProcessingAdapter = new DocumentProcessingAdapter();
    }
    
    @Test
    public void testExtractTextFromTxt() throws IOException, DocumentProcessingPort.DocumentProcessingException {
        // Pfad zur Testdatei
        File testFile = new File("test-files/test-document.txt");
        
        // Überprüfen, ob die Datei existiert
        assertTrue(testFile.exists(), "Testdatei existiert nicht: " + testFile.getAbsolutePath());
        
        // Text aus der Datei extrahieren
        String extractedText = documentProcessingAdapter.extractTextFromDocument(
                new FileInputStream(testFile),
                testFile.getName()
        );
        
        // Überprüfen, ob der extrahierte Text nicht leer ist
        assertNotNull(extractedText, "Extrahierter Text ist null");
        assertFalse(extractedText.isEmpty(), "Extrahierter Text ist leer");
        
        // Überprüfen, ob der extrahierte Text den erwarteten Inhalt enthält
        assertTrue(extractedText.contains("Einführung in Java"), "Extrahierter Text enthält nicht den erwarteten Inhalt");
        assertTrue(extractedText.contains("Klassen und Objekte"), "Extrahierter Text enthält nicht den erwarteten Inhalt");
        assertTrue(extractedText.contains("Vererbung"), "Extrahierter Text enthält nicht den erwarteten Inhalt");
    }
}