package com.fao.flashcards.domain.port;

import java.io.InputStream;

/**
 * Port für die Verarbeitung von Dokumenten.
 * Dieser Port wird vom Anwendungskern verwendet, um Text aus verschiedenen Dokumentformaten zu extrahieren.
 */
public interface DocumentProcessingPort {
    
    /**
     * Extrahiert Text aus einem Dokument.
     * 
     * @param documentStream Der Dokumentinhalt als InputStream
     * @param fileName Der Dateiname mit Erweiterung (für die Bestimmung des Dateityps)
     * @return Der extrahierte Text
     * @throws DocumentProcessingException wenn bei der Verarbeitung ein Fehler auftritt
     */
    String extractTextFromDocument(InputStream documentStream, String fileName) throws DocumentProcessingException;
    
    /**
     * Exception für Fehler bei der Dokumentenverarbeitung.
     */
    class DocumentProcessingException extends Exception {
        private static final long serialVersionUID = 1L;
        
        public DocumentProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
        
        public DocumentProcessingException(String message) {
            super(message);
        }
    }
}